package com.jdc.themis.dealer.service.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jdc.themis.dealer.data.dao.RefDataDAO;
import com.jdc.themis.dealer.domain.AccountReceivableDurationItem;
import com.jdc.themis.dealer.domain.Dealer;
import com.jdc.themis.dealer.domain.Department;
import com.jdc.themis.dealer.domain.Duration;
import com.jdc.themis.dealer.domain.EmployeeFeeItem;
import com.jdc.themis.dealer.domain.EmployeeFeeSummaryItem;
import com.jdc.themis.dealer.domain.GeneralJournalItem;
import com.jdc.themis.dealer.domain.InventoryDurationItem;
import com.jdc.themis.dealer.domain.JobPosition;
import com.jdc.themis.dealer.domain.Menu;
import com.jdc.themis.dealer.domain.MenuHierachy;
import com.jdc.themis.dealer.domain.SalesServiceJournalItem;
import com.jdc.themis.dealer.domain.Vehicle;
import com.jdc.themis.dealer.service.RefDataQueryService;
import com.jdc.themis.dealer.utils.Performance;
import com.jdc.themis.dealer.web.domain.AccountReceivableDurationItemDetail;
import com.jdc.themis.dealer.web.domain.DealerDetail;
import com.jdc.themis.dealer.web.domain.DepartmentDetail;
import com.jdc.themis.dealer.web.domain.DurationDetail;
import com.jdc.themis.dealer.web.domain.EmployeeFeeItemDetail;
import com.jdc.themis.dealer.web.domain.EmployeeFeeSummaryItemDetail;
import com.jdc.themis.dealer.web.domain.GeneralJournalItemDetail;
import com.jdc.themis.dealer.web.domain.GetDepartmentResponse;
import com.jdc.themis.dealer.web.domain.GetGeneralJournalItemResponse;
import com.jdc.themis.dealer.web.domain.GetMenuResponse;
import com.jdc.themis.dealer.web.domain.GetSalesServiceJournalItemResponse;
import com.jdc.themis.dealer.web.domain.GetVehicleResponse;
import com.jdc.themis.dealer.web.domain.HumanResourceAllocationItemDetail;
import com.jdc.themis.dealer.web.domain.InventoryDurationItemDetail;
import com.jdc.themis.dealer.web.domain.MenuDetail;
import com.jdc.themis.dealer.web.domain.MenuOrderItem;
import com.jdc.themis.dealer.web.domain.SalesServiceJournalItemDetail;
import com.jdc.themis.dealer.web.domain.VehicleDetail;

@Service
public class RefDataQueryServiceImpl implements RefDataQueryService {
	
	@Autowired
	private RefDataDAO refDataDAL;
	
	public RefDataDAO getRefDataDAL() {
		return refDataDAL;
	}

	public void setRefDataDAL(RefDataDAO refDataDAL) {
		this.refDataDAL = refDataDAL;
	}

	/**
	 * Get menu item details by id. 
	 * 
	 * @param id
	 * @return
	 */
	private MenuDetail getMenuByID(Integer id) {
		final Menu menu = refDataDAL.getMenu(id);
		final MenuDetail item = new MenuDetail();
		item.setId(menu.getId());
		item.setName(menu.getName());
		item.setDisplayText(menu.getDisplayText());
		item.setParentID(refDataDAL.getParentMenuID(id));
		
		final Collection<MenuHierachy> children = refDataDAL.getChildMenus(id);
		if ( children != null ) {
			for (final MenuHierachy child : children) {
				item.getChildren().add(
						new MenuOrderItem(
								child.getMenuHierachyID().getChildID(), 
								refDataDAL.getMenu(child.getMenuHierachyID().getChildID()).getName(), 
								child.getItemOrder()));
			}
		}
		return item;
	}
	
	/* Services */
	
	/**
	 * Get full list of menu. 
	 * Each menu includes its parent id and child id list.
	 * 
	 * @return
	 */
	@Performance
	@Override
	public GetMenuResponse getAllMenu() {
		final GetMenuResponse response = new GetMenuResponse();

		for (final Menu menu : refDataDAL.getMenus()) {
			final MenuDetail item = getMenuByID(menu.getId());
			response.getItems().add(item);
		}
		return response;
	}

	/**
	 * Get full list of vehicles. 
	 * 
	 * @return
	 */
	@Performance
	public GetVehicleResponse getAllVehicles() {
		final GetVehicleResponse response = new GetVehicleResponse();

		for (final Vehicle vehicle : refDataDAL.getVehicles()) {
			final VehicleDetail item = new VehicleDetail();
			item.setId(vehicle.getId());
			item.setName(vehicle.getName());
			item.setCategory(this.refDataDAL.getSalesServiceJournalCategory(vehicle.getCategoryID()).getName());
			item.setTimestamp(vehicle.getTimestamp());
			response.getItems().add(item);
		}
		return response;
	}

	/**
	 * Get full list of sales & service revenue items. 
	 * 
	 * @return
	 */
	@Performance
	public GetSalesServiceJournalItemResponse getAllSalesServiceRevenueItems() {
		final GetSalesServiceJournalItemResponse response = new GetSalesServiceJournalItemResponse();

		for (final SalesServiceJournalItem ssj : refDataDAL.getSalesServiceJournalItems()) {
			final SalesServiceJournalItemDetail item = new SalesServiceJournalItemDetail();
			item.setId(ssj.getId());
			item.setName(ssj.getName());
			response.getItems().add(item);
		}
		return response;
	}

	@Override
	public GetGeneralJournalItemResponse getAllGeneralIncomeItems() {
		final GetGeneralJournalItemResponse response = new GetGeneralJournalItemResponse();

		for (final GeneralJournalItem gji : refDataDAL.getGeneralJournalItems()) {
			final GeneralJournalItemDetail item = new GeneralJournalItemDetail();
			item.setId(gji.getId());
			item.setName(gji.getName());
			response.getItems().add(item);
		}
		return response;
	}

	@Override
	public GetDepartmentResponse getAllDepartments() {
		final GetDepartmentResponse response = new GetDepartmentResponse();

		for (final Department department : refDataDAL.getDepartments()) {
			final DepartmentDetail item = new DepartmentDetail();
			item.setId(department.getId());
			item.setName(department.getName());
			response.getItems().add(item);
		}
		return response;
	}

	@Override
	public MenuDetail getMenu(Integer id) {
		return getMenuByID(id);
	}

	@Override
	public DepartmentDetail getDepartment(Integer id) {
		final Department department = refDataDAL.getDepartment(id);
		final DepartmentDetail item = new DepartmentDetail();
		item.setId(department.getId());
		item.setName(department.getName());
		return item;
	}

	@Override
	public VehicleDetail getVehicle(Integer id) {
		final Vehicle vehicle = refDataDAL.getVehicle(id);
		final VehicleDetail item = new VehicleDetail();
		item.setId(vehicle.getId());
		item.setName(vehicle.getName());
		item.setCategory(this.refDataDAL.getSalesServiceJournalCategory(vehicle.getCategoryID()).getName());
		item.setTimestamp(vehicle.getTimestamp());
		return item;
	}

	@Override
	public SalesServiceJournalItemDetail getSalesServiceRevenueItem(Integer id) {
		final SalesServiceJournalItem ssj = refDataDAL.getSalesServiceJournalItem(id);
		final SalesServiceJournalItemDetail item = new SalesServiceJournalItemDetail();
		item.setId(ssj.getId());
		item.setName(ssj.getName());
		return item;
	}

	@Override
	public GeneralJournalItemDetail getGeneralIncomeItem(Integer id) {
		final GeneralJournalItem gji = refDataDAL.getGeneralJournalItem(id);
		final GeneralJournalItemDetail item = new GeneralJournalItemDetail();
		item.setId(gji.getId());
		item.setName(gji.getName());
		return item;
	}

	@Override
	public InventoryDurationItemDetail getInventoryDurationItem(Integer id) {
		final InventoryDurationItem gji = refDataDAL.getInventoryDurationItem(id);
		final InventoryDurationItemDetail item = new InventoryDurationItemDetail();
		item.setId(gji.getId());
		item.setName(gji.getName());
		return item;
	}

	@Override
	public AccountReceivableDurationItemDetail getAccountReceivableDurationItem(
			Integer id) {
		final AccountReceivableDurationItem gji = refDataDAL.getAccountReceivableDurationItem(id);
		final AccountReceivableDurationItemDetail item = new AccountReceivableDurationItemDetail();
		item.setId(gji.getId());
		item.setName(gji.getName());
		return item;
	}

	@Override
	public EmployeeFeeItemDetail getEmployeeFeeItem(Integer id) {
		final EmployeeFeeItem gji = refDataDAL.getEmployeeFeeItem(id);
		final EmployeeFeeItemDetail item = new EmployeeFeeItemDetail();
		item.setId(gji.getId());
		item.setName(gji.getName());
		return item;
	}

	@Override
	public EmployeeFeeSummaryItemDetail getEmployeeFeeSummaryItem(Integer id) {
		final EmployeeFeeSummaryItem gji = refDataDAL.getEmployeeFeeSummaryItem(id);
		final EmployeeFeeSummaryItemDetail item = new EmployeeFeeSummaryItemDetail();
		item.setId(gji.getId());
		item.setName(gji.getName());
		return item;
	}

	@Override
	public HumanResourceAllocationItemDetail getHumanResourceAllocationItem(
			Integer id) {
		final JobPosition gji = refDataDAL.getJobPosition(id);
		final HumanResourceAllocationItemDetail item = new HumanResourceAllocationItemDetail();
		item.setId(gji.getId());
		item.setName(gji.getName());
		return item;
	}

	@Override
	public DurationDetail getDuration(Integer id) {
		final Duration gji = refDataDAL.getDuration(id);
		final DurationDetail item = new DurationDetail();
		item.setId(gji.getId());
		item.setLowerBound(gji.getLowerBound());
		item.setUnit(gji.getUnit());
		item.setUpperBound(gji.getUpperBound());
		return item;
	}

	@Override
	public DealerDetail getDealer(Integer id) {
		final Dealer gji = refDataDAL.getDealer(id);
		final DealerDetail item = new DealerDetail();
		item.setId(gji.getId());
		item.setName(gji.getName());
		item.setCode(gji.getCode());
		item.setFullName(gji.getFullName());
		return item;
	}

}
