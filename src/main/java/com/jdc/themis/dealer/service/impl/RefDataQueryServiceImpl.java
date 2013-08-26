package com.jdc.themis.dealer.service.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jdc.themis.dealer.data.dao.RefDataDAO;
import com.jdc.themis.dealer.domain.Menu;
import com.jdc.themis.dealer.domain.MenuHierachy;
import com.jdc.themis.dealer.domain.SalesServiceJournalItem;
import com.jdc.themis.dealer.domain.Vehicle;
import com.jdc.themis.dealer.service.RefDataQueryService;
import com.jdc.themis.dealer.utils.Performance;
import com.jdc.themis.dealer.web.domain.GetMenuResponse;
import com.jdc.themis.dealer.web.domain.GetSalesServiceRevenueItemResponse;
import com.jdc.themis.dealer.web.domain.GetVehicleResponse;
import com.jdc.themis.dealer.web.domain.MenuItem;
import com.jdc.themis.dealer.web.domain.MenuOrderItem;
import com.jdc.themis.dealer.web.domain.SalesServiceRevenueItem;
import com.jdc.themis.dealer.web.domain.VehicleItem;

@Service
public class RefDataQueryServiceImpl implements RefDataQueryService {
	
	@Autowired
	private RefDataDAO refDataDAL;
	/**
	 * Get menu item details by id. 
	 * 
	 * @param id
	 * @return
	 */
	private MenuItem getMenuByID(Integer id) {
		final Menu menu = refDataDAL.getMenu(id);
		final MenuItem item = new MenuItem();
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
			final MenuItem item = getMenuByID(menu.getId());
			response.getItems().add(item);
		}
		return response;
	}

	/**
	 * Get full list of vehicles. 
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	@Performance
	public GetVehicleResponse getAllVehicles() {
		final GetVehicleResponse response = new GetVehicleResponse();

		for (final Vehicle vehicle : refDataDAL.getVehicles()) {
			final VehicleItem item = new VehicleItem();
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
	@Transactional(readOnly = true)
	@Performance
	public GetSalesServiceRevenueItemResponse getAllSalesServiceRevenueItems() {
		final GetSalesServiceRevenueItemResponse response = new GetSalesServiceRevenueItemResponse();

		for (final SalesServiceJournalItem ssj : refDataDAL.getSalesServiceJournalItems()) {
			final SalesServiceRevenueItem item = new SalesServiceRevenueItem();
			item.setId(ssj.getId());
			item.setName(ssj.getName());
			response.getItems().add(item);
		}
		return response;
	}

}
