package com.jdc.themis.dealer.data.dao.hibernate;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.jdc.themis.dealer.data.dao.RefDataDAO;
import com.jdc.themis.dealer.domain.Duration;

import fj.data.Option;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
    "classpath:test-database-config.xml" })
@Transactional
public class TestRefDataDAOImpl {
	
	@Autowired
	private RefDataDAO refDataDAO;
	
	@Test
	public void getAllMenuAndCompareSize() {
		Assert.assertEquals(5, refDataDAO.getMenus().size());
	}

	@Test
	public void getAllMenuAndCompareFirstMenuName() {
		Assert.assertEquals(1, refDataDAO.getMenus().get(0).getId().intValue());
		Assert.assertEquals("DealerEntrySystem", refDataDAO.getMenus().get(0).getName());
		Assert.assertEquals("DealerEntrySystemText", refDataDAO.getMenus().get(0).getDisplayText());
	}
	
	@Test
	public void getDealerEntrySystemMenu() {
		Assert.assertEquals("DealerEntrySystem", refDataDAO.getMenu(1).getName());
	}
	
	@Test
	public void getDealerEntrySystemMenuChildren() {
		Assert.assertEquals(2, refDataDAO.getChildMenus(1).size());
	}
	
	@Test
	public void getDealerEntrySystemMenuParent() {
		Assert.assertEquals(null, refDataDAO.getParentMenuID(1));
	}
	
	@Test
	public void getDealerDataEntryMenuParent() {
		Assert.assertEquals(1, refDataDAO.getParentMenuID(2).intValue());
	}
	
	@Test
	public void getTaxJournalItemListSize() {
		Assert.assertEquals(1, refDataDAO.getTaxJournalItems().size());
	}
	
	@Test
	public void getDurationListSize() {
		Assert.assertEquals(2, refDataDAO.getDurations().size());
	}
	
	@Test
	public void getGeneralJournalCategoryListSize() {
		Assert.assertEquals(2, refDataDAO.getGeneralJournalCategorys().size());
	}
	
	@Test
	public void getGeneralJournalCategory() {
		Assert.assertEquals("GeneralJournalCate2", refDataDAO.getGeneralJournalCategory(2).some().getName());
	}
	
	@Test
	public void getDuration() {
		Assert.assertEquals(60, refDataDAO.getDuration(2).some().getUpperBound().intValue());
		Assert.assertEquals(Option.<Duration>none(), refDataDAO.getDuration(4));
	}
	
	@Test
	public void getSalesServiceJournalItemList() {
		Assert.assertEquals(3, refDataDAO.getSalesServiceJournalItems(Option.<Integer>none()).size());
	}
	@Test
	public void getSalesServiceJournalItemListByCategoryID() {
		Assert.assertEquals(2, refDataDAO.getSalesServiceJournalItems(Option.<Integer>some(1)).size());
	}
	
	@Test
	public void getSalesServiceJournalItemByName() {
		Assert.assertEquals("SalesServiceJournalItem1", refDataDAO.getSalesServiceJournalItem("SalesServiceJournalItem1", 1).some().getName());
	}
	
	@Test
	public void getSalesServiceJournalItem() {
		Assert.assertEquals("SalesServiceJournalItem1", refDataDAO.getSalesServiceJournalItem(1).some().getName());
		Assert.assertEquals(0, refDataDAO.getSalesServiceJournalItem(1).some().getJournalType().intValue());
	}
	
	@Test
	public void getGeneralJournalItemList() {
		Assert.assertEquals(2, refDataDAO.getGeneralJournalItems(Option.<Integer>none()).size());
	}
	
	@Test
	public void getGeneralJournalItemListByCategoryID() {
		Assert.assertEquals(1, refDataDAO.getGeneralJournalItems(Option.<Integer>some(1)).size());
	}
	
	@Test
	public void getVehicleList() {
		Assert.assertEquals(2, refDataDAO.getVehicles(Option.<Integer>none()).size());
	}
	
	@Test
	public void getVehicle() {
		Assert.assertEquals(0, refDataDAO.getVehicles(Option.<Integer>none()).get(0).getType().intValue());
	}
	
	@Test
	public void getVehicleListByCategoryID() {
		Assert.assertEquals(2, refDataDAO.getVehicles(Option.<Integer>some(1)).size());
	}
	
	@Test
	public void getGeneralJournalItem() {
		Assert.assertEquals("GeneralJournalItem1", refDataDAO.getGeneralJournalItem(1).some().getName());
		Assert.assertEquals(0, refDataDAO.getGeneralJournalItem(1).some().getJournalType().intValue());
		Assert.assertEquals(1, refDataDAO.getGeneralJournalItem(2).some().getJournalType().intValue());
	}
	
	@Test
	public void getAccountReceivableDurationItemList() {
		Assert.assertEquals(2, refDataDAO.getAccountReceivableDurationItems().size());
	}
	
	@Test
	public void getAccountReceivableDurationItem() {
		Assert.assertEquals("AccountReceivableDurationItem2", refDataDAO.getAccountReceivableDurationItem(2).some().getName());
	}
	
	@Test
	public void getInventoryDurationItemList() {
		Assert.assertEquals(3, refDataDAO.getInventoryDurationItems().size());
	}
	
	@Test
	public void getInventoryDurationItem() {
		Assert.assertEquals("InventoryDurationItem2", refDataDAO.getInventoryDurationItem(2).some().getName());
	}
	
	@Test
	public void getEmployeeFeeItemList() {
		Assert.assertEquals(1, refDataDAO.getEmployeeFeeItems().size());
	}
	
	@Test
	public void getEmployeeFeeItem() {
		Assert.assertEquals("EmployeeFeeItem1", refDataDAO.getEmployeeFeeItem(1).some().getName());
	}
	
	@Test
	public void getEmployeeFeeSummaryItemList() {
		Assert.assertEquals(2, refDataDAO.getEmployeeFeeSummaryItems().size());
	}
	
	@Test
	public void getEmployeeFeeSummaryItem() {
		Assert.assertEquals("EmployeeFeeSummaryItem1", refDataDAO.getEmployeeFeeSummaryItem(1).some().getName());
	}
	
	@Test
	public void getDepartmentList() {
		Assert.assertEquals(4, refDataDAO.getDepartments().size());
	}
	
	@Test
	public void getDealerList() {
		Assert.assertEquals(4, refDataDAO.getDealers().size());
	}
	
	@Test
	public void getDealer() {
		Assert.assertEquals("Dealer2", refDataDAO.getDealer(2).some().getName());
	}
	
	@Test
	public void getDepartment() {
		Assert.assertEquals("Department1", refDataDAO.getDepartment(1).some().getName());
	}
	
	@Test
	public void getJobPositionList() {
		Assert.assertEquals(5, refDataDAO.getJobPositions().size());
	}
	
	@Test
	public void getJobPosition() {
		Assert.assertEquals("JobPosition1", refDataDAO.getJobPosition(1).some().getName());
	}
	
	@Test
	public void getEnumValueByValue() {
		Assert.assertEquals("Months", refDataDAO.getEnumValue("DurationUnit", 1).some().getName());
	}
	
	@Test
	public void getEnumValueByName() {
		Assert.assertEquals(true, refDataDAO.getEnumValue("DurationUnit", "X").isNone());
	}
}
