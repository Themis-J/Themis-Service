package com.jdc.themis.dealer.service.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.jdc.themis.dealer.data.dao.RefDataDAO;
import com.jdc.themis.dealer.domain.AccountReceivableDurationItem;
import com.jdc.themis.dealer.domain.Dealer;
import com.jdc.themis.dealer.domain.Department;
import com.jdc.themis.dealer.domain.Duration;
import com.jdc.themis.dealer.domain.EmployeeFeeItem;
import com.jdc.themis.dealer.domain.EmployeeFeeSummaryItem;
import com.jdc.themis.dealer.domain.EnumValue;
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
import com.jdc.themis.dealer.web.domain.GetAccountReceivableDurationItemResponse;
import com.jdc.themis.dealer.web.domain.GetDealerResponse;
import com.jdc.themis.dealer.web.domain.GetDepartmentResponse;
import com.jdc.themis.dealer.web.domain.GetEmployeeFeeItemResponse;
import com.jdc.themis.dealer.web.domain.GetEmployeeFeeSummaryItemResponse;
import com.jdc.themis.dealer.web.domain.GetGeneralJournalItemResponse;
import com.jdc.themis.dealer.web.domain.GetHumanResourceAllocationItemResponse;
import com.jdc.themis.dealer.web.domain.GetInventoryDurationItemResponse;
import com.jdc.themis.dealer.web.domain.GetMenuResponse;
import com.jdc.themis.dealer.web.domain.GetSalesServiceJournalItemResponse;
import com.jdc.themis.dealer.web.domain.GetVehicleResponse;
import com.jdc.themis.dealer.web.domain.HumanResourceAllocationItemDetail;
import com.jdc.themis.dealer.web.domain.InventoryDurationItemDetail;
import com.jdc.themis.dealer.web.domain.MenuDetail;
import com.jdc.themis.dealer.web.domain.MenuOrderItem;
import com.jdc.themis.dealer.web.domain.SalesServiceJournalItemDetail;
import com.jdc.themis.dealer.web.domain.VehicleDetail;

import fj.data.Option;

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
	public GetMenuResponse getMenu() {
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
	public GetVehicleResponse getVehicles(Option<Integer> categoryID) {
		final GetVehicleResponse response = new GetVehicleResponse();

		for (final Vehicle vehicle : refDataDAL.getVehicles(categoryID)) {
			final VehicleDetail item = new VehicleDetail();
			item.setId(vehicle.getId());
			item.setName(vehicle.getName());
			item.setCategoryID(vehicle.getCategoryID());
			item.setCategory(this.refDataDAL.getSalesServiceJournalCategory(vehicle.getCategoryID()).some().getName());
			item.setTypeID(vehicle.getType());
			item.setType(refDataDAL.getEnumValue("VehicleType", vehicle.getType()).some().getName());
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
	public GetSalesServiceJournalItemResponse getSalesServiceRevenueItems(Option<Integer> categoryID) {
		final GetSalesServiceJournalItemResponse response = new GetSalesServiceJournalItemResponse();
		final Integer expenseJournalType = refDataDAL.getEnumValue("JournalType", "Expense").some().getValue();
		final Integer revenueJournalType = refDataDAL.getEnumValue("JournalType", "Revenue").some().getValue();
		for (final SalesServiceJournalItem ssj : refDataDAL.getSalesServiceJournalItems(categoryID)) {
			final SalesServiceJournalItemDetail item = new SalesServiceJournalItemDetail();
			item.setId(ssj.getId());
			item.setName(ssj.getName());
			item.setCategoryID(ssj.getCategoryID());
			item.setCategory(this.refDataDAL.getSalesServiceJournalCategory(ssj.getCategoryID()).some().getName());
			if ( ssj.getJournalType().equals(expenseJournalType) ) {
				item.setJournalType("Expense");
			} else if ( ssj.getJournalType().equals(revenueJournalType) ) {
				item.setJournalType("Revenue");
			} 
			response.getItems().add(item);
		}
		return response;
	}

	@Override
	public GetGeneralJournalItemResponse getGeneralIncomeItems(Option<Integer> categoryID) {
		final GetGeneralJournalItemResponse response = new GetGeneralJournalItemResponse();
		final Integer expenseJournalType = refDataDAL.getEnumValue("JournalType", "Expense").some().getValue();
		final Integer revenueJournalType = refDataDAL.getEnumValue("JournalType", "Revenue").some().getValue();
		for (final GeneralJournalItem gji : refDataDAL.getGeneralJournalItems(categoryID)) {
			final GeneralJournalItemDetail item = new GeneralJournalItemDetail();
			item.setId(gji.getId());
			item.setName(gji.getName());
			item.setCategoryID(gji.getCategoryID());
			item.setCategory(this.refDataDAL.getGeneralJournalCategory(gji.getCategoryID()).some().getName());
			if ( gji.getJournalType().equals(expenseJournalType) ) {
				item.setJournalType("Expense");
			} else if ( gji.getJournalType().equals(revenueJournalType) ) {
				item.setJournalType("Revenue");
			} 
			response.getItems().add(item);
		}
		return response;
	}

	@Override
	public GetDepartmentResponse getDepartments() {
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
		Preconditions.checkArgument(refDataDAL.getDepartment(id).isSome(), "unknown department id");
		final Department department = refDataDAL.getDepartment(id).some();
		final DepartmentDetail item = new DepartmentDetail();
		item.setId(department.getId());
		item.setName(department.getName());
		return item;
	}

	@Override
	public VehicleDetail getVehicle(Integer id) {
		Preconditions.checkArgument(refDataDAL.getVehicle(id).isSome(), "unknown vehicle id");
		final Vehicle vehicle = refDataDAL.getVehicle(id).some();
		final VehicleDetail item = new VehicleDetail();
		item.setId(vehicle.getId());
		item.setName(vehicle.getName());
		item.setCategoryID(vehicle.getCategoryID());
		item.setCategory(this.refDataDAL.getSalesServiceJournalCategory(vehicle.getCategoryID()).some().getName());
		return item;
	}

	@Override
	public SalesServiceJournalItemDetail getSalesServiceRevenueItem(Integer id) {
		Preconditions.checkArgument(refDataDAL.getSalesServiceJournalItem(id).isSome(), "unknown item id");
		final SalesServiceJournalItem ssj = refDataDAL.getSalesServiceJournalItem(id).some();
		final SalesServiceJournalItemDetail item = new SalesServiceJournalItemDetail();
		item.setId(ssj.getId());
		item.setName(ssj.getName());
		item.setCategoryID(ssj.getCategoryID());
		item.setCategory(this.refDataDAL.getSalesServiceJournalCategory(ssj.getCategoryID()).some().getName());
		return item;
	}
	
	@Override
	public SalesServiceJournalItemDetail getSalesServiceRevenueItem(String name, String category) {
		final Option<SalesServiceJournalItem> ssjOption = refDataDAL.getSalesServiceJournalItem(name, refDataDAL.getSalesServiceJournalCategory(category).some().getId());
		Preconditions.checkArgument(ssjOption.isSome(), "unknown item id");
		final SalesServiceJournalItemDetail item = new SalesServiceJournalItemDetail();
		item.setId(ssjOption.some().getId());
		item.setName(ssjOption.some().getName());
		item.setCategoryID(ssjOption.some().getCategoryID());
		item.setCategory(this.refDataDAL.getSalesServiceJournalCategory(ssjOption.some().getCategoryID()).some().getName());
		return item;
	}

	@Override
	public GeneralJournalItemDetail getGeneralIncomeItem(Integer id) {
		Preconditions.checkArgument(refDataDAL.getGeneralJournalItem(id).isSome(), "unknown item id");
		final GeneralJournalItem gji = refDataDAL.getGeneralJournalItem(id).some();
		final GeneralJournalItemDetail item = new GeneralJournalItemDetail();
		item.setId(gji.getId());
		item.setName(gji.getName());
		item.setCategoryID(gji.getCategoryID());
		item.setCategory(this.refDataDAL.getGeneralJournalCategory(gji.getCategoryID()).some().getName());
		return item;
	}

	@Override
	public InventoryDurationItemDetail getInventoryDurationItem(Integer id) {
		Preconditions.checkArgument(refDataDAL.getInventoryDurationItem(id).isSome(), "unknown item id");
		final InventoryDurationItem gji = refDataDAL.getInventoryDurationItem(id).some();
		final InventoryDurationItemDetail item = new InventoryDurationItemDetail();
		item.setId(gji.getId());
		item.setName(gji.getName());
		return item;
	}

	@Override
	public AccountReceivableDurationItemDetail getAccountReceivableDurationItem(
			Integer id) {
		Preconditions.checkArgument(refDataDAL.getAccountReceivableDurationItem(id).isSome(), "unknown item id");
		final AccountReceivableDurationItem gji = refDataDAL.getAccountReceivableDurationItem(id).some();
		final AccountReceivableDurationItemDetail item = new AccountReceivableDurationItemDetail();
		item.setId(gji.getId());
		item.setName(gji.getName());
		return item;
	}

	@Override
	public EmployeeFeeItemDetail getEmployeeFeeItem(Integer id) {
		Preconditions.checkArgument(refDataDAL.getEmployeeFeeItem(id).isSome(), "unknown item id");
		final EmployeeFeeItem gji = refDataDAL.getEmployeeFeeItem(id).some();
		final EmployeeFeeItemDetail item = new EmployeeFeeItemDetail();
		item.setId(gji.getId());
		item.setName(gji.getName());
		return item;
	}

	@Override
	public EmployeeFeeSummaryItemDetail getEmployeeFeeSummaryItem(Integer id) {
		Preconditions.checkArgument(refDataDAL.getEmployeeFeeSummaryItem(id).isSome(), "unknown item id");
		final EmployeeFeeSummaryItem gji = refDataDAL.getEmployeeFeeSummaryItem(id).some();
		final EmployeeFeeSummaryItemDetail item = new EmployeeFeeSummaryItemDetail();
		item.setId(gji.getId());
		item.setName(gji.getName());
		return item;
	}

	@Override
	public HumanResourceAllocationItemDetail getHumanResourceAllocationItem(
			Integer id) {
		Preconditions.checkArgument(
				refDataDAL.getJobPosition(id).isSome(),
				"unknown job position id " + id);
		
		final JobPosition gji = refDataDAL.getJobPosition(id).some();
		final HumanResourceAllocationItemDetail item = new HumanResourceAllocationItemDetail();
		item.setId(gji.getId());
		item.setName(gji.getName());
		return item;
	}

	@Override
	public DurationDetail getDuration(Integer id) {
		Preconditions.checkArgument(refDataDAL.getDuration(id).isSome(), "unknown duration id");
		final Duration gji = refDataDAL.getDuration(id).some();
		final DurationDetail item = new DurationDetail();
		item.setId(gji.getId());
		item.setLowerBound(gji.getLowerBound());
		item.setUnit(gji.getUnit());
		item.setUpperBound(gji.getUpperBound());
		return item;
	}

	@Override
	public DealerDetail getDealer(Integer id) {
		Preconditions.checkArgument(refDataDAL.getDealer(id).isSome(), "unknown dealer id");
		final Dealer gji = refDataDAL.getDealer(id).some();
		final DealerDetail item = new DealerDetail();
		item.setId(gji.getId());
		item.setName(gji.getName());
		item.setCode(gji.getCode());
		item.setFullName(gji.getFullName());
		return item;
	}

	@Override
	public GetInventoryDurationItemResponse getInventoryDurationItems() {
		final GetInventoryDurationItemResponse response = new GetInventoryDurationItemResponse();

		for (final InventoryDurationItem gji : refDataDAL.getInventoryDurationItems()) {
			final InventoryDurationItemDetail item = new InventoryDurationItemDetail();
			item.setId(gji.getId());
			item.setName(gji.getName());
			response.getItems().add(item);
		}
		return response;
	}

	@Override
	public GetAccountReceivableDurationItemResponse getAccountReceivableDurationItems() {
		final GetAccountReceivableDurationItemResponse response = new GetAccountReceivableDurationItemResponse();

		for (final AccountReceivableDurationItem gji : refDataDAL.getAccountReceivableDurationItems()) {
			final AccountReceivableDurationItemDetail item = new AccountReceivableDurationItemDetail();
			item.setId(gji.getId());
			item.setName(gji.getName());
			response.getItems().add(item);
		}
		return response;
	}

	@Override
	public GetEmployeeFeeItemResponse getEmployeeFeeItems() {
		final GetEmployeeFeeItemResponse response = new GetEmployeeFeeItemResponse();

		for (final EmployeeFeeItem gji : refDataDAL.getEmployeeFeeItems()) {
			final EmployeeFeeItemDetail item = new EmployeeFeeItemDetail();
			item.setId(gji.getId());
			item.setName(gji.getName());
			response.getItems().add(item);
		}
		return response;
	}

	@Override
	public GetEmployeeFeeSummaryItemResponse getEmployeeFeeSummaryItems() {
		final GetEmployeeFeeSummaryItemResponse response = new GetEmployeeFeeSummaryItemResponse();

		for (final EmployeeFeeSummaryItem gji : refDataDAL.getEmployeeFeeSummaryItems()) {
			final EmployeeFeeSummaryItemDetail item = new EmployeeFeeSummaryItemDetail();
			item.setId(gji.getId());
			item.setName(gji.getName());
			response.getItems().add(item);
		}
		return response;
	}

	@Override
	public GetHumanResourceAllocationItemResponse getHumanResourceAllocationItems() {
		final GetHumanResourceAllocationItemResponse response = new GetHumanResourceAllocationItemResponse();

		for (final JobPosition gji : refDataDAL.getJobPositions()) {
			final HumanResourceAllocationItemDetail item = new HumanResourceAllocationItemDetail();
			item.setId(gji.getId());
			item.setName(gji.getName());
			response.getItems().add(item);
		}
		return response;
	}

	@Override
	public EnumValue getEnumValue(String enumType, Integer enumValue) {
		return refDataDAL.getEnumValue(enumType, enumValue).some();
	}

	@Override
	public EnumValue getEnumValue(String enumType, String enumValue) {
		return refDataDAL.getEnumValue(enumType, enumValue).some();
	}

	@Override
	public GetDealerResponse getDealers() {
		final GetDealerResponse response = new GetDealerResponse();
		for (final Dealer dealer : refDataDAL.getDealers()) {
			final DealerDetail item = new DealerDetail();
			item.setId(dealer.getId());
			item.setName(dealer.getName());
			item.setCode(dealer.getCode());
			item.setFullName(dealer.getFullName());
			response.getItems().add(item);
		}
		 
		return response;
	}

}
