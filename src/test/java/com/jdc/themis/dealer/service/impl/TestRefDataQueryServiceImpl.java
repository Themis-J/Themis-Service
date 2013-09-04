package com.jdc.themis.dealer.service.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.jdc.themis.dealer.data.dao.RefDataDAO;
import com.jdc.themis.dealer.domain.Menu;
import com.jdc.themis.dealer.domain.MenuHierachy;
import com.jdc.themis.dealer.domain.MenuHierachyId;
import com.jdc.themis.dealer.domain.SalesServiceJournalCategory;
import com.jdc.themis.dealer.domain.Vehicle;
import com.jdc.themis.dealer.web.domain.GetMenuResponse;
import com.jdc.themis.dealer.web.domain.GetVehicleResponse;

import fj.data.Option;

public class TestRefDataQueryServiceImpl {

	private RefDataQueryServiceImpl refDataQueryService;
	@Mock
	private RefDataDAO refDataDAL;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks( this );
		refDataDAL = mock(RefDataDAO.class);
		refDataQueryService = new RefDataQueryServiceImpl();
		refDataQueryService.setRefDataDAL(refDataDAL);
	} 
	
	@Test
	public void getAllMenu() {
		final List<Menu> menus = Lists.newArrayList();
		final Menu menu1 = new Menu();
		menu1.setId(1);
		menu1.setName("Menu1");
		menu1.setDisplayText("Menu1Text");
		menus.add(menu1);
		final Menu menu2 = new Menu();
		menu2.setId(2);
		menu2.setName("Menu2");
		menu2.setDisplayText("Menu2Text");
		menus.add(menu2);
		
		when(refDataDAL.getMenus()).thenReturn(menus);
		
		final MenuHierachy menuHierachy2 = new MenuHierachy();
		menuHierachy2.setItemOrder(2);
		final MenuHierachyId menuHierachyId2 = new MenuHierachyId();
		menuHierachyId2.setParentID(1);
		menuHierachyId2.setChildID(2);
		menuHierachy2.setMenuHierachyID(menuHierachyId2);
		
		when(refDataDAL.getChildMenus(1)).thenReturn(Arrays.asList(new MenuHierachy[]{menuHierachy2}));
		when(refDataDAL.getMenu(1)).thenReturn(menu1);
		when(refDataDAL.getMenu(2)).thenReturn(menu2);
		
		final GetMenuResponse response = refDataQueryService.getAllMenu();
		
		Assert.assertNotNull(response);
		Assert.assertEquals(2, response.getItems().size());
		Assert.assertEquals("Menu1Text", response.getItems().get(0).getDisplayText());
		Assert.assertEquals("Menu1", response.getItems().get(0).getName());
		Assert.assertEquals(1, response.getItems().get(0).getChildren().size());
	}
	
	@Test
	public void getAllVehicles() {
		final List<Vehicle> list = Lists.newArrayList();
		final Vehicle v1 = new Vehicle();
		final Vehicle v2 = new Vehicle();
		list.add(v1);
		list.add(v2);
		
		v1.setId(1);
		v1.setName("v1");
		v1.setCategoryID(1);
		v2.setId(2);
		v2.setName("v2");
		v2.setCategoryID(1);
		
		final SalesServiceJournalCategory category = new SalesServiceJournalCategory();
		category.setId(1);
		category.setName("VC1");
		when(refDataDAL.getVehicles()).thenReturn(list);
		when(refDataDAL.getSalesServiceJournalCategory(1)).thenReturn(Option.<SalesServiceJournalCategory>some(category));
		final GetVehicleResponse response = refDataQueryService.getAllVehicles();
		
		Assert.assertNotNull(response);
		Assert.assertEquals(2, response.getItems().size());
		Assert.assertEquals("v2", response.getItems().get(1).getName());
	}
	
	@Test
	public void getEmployeeFeeItem() {
		//TODO:
	}
	
	@Test
	public void getEmployeeFeeSummaryItem() {
		//TODO:
	}
	
	@Test
	public void getInventoryDurationItem() {
		//TODO:
	}
	
	@Test
	public void getAccountReceivableDurationItem() {
		//TODO:
	}
	
	@Test
	public void getHumanResourceAllocationItem() {
		//TODO:
	}
	
	@Test
	public void getDuration() {
		//TODO:
	}
}
