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
    "classpath:/META-INF/test-database-config.xml" })
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
	public void getGeneralJournalItemList() {
		Assert.assertEquals(2, refDataDAO.getGeneralJournalItems().size());
	}
	
	@Test
	public void getAccountReceivableDurationItemList() {
		Assert.assertEquals(2, refDataDAO.getAccountReceivableDurationItems().size());
	}
	
	@Test
	public void getInventoryDurationItemList() {
		Assert.assertEquals(3, refDataDAO.getInventoryDurationItems().size());
	}
	
	@Test
	public void getEmployeeFeeItemList() {
		Assert.assertEquals(1, refDataDAO.getEmployeeFeeItems().size());
	}
	
	@Test
	public void getEmployeeFeeSummaryItemList() {
		Assert.assertEquals(2, refDataDAO.getEmployeeFeeSummaryItems().size());
	}
}
