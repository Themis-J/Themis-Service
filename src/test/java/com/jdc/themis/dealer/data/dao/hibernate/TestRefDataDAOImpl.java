package com.jdc.themis.dealer.data.dao.hibernate;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.jdc.themis.dealer.data.dao.RefDataDAO;

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
	public void getSalesServiceJournalItemList() {
		Assert.assertEquals(2, refDataDAO.getSalesServiceJournalItems().size());
	}
	
	@Test
	public void getSalesServiceJournalItem() {
		Assert.assertEquals("SalesServiceJournalItem1", refDataDAO.getSalesServiceJournalItem(1).getName());
	}
	
	@Test
	public void getGeneralJournalItemList() {
		Assert.assertEquals(2, refDataDAO.getGeneralJournalItems().size());
	}
	
	@Test
	public void getGeneralJournalItem() {
		Assert.assertEquals("GeneralJournalItem1", refDataDAO.getGeneralJournalItem(1).getName());
	}
	
	@Test
	public void getAccountReceivableDurationItemList() {
		Assert.assertEquals(2, refDataDAO.getAccountReceivableDurationItems().size());
	}
	
	@Test
	public void getAccountReceivableDurationItem() {
		Assert.assertEquals("AccountReceivableDurationItem2", refDataDAO.getAccountReceivableDurationItem(2).getName());
	}
	
	@Test
	public void getInventoryDurationItemList() {
		Assert.assertEquals(3, refDataDAO.getInventoryDurationItems().size());
	}
	
	@Test
	public void getInventoryDurationItem() {
		Assert.assertEquals("InventoryDurationItem2", refDataDAO.getInventoryDurationItem(2).getName());
	}
	
	@Test
	public void getEmployeeFeeItemList() {
		Assert.assertEquals(1, refDataDAO.getEmployeeFeeItems().size());
	}
	
	@Test
	public void getEmployeeFeeItem() {
		Assert.assertEquals("EmployeeFeeItem1", refDataDAO.getEmployeeFeeItem(1).getName());
	}
	
	@Test
	public void getEmployeeFeeSummaryItemList() {
		Assert.assertEquals(2, refDataDAO.getEmployeeFeeSummaryItems().size());
	}
	
	@Test
	public void getEmployeeFeeSummaryItem() {
		Assert.assertEquals("EmployeeFeeSummaryItem1", refDataDAO.getEmployeeFeeSummaryItem(1).getName());
	}
	
	@Test
	public void getDepartmentList() {
		Assert.assertEquals(4, refDataDAO.getDepartments().size());
	}
	
	@Test
	public void getDepartment() {
		Assert.assertEquals("Department1", refDataDAO.getDepartment(1).getName());
	}
}
