package com.jdc.themis.dealer.data.dao.hibernate;

import java.math.BigDecimal;

import javax.time.calendar.LocalDate;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.jdc.themis.dealer.data.dao.IncomeJournalDAO;
import com.jdc.themis.dealer.domain.DealerEntryItemStatus;
import com.jdc.themis.dealer.domain.GeneralJournal;
import com.jdc.themis.dealer.domain.SalesServiceJournal;
import com.jdc.themis.dealer.domain.TaxJournal;
import com.jdc.themis.dealer.domain.VehicleSalesJournal;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
    "classpath:test-database-config.xml" })
@Transactional
public class TestIncomeJournalDAOImpl {
	@Autowired
	private IncomeJournalDAO incomeJournalDAL;

	@Test
	public void getOneExistingTaxJournal() {
		int hasJournal = 0;
		for (final TaxJournal journal : incomeJournalDAL.getTaxJournal(2, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test0", journal.getUpdatedBy());
		}
		Assert.assertEquals(1, hasJournal);
	} 
	
	@Test
	public void getNonExistingTaxJournal() {
		int hasJournal = 0;
		for (final TaxJournal journal : incomeJournalDAL.getTaxJournal(2, LocalDate.of(2013, 8, 2))) {
			hasJournal++;
			System.err.println(journal);
		}
		Assert.assertEquals(0, hasJournal);
	} 
	
	@Test
	public void insertOneTaxJournalForExistingDealer() {
		final TaxJournal taxJournal = new TaxJournal();
		taxJournal.setAmount(new BigDecimal("2234.01"));
		taxJournal.setDealerID(2);
		taxJournal.setId(1);
		taxJournal.setValidDate(LocalDate.of(2013, 8, 1));
		taxJournal.setUpdatedBy("test");
		incomeJournalDAL.saveTaxJournal(2, Lists.newArrayList(taxJournal));
		
		int hasJournal = 0;
		for (final TaxJournal journal : incomeJournalDAL.getTaxJournal(2, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test", journal.getUpdatedBy());
			Assert.assertEquals(new BigDecimal("2234.01"), journal.getAmount());
		}
		Assert.assertEquals(1, hasJournal);
	} 
	
	@Test
	public void insertOneTaxJournal() {
		final TaxJournal taxJournal = new TaxJournal();
		taxJournal.setAmount(new BigDecimal("1234.01"));
		taxJournal.setDealerID(1);
		taxJournal.setId(1);
		taxJournal.setValidDate(LocalDate.of(2013, 8, 1));
		taxJournal.setUpdatedBy("test");
		incomeJournalDAL.saveTaxJournal(1, Lists.newArrayList(taxJournal));
		
		int hasJournal = 0;
		for (final TaxJournal journal : incomeJournalDAL.getTaxJournal(1, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.out.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test", journal.getUpdatedBy());
		}
		Assert.assertEquals(1, hasJournal);
	} 
	
	@Test
	public void insertTwoTaxJournal() {
		final TaxJournal taxJournal1 = new TaxJournal();
		taxJournal1.setAmount(new BigDecimal("1235.01"));
		taxJournal1.setDealerID(1);
		taxJournal1.setId(1);
		taxJournal1.setValidDate(LocalDate.of(2013, 8, 1));
		taxJournal1.setUpdatedBy("test1");
		incomeJournalDAL.saveTaxJournal(1, Lists.newArrayList(taxJournal1));
		
		int hasJournal = 0;
		for (final TaxJournal journal : incomeJournalDAL.getTaxJournal(1, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.out.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test1", journal.getUpdatedBy());
		}
		Assert.assertEquals(1, hasJournal);
		
		final TaxJournal taxJournal2 = new TaxJournal();
		taxJournal2.setAmount(new BigDecimal("1235.01"));
		taxJournal2.setDealerID(1);
		taxJournal2.setId(1);
		taxJournal2.setValidDate(LocalDate.of(2013, 8, 1));
		taxJournal2.setUpdatedBy("test2");
		incomeJournalDAL.saveTaxJournal(1, Lists.newArrayList(taxJournal2));
		
		hasJournal = 0;
		for (final TaxJournal journal : incomeJournalDAL.getTaxJournal(1, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test2", journal.getUpdatedBy());
			Assert.assertEquals(new BigDecimal("1235.01"), journal.getAmount());
		}
		Assert.assertEquals(1, hasJournal);
	} 
	
	@Test
	public void insertOneDealerEntryItemStatus() {
		final DealerEntryItemStatus status = new DealerEntryItemStatus();
		status.setDealerID(1);
		status.setEntryItemID(1);
		status.setValidDate(LocalDate.of(2013, 8, 1));
		status.setUpdateBy("test");
		incomeJournalDAL.saveDealerEntryItemStatus(1, Lists.newArrayList(status));
		
		int hasJournal = 0;
		for (final DealerEntryItemStatus journal : incomeJournalDAL.getDealerEntryItemStatus(1, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.out.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test", journal.getUpdateBy());
			Assert.assertEquals(1, journal.getDealerID().intValue());
		}
		Assert.assertEquals(1, hasJournal);
	} 
	
	@Test
	public void insertTwoDealerEntryItemStatus() {
		final DealerEntryItemStatus status1 = new DealerEntryItemStatus();
		status1.setDealerID(1);
		status1.setEntryItemID(1);
		status1.setValidDate(LocalDate.of(2013, 8, 1));
		status1.setUpdateBy("test2");
		
		final DealerEntryItemStatus status2 = new DealerEntryItemStatus();
		status2.setDealerID(1);
		status2.setEntryItemID(2);
		status2.setValidDate(LocalDate.of(2013, 8, 1));
		status2.setUpdateBy("test2");
		incomeJournalDAL.saveDealerEntryItemStatus(1, Lists.newArrayList(status1, status2));
		
		int hasJournal = 0;
		for (final DealerEntryItemStatus journal : incomeJournalDAL.getDealerEntryItemStatus(1, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test2", journal.getUpdateBy());
			Assert.assertEquals(1, journal.getDealerID().intValue());
		}
		Assert.assertEquals(2, hasJournal);
	} 
	
	@Test
	public void insertOneVehicleSalesJournal() {
		final VehicleSalesJournal status = new VehicleSalesJournal();
		status.setDealerID(10);
		status.setId(1);
		status.setAmount(new BigDecimal("1234.343"));
		status.setMargin(new BigDecimal("2234.343"));
		status.setCount(12);
		status.setDepartmentID(1);
		status.setValidDate(LocalDate.of(2013, 8, 1));
		status.setUpdatedBy("test");
		incomeJournalDAL.saveVehicleSalesJournal(10, 1, Lists.newArrayList(status));
		
		int hasJournal = 0;
		for (final VehicleSalesJournal journal : incomeJournalDAL.getVehicleSalesJournal(10, 1, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.out.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test", journal.getUpdatedBy());
			Assert.assertEquals(10, journal.getDealerID().intValue());
		} 
		Assert.assertEquals(1, hasJournal);
	} 
	
	@Test
	public void insertTwoVehicleSalesJournal() {
		final VehicleSalesJournal status = new VehicleSalesJournal();
		status.setDealerID(10);
		status.setId(1);
		status.setAmount(new BigDecimal("1234.343"));
		status.setMargin(new BigDecimal("2234.343"));
		status.setCount(12);
		status.setDepartmentID(1);
		status.setValidDate(LocalDate.of(2013, 8, 1));
		status.setUpdatedBy("test");
		incomeJournalDAL.saveVehicleSalesJournal(10, 1, Lists.newArrayList(status));
		
		final VehicleSalesJournal status2 = new VehicleSalesJournal();
		status2.setDealerID(10);
		status2.setId(1);
		status2.setAmount(new BigDecimal("5234.343"));
		status2.setMargin(new BigDecimal("2234.343"));
		status2.setCount(1123432);
		status2.setDepartmentID(1);
		status2.setValidDate(LocalDate.of(2013, 8, 1));
		status2.setUpdatedBy("test2");
		incomeJournalDAL.saveVehicleSalesJournal(10, 1, Lists.newArrayList(status2));
		
		int hasJournal = 0;
		for (final VehicleSalesJournal journal : incomeJournalDAL.getVehicleSalesJournal(10, 1, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test2", journal.getUpdatedBy());
			Assert.assertEquals(10, journal.getDealerID().intValue());
			Assert.assertEquals(1123432, journal.getCount().intValue());
		}
		Assert.assertEquals(1, hasJournal);
	} 
	
	@Test
	public void insertOneSalesServiceJournal() {
		final SalesServiceJournal status = new SalesServiceJournal();
		status.setDealerID(10);
		status.setId(1);
		status.setAmount(new BigDecimal("1234.343"));
		status.setMargin(new BigDecimal("2234.343"));
		status.setCount(12);
		status.setDepartmentID(1);
		status.setValidDate(LocalDate.of(2013, 8, 1));
		status.setUpdatedBy("test");
		incomeJournalDAL.saveSalesServiceJournal(10, 1, Lists.newArrayList(status));
		
		int hasJournal = 0;
		for (final SalesServiceJournal journal : incomeJournalDAL.getSalesServiceJournal(10, 1, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.out.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test", journal.getUpdatedBy());
			Assert.assertEquals(10, journal.getDealerID().intValue());
		} 
		Assert.assertEquals(1, hasJournal);
	} 
	
	@Test
	public void insertTwoSalesServiceJournal() {
		final SalesServiceJournal status = new SalesServiceJournal();
		status.setDealerID(10);
		status.setId(1);
		status.setAmount(new BigDecimal("1234.343"));
		status.setMargin(new BigDecimal("2234.343"));
		status.setCount(12);
		status.setDepartmentID(1);
		status.setValidDate(LocalDate.of(2013, 8, 1));
		status.setUpdatedBy("test");
		incomeJournalDAL.saveSalesServiceJournal(10, 1, Lists.newArrayList(status));
		
		final SalesServiceJournal status2 = new SalesServiceJournal();
		status2.setDealerID(10);
		status2.setId(1);
		status2.setAmount(new BigDecimal("5234.343"));
		status2.setMargin(new BigDecimal("2234.343"));
		status2.setCount(1123432);
		status2.setDepartmentID(1);
		status2.setValidDate(LocalDate.of(2013, 8, 1));
		status2.setUpdatedBy("test2");
		incomeJournalDAL.saveSalesServiceJournal(10, 1, Lists.newArrayList(status2));
		
		int hasJournal = 0;
		for (final SalesServiceJournal journal : incomeJournalDAL.getSalesServiceJournal(10, 1, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test2", journal.getUpdatedBy());
			Assert.assertEquals(10, journal.getDealerID().intValue());
			Assert.assertEquals(1123432, journal.getCount().intValue());
		}
		Assert.assertEquals(1, hasJournal);
	} 
	
	@Test
	public void insertOneGeneralJournal() {
		final GeneralJournal status = new GeneralJournal();
		status.setDealerID(10);
		status.setId(1);
		status.setAmount(new BigDecimal("1234.343"));
		status.setDepartmentID(1);
		status.setValidDate(LocalDate.of(2013, 8, 1));
		status.setUpdatedBy("test");
		incomeJournalDAL.saveGeneralJournal(10, 1, Lists.newArrayList(status));
		
		int hasJournal = 0;
		for (final GeneralJournal journal : incomeJournalDAL.getGeneralJournal(10, 1, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.out.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test", journal.getUpdatedBy());
			Assert.assertEquals(10, journal.getDealerID().intValue());
		} 
		Assert.assertEquals(1, hasJournal);
	} 
	
	@Test
	public void insertTwoGeneralJournal() {
		final GeneralJournal status = new GeneralJournal();
		status.setDealerID(10);
		status.setId(1);
		status.setAmount(new BigDecimal("1234.343"));
		status.setDepartmentID(1);
		status.setValidDate(LocalDate.of(2013, 8, 1));
		status.setUpdatedBy("test");
		incomeJournalDAL.saveGeneralJournal(10, 1, Lists.newArrayList(status));
		
		final GeneralJournal status2 = new GeneralJournal();
		status2.setDealerID(10);
		status2.setId(1);
		status2.setAmount(new BigDecimal("5234.343"));
		status2.setDepartmentID(1);
		status2.setValidDate(LocalDate.of(2013, 8, 1));
		status2.setUpdatedBy("test2");
		incomeJournalDAL.saveGeneralJournal(10, 1, Lists.newArrayList(status2));
		
		int hasJournal = 0;
		for (final GeneralJournal journal : incomeJournalDAL.getGeneralJournal(10, 1, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test2", journal.getUpdatedBy());
			Assert.assertEquals(10, journal.getDealerID().intValue());
		}
		Assert.assertEquals(1, hasJournal);
	} 
	
	@Test
	public void getOneExistingGeneralJournal() {
		int hasJournal = 0;
		for (final GeneralJournal journal : incomeJournalDAL.getGeneralJournal(2, 4, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test2", journal.getUpdatedBy());
			Assert.assertEquals(2, journal.getId().intValue());
		}
		Assert.assertEquals(1, hasJournal);
	} 
	
}
