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
import com.jdc.themis.dealer.domain.AccountReceivableDurationItem;
import com.jdc.themis.dealer.domain.Dealer;
import com.jdc.themis.dealer.domain.Department;
import com.jdc.themis.dealer.domain.EmployeeFeeItem;
import com.jdc.themis.dealer.domain.EmployeeFeeSummaryItem;
import com.jdc.themis.dealer.domain.EnumValue;
import com.jdc.themis.dealer.domain.GeneralJournalCategory;
import com.jdc.themis.dealer.domain.GeneralJournalItem;
import com.jdc.themis.dealer.domain.InventoryDurationItem;
import com.jdc.themis.dealer.domain.JobPosition;
import com.jdc.themis.dealer.domain.Menu;
import com.jdc.themis.dealer.domain.MenuHierachy;
import com.jdc.themis.dealer.domain.MenuHierachyId;
import com.jdc.themis.dealer.domain.SalesServiceJournalCategory;
import com.jdc.themis.dealer.domain.SalesServiceJournalItem;
import com.jdc.themis.dealer.domain.Vehicle;
import com.jdc.themis.dealer.web.domain.AccountReceivableDurationItemDetail;
import com.jdc.themis.dealer.web.domain.DealerDetail;
import com.jdc.themis.dealer.web.domain.DepartmentDetail;
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
import com.jdc.themis.dealer.web.domain.SalesServiceJournalItemDetail;

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
		
		final GetMenuResponse response = refDataQueryService.getMenu();
		final MenuDetail menuDetail1 = refDataQueryService.getMenu(1);
		Assert.assertNotNull(menuDetail1);
		Assert.assertEquals("Menu1", menuDetail1.getName());
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
		v1.setType(0);
		v2.setId(2);
		v2.setName("v2");
		v2.setCategoryID(1);
		v2.setType(0);
		
		final SalesServiceJournalCategory category = new SalesServiceJournalCategory();
		category.setId(1);
		category.setName("VC1");
		final EnumValue mini = new EnumValue();
		mini.setName("Mini");
		mini.setTypeID(6);
		mini.setValue(0);
		when(refDataDAL.getEnumValue("VehicleType", 0)).thenReturn(Option.<EnumValue>some(mini));
		when(refDataDAL.getVehicles(Option.<Integer>none())).thenReturn(list);
		when(refDataDAL.getVehicle(1)).thenReturn(Option.<Vehicle>some(v1));
		when(refDataDAL.getSalesServiceJournalCategory(1)).thenReturn(Option.<SalesServiceJournalCategory>some(category));
		final GetVehicleResponse response = refDataQueryService.getVehicles(Option.<Integer>none());
		Assert.assertNotNull(refDataQueryService.getVehicle(1));
		Assert.assertEquals("v1", refDataQueryService.getVehicle(1).getName());
		Assert.assertNotNull(response);
		Assert.assertEquals(2, response.getItems().size());
		Assert.assertEquals("v2", response.getItems().get(1).getName());
		Assert.assertEquals(1, response.getItems().get(1).getCategoryID().intValue());
		Assert.assertEquals("VC1", response.getItems().get(1).getCategory());
	}
	
	@Test
	public void getAllSalesServiceJournalItems() {
		final List<SalesServiceJournalItem> list = Lists.newArrayList();
		final SalesServiceJournalItem v1 = new SalesServiceJournalItem();
		final SalesServiceJournalItem v2 = new SalesServiceJournalItem();
		list.add(v1);
		list.add(v2);
		
		v1.setId(1);
		v1.setName("s1");
		v1.setJournalType(0);
		v1.setCategoryID(1);
		v2.setId(2);
		v2.setName("s2");
		v2.setJournalType(1);
		v2.setCategoryID(1);
		
		final SalesServiceJournalCategory category = new SalesServiceJournalCategory();
		category.setId(1);
		category.setName("SSJ1");
		when(refDataDAL.getSalesServiceJournalItems(Option.<Integer>none())).thenReturn(list);
		when(refDataDAL.getSalesServiceJournalCategory(1)).thenReturn(Option.<SalesServiceJournalCategory>some(category));
		final EnumValue expense = new EnumValue();
		expense.setName("Expense");
		expense.setTypeID(1);
		expense.setValue(1);
		final EnumValue revenue = new EnumValue();
		revenue.setName("Revenue");
		revenue.setTypeID(1);
		revenue.setValue(0);
		when(refDataDAL.getEnumValue("JournalType", "Expense")).thenReturn(Option.<EnumValue>some(expense));
		when(refDataDAL.getEnumValue("JournalType", "Revenue")).thenReturn(Option.<EnumValue>some(revenue));
		
		final GetSalesServiceJournalItemResponse response = refDataQueryService.getSalesServiceRevenueItems(Option.<Integer>none());
		
		Assert.assertNotNull(response);
		Assert.assertEquals(2, response.getItems().size());
		Assert.assertEquals("s2", response.getItems().get(1).getName());
		
		when(refDataDAL.getSalesServiceJournalItem(1)).thenReturn(Option.<SalesServiceJournalItem>some(v1));
		final SalesServiceJournalItemDetail item = refDataQueryService.getSalesServiceRevenueItem(1);
		Assert.assertEquals("s1", item.getName());
	}
	
	@Test
	public void getAllGeneralJournalItems() {
		final List<GeneralJournalItem> list = Lists.newArrayList();
		final GeneralJournalItem v1 = new GeneralJournalItem();
		final GeneralJournalItem v2 = new GeneralJournalItem();
		list.add(v1);
		list.add(v2);
		
		v1.setId(1);
		v1.setName("g1");
		v1.setJournalType(0);
		v1.setCategoryID(1);
		v2.setId(2);
		v2.setName("g2");
		v2.setJournalType(1);
		v2.setCategoryID(1);
		
		final GeneralJournalCategory category = new GeneralJournalCategory();
		category.setId(1);
		category.setName("GJ1");
		when(refDataDAL.getGeneralJournalItems(Option.<Integer>none())).thenReturn(list);
		when(refDataDAL.getGeneralJournalCategory(1)).thenReturn(Option.<GeneralJournalCategory>some(category));
		final EnumValue expense = new EnumValue();
		expense.setName("Expense");
		expense.setTypeID(1);
		expense.setValue(1);
		final EnumValue revenue = new EnumValue();
		revenue.setName("Revenue");
		revenue.setTypeID(1);
		revenue.setValue(0);
		when(refDataDAL.getEnumValue("JournalType", "Expense")).thenReturn(Option.<EnumValue>some(expense));
		when(refDataDAL.getEnumValue("JournalType", "Revenue")).thenReturn(Option.<EnumValue>some(revenue));
		
		final GetGeneralJournalItemResponse response = refDataQueryService.getGeneralIncomeItems(Option.<Integer>none());
		
		Assert.assertNotNull(response);
		Assert.assertEquals(2, response.getItems().size());
		Assert.assertEquals("g2", response.getItems().get(1).getName());
		
		when(refDataDAL.getGeneralJournalItem(1)).thenReturn(Option.<GeneralJournalItem>some(v1));
		final GeneralJournalItemDetail item = refDataQueryService.getGeneralIncomeItem(1);
		Assert.assertEquals("g1", item.getName());
	}
	
	@Test
	public void getAllDealers() {
		final List<Dealer> list = Lists.newArrayList();
		final Dealer v1 = new Dealer();
		final Dealer v2 = new Dealer();
		list.add(v1);
		list.add(v2);
		
		v1.setId(1);
		v1.setName("g1");
		v2.setId(2);
		v2.setName("g2");
		
		when(refDataDAL.getDealers()).thenReturn(list);
		final GetDealerResponse response = refDataQueryService.getDealers();
		
		Assert.assertNotNull(response);
		Assert.assertEquals(2, response.getItems().size());
		Assert.assertEquals("g1", response.getItems().iterator().next().getName());
		
		when(refDataDAL.getDealer(1)).thenReturn(Option.<Dealer>some(v1));
		final DealerDetail item = refDataQueryService.getDealer(1);
		Assert.assertEquals("g1", item.getName());
	}
	
	@Test
	public void getAllDepartments() {
		final List<Department> list = Lists.newArrayList();
		final Department v1 = new Department();
		final Department v2 = new Department();
		list.add(v1);
		list.add(v2);
		
		v1.setId(1);
		v1.setName("g1");
		v2.setId(2);
		v2.setName("g2");
		
		when(refDataDAL.getDepartments()).thenReturn(list);
		final GetDepartmentResponse response = refDataQueryService.getDepartments();
		
		Assert.assertNotNull(response);
		Assert.assertEquals(2, response.getItems().size());
		Assert.assertEquals("g1", response.getItems().get(0).getName());
		
		when(refDataDAL.getDepartment(1)).thenReturn(Option.<Department>some(v1));
		final DepartmentDetail item = refDataQueryService.getDepartment(1);
		Assert.assertEquals("g1", item.getName());
	}
	
	@Test
	public void getAllEmployeeFeeItems() {
		final List<EmployeeFeeItem> list = Lists.newArrayList();
		final EmployeeFeeItem v1 = new EmployeeFeeItem();
		final EmployeeFeeItem v2 = new EmployeeFeeItem();
		list.add(v1);
		list.add(v2);
		
		v1.setId(1);
		v1.setName("g1");
		v2.setId(2);
		v2.setName("g2");
		
		when(refDataDAL.getEmployeeFeeItems()).thenReturn(list);
		final GetEmployeeFeeItemResponse response = refDataQueryService.getEmployeeFeeItems();
		
		Assert.assertNotNull(response);
		Assert.assertEquals(2, response.getItems().size());
		Assert.assertEquals("g1", response.getItems().get(0).getName());
		
		when(refDataDAL.getEmployeeFeeItem(1)).thenReturn(Option.<EmployeeFeeItem>some(v1));
		final EmployeeFeeItemDetail item = refDataQueryService.getEmployeeFeeItem(1);
		Assert.assertEquals("g1", item.getName());
	}
	
	@Test
	public void getAllEmployeeFeeSummaryItems() {
		final List<EmployeeFeeSummaryItem> list = Lists.newArrayList();
		final EmployeeFeeSummaryItem v1 = new EmployeeFeeSummaryItem();
		final EmployeeFeeSummaryItem v2 = new EmployeeFeeSummaryItem();
		list.add(v1);
		list.add(v2);
		
		v1.setId(1);
		v1.setName("g1");
		v2.setId(2);
		v2.setName("g2");
		
		when(refDataDAL.getEmployeeFeeSummaryItems()).thenReturn(list);
		final GetEmployeeFeeSummaryItemResponse response = refDataQueryService.getEmployeeFeeSummaryItems();
		
		Assert.assertNotNull(response);
		Assert.assertEquals(2, response.getItems().size());
		Assert.assertEquals("g1", response.getItems().get(0).getName());
		
		when(refDataDAL.getEmployeeFeeSummaryItem(1)).thenReturn(Option.<EmployeeFeeSummaryItem>some(v1));
		final EmployeeFeeSummaryItemDetail item = refDataQueryService.getEmployeeFeeSummaryItem(1);
		Assert.assertEquals("g1", item.getName());
	}
	
	@Test
	public void getAllAccountReceivableDurationItems() {
		final List<AccountReceivableDurationItem> list = Lists.newArrayList();
		final AccountReceivableDurationItem v1 = new AccountReceivableDurationItem();
		final AccountReceivableDurationItem v2 = new AccountReceivableDurationItem();
		list.add(v1);
		list.add(v2);
		
		v1.setId(1);
		v1.setName("g1");
		v2.setId(2);
		v2.setName("g2");
		
		when(refDataDAL.getAccountReceivableDurationItems()).thenReturn(list);
		final GetAccountReceivableDurationItemResponse response = refDataQueryService.getAccountReceivableDurationItems();
		
		Assert.assertNotNull(response);
		Assert.assertEquals(2, response.getItems().size());
		Assert.assertEquals("g1", response.getItems().get(0).getName());
		
		when(refDataDAL.getAccountReceivableDurationItem(1)).thenReturn(Option.<AccountReceivableDurationItem>some(v1));
		final AccountReceivableDurationItemDetail item = refDataQueryService.getAccountReceivableDurationItem(1);
		Assert.assertEquals("g1", item.getName());
	}
	
	@Test
	public void getAllInventoryDurationItems() {
		final List<InventoryDurationItem> list = Lists.newArrayList();
		final InventoryDurationItem v1 = new InventoryDurationItem();
		final InventoryDurationItem v2 = new InventoryDurationItem();
		list.add(v1);
		list.add(v2);
		
		v1.setId(1);
		v1.setName("g1");
		v2.setId(2);
		v2.setName("g2");
		
		when(refDataDAL.getInventoryDurationItems()).thenReturn(list);
		final GetInventoryDurationItemResponse response = refDataQueryService.getInventoryDurationItems();
		
		Assert.assertNotNull(response);
		Assert.assertEquals(2, response.getItems().size());
		Assert.assertEquals("g1", response.getItems().get(0).getName());
		
		when(refDataDAL.getInventoryDurationItem(1)).thenReturn(Option.<InventoryDurationItem>some(v1));
		final InventoryDurationItemDetail item = refDataQueryService.getInventoryDurationItem(1);
		Assert.assertEquals("g1", item.getName());
	}
	
	@Test
	public void getAllJobPositionItems() {
		final List<JobPosition> list = Lists.newArrayList();
		final JobPosition v1 = new JobPosition();
		final JobPosition v2 = new JobPosition();
		list.add(v1);
		list.add(v2);
		
		v1.setId(1);
		v1.setName("g1");
		v2.setId(2);
		v2.setName("g2");
		
		when(refDataDAL.getJobPositions()).thenReturn(list);
		final GetHumanResourceAllocationItemResponse response = refDataQueryService.getHumanResourceAllocationItems();
		
		Assert.assertNotNull(response);
		Assert.assertEquals(2, response.getItems().size());
		Assert.assertEquals("g1", response.getItems().get(0).getName());
		
		when(refDataDAL.getJobPosition(1)).thenReturn(Option.<JobPosition>some(v1));
		final HumanResourceAllocationItemDetail item = refDataQueryService.getHumanResourceAllocationItem(1);
		Assert.assertEquals("g1", item.getName());
	}
}
