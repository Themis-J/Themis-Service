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
import com.jdc.themis.dealer.domain.AccountReceivableDuration;
import com.jdc.themis.dealer.domain.DealerEntryItemStatus;
import com.jdc.themis.dealer.domain.EmployeeFee;
import com.jdc.themis.dealer.domain.EmployeeFeeSummary;
import com.jdc.themis.dealer.domain.GeneralJournal;
import com.jdc.themis.dealer.domain.HumanResourceAllocation;
import com.jdc.themis.dealer.domain.InventoryDuration;
import com.jdc.themis.dealer.domain.SalesServiceJournal;
import com.jdc.themis.dealer.domain.TaxJournal;
import com.jdc.themis.dealer.domain.VehicleSalesJournal;
import com.jdc.themis.dealer.utils.Utils;


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
			Assert.assertEquals(0, journal.getVersion().intValue());
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
		System.err.println(taxJournal1);
		Assert.assertEquals(0, taxJournal1.getVersion().intValue());
		
		int hasJournal = 0;
		for (final TaxJournal journal : incomeJournalDAL.getTaxJournal(1, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.out.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test1", journal.getUpdatedBy());
		}
		Assert.assertEquals(1, hasJournal);
		
		final TaxJournal taxJournal2 = new TaxJournal();
		taxJournal2.setAmount(new BigDecimal("2235.01"));
		taxJournal2.setDealerID(1);
		taxJournal2.setId(1);
		taxJournal2.setValidDate(LocalDate.of(2013, 8, 1));
		taxJournal2.setUpdatedBy("test2");
		incomeJournalDAL.saveTaxJournal(1, Lists.newArrayList(taxJournal2));
		System.err.println(taxJournal1);
		
		hasJournal = 0;
		for (final TaxJournal journal : incomeJournalDAL.getTaxJournal(1, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test2", journal.getUpdatedBy());
			Assert.assertEquals(0, journal.getVersion().intValue());
			Assert.assertEquals(new BigDecimal("2235.01"), journal.getAmount());
		}
		
		Assert.assertEquals(1, taxJournal1.getVersion().intValue());
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
	public void insertTwoDealerEntryItemStatus2() {
		final DealerEntryItemStatus status1 = new DealerEntryItemStatus();
		status1.setDealerID(1);
		status1.setEntryItemID(1);
		status1.setValidDate(LocalDate.of(2013, 8, 1));
		status1.setUpdateBy("test2");
		incomeJournalDAL.saveDealerEntryItemStatus(1, Lists.newArrayList(status1));
		
		final DealerEntryItemStatus status2 = new DealerEntryItemStatus();
		status2.setDealerID(1);
		status2.setEntryItemID(1);
		status2.setValidDate(LocalDate.of(2013, 8, 1));
		status2.setUpdateBy("test2");
		incomeJournalDAL.saveDealerEntryItemStatus(1, Lists.newArrayList(status2));
		
		int hasJournal = 0;
		for (final DealerEntryItemStatus journal : incomeJournalDAL.getDealerEntryItemStatus(1, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test2", journal.getUpdateBy());
			Assert.assertEquals(1, journal.getDealerID().intValue());
		}
		Assert.assertEquals(1, hasJournal);
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
	public void insertOneVehicleSalesJournalWithNullAmount() {
		final VehicleSalesJournal status = new VehicleSalesJournal();
		status.setDealerID(10);
		status.setId(1);
		status.setAmount(null);
		status.setMargin(null);
		status.setCount(null);
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
			Assert.assertEquals(BigDecimal.ZERO, journal.getAmount());
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
	public void getVehicleSalesJournal() {
		final VehicleSalesJournal status = new VehicleSalesJournal();
		status.setDealerID(10);
		status.setId(2);
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
		for (final VehicleSalesJournal journal : incomeJournalDAL.getVehicleSalesJournal(LocalDate.of(2013, 8, 1), Utils.currentTimestamp())) {
			hasJournal++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
		}
		Assert.assertEquals(2, hasJournal);
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
		
		hasJournal = 0;
		for (final SalesServiceJournal journal : incomeJournalDAL.getSalesServiceJournal(LocalDate.of(2013, 8, 1), Utils.currentTimestamp())) {
			hasJournal++;
			System.out.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test", journal.getUpdatedBy());
			Assert.assertEquals(10, journal.getDealerID().intValue());
		} 
		Assert.assertEquals(1, hasJournal);
	} 
	
	@Test
	public void insertOneSalesServiceJournalWithNullAmount() {
		final SalesServiceJournal status = new SalesServiceJournal();
		status.setDealerID(10);
		status.setId(1);
		status.setAmount(null);
		status.setMargin(null);
		status.setCount(null);
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
			Assert.assertEquals(BigDecimal.ZERO, journal.getAmount());
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
		
		hasJournal = 0;
		for (final GeneralJournal journal : incomeJournalDAL.getGeneralJournal(LocalDate.of(2013, 8, 1), Utils.currentTimestamp())) {
			hasJournal++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
		}
		Assert.assertEquals(2, hasJournal);
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
	
	@Test
	public void insertOneAcctReceivableDuration() {
		final AccountReceivableDuration status = new AccountReceivableDuration();
		status.setDealerID(10);
		status.setId(1);
		status.setAmount(new BigDecimal("1234.343"));
		status.setDurationID(1);
		status.setValidDate(LocalDate.of(2013, 8, 1));
		status.setUpdatedBy("test");
		incomeJournalDAL.saveAccountReceivableDuration(10, Lists.newArrayList(status));
		
		int hasJournal = 0;
		for (final AccountReceivableDuration journal : incomeJournalDAL.getAccountReceivableDuration(10, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.out.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test", journal.getUpdatedBy());
			Assert.assertEquals(1, journal.getDurationID().intValue());
			Assert.assertEquals(10, journal.getDealerID().intValue());
		} 
		Assert.assertEquals(1, hasJournal);
	} 
	
	@Test
	public void insertTwoAcctReceivableDuration() {
		final AccountReceivableDuration status = new AccountReceivableDuration();
		status.setDealerID(10);
		status.setId(1);
		status.setAmount(new BigDecimal("1234.343"));
		status.setDurationID(1);
		status.setValidDate(LocalDate.of(2013, 8, 1));
		status.setUpdatedBy("test");
		incomeJournalDAL.saveAccountReceivableDuration(10, Lists.newArrayList(status));
		
		final AccountReceivableDuration status2 = new AccountReceivableDuration();
		status2.setDealerID(10);
		status2.setId(1);
		status2.setAmount(new BigDecimal("5234.343"));
		status2.setDurationID(1);
		status2.setValidDate(LocalDate.of(2013, 8, 1));
		status2.setUpdatedBy("test2");
		incomeJournalDAL.saveAccountReceivableDuration(10, Lists.newArrayList(status2));
		
		int hasJournal = 0;
		for (final AccountReceivableDuration journal : incomeJournalDAL.getAccountReceivableDuration(10, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test2", journal.getUpdatedBy());
			Assert.assertEquals(10, journal.getDealerID().intValue());
		}
		Assert.assertEquals(1, hasJournal);
	} 
	
	@Test
	public void getOneExistingAcctReceivableDuration() {
		int hasJournal = 0;
		for (final AccountReceivableDuration journal : incomeJournalDAL.getAccountReceivableDuration(2, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test2", journal.getUpdatedBy());
			Assert.assertEquals(2, journal.getId().intValue());
		}
		Assert.assertEquals(1, hasJournal);
	} 
	
	@Test
	public void insertOneEmployeeFee() {
		final EmployeeFee status = new EmployeeFee();
		status.setDealerID(10);
		status.setId(1);
		status.setFeeTypeID(2);
		status.setAmount(new BigDecimal("1234.343"));
		status.setDepartmentID(1);
		status.setValidDate(LocalDate.of(2013, 8, 1));
		status.setUpdatedBy("test");
		incomeJournalDAL.saveEmployeeFee(10, 1, Lists.newArrayList(status));
		
		int hasJournal = 0;
		for (final EmployeeFee journal : incomeJournalDAL.getEmployeeFee(10, 1, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.out.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test", journal.getUpdatedBy());
			Assert.assertEquals(10, journal.getDealerID().intValue());
		} 
		Assert.assertEquals(1, hasJournal);
	} 
	
	@Test
	public void insertTwoEmployeeFee() {
		final EmployeeFee status = new EmployeeFee();
		status.setDealerID(10);
		status.setId(1);
		status.setFeeTypeID(2);
		status.setAmount(new BigDecimal("1234.343"));
		status.setDepartmentID(1);
		status.setValidDate(LocalDate.of(2013, 8, 1));
		status.setUpdatedBy("test");
		incomeJournalDAL.saveEmployeeFee(10, 1, Lists.newArrayList(status));
		
		final EmployeeFee status2 = new EmployeeFee();
		status2.setDealerID(10);
		status2.setId(1);
		status2.setFeeTypeID(2);
		status2.setAmount(new BigDecimal("5234.343"));
		status2.setDepartmentID(1);
		status2.setValidDate(LocalDate.of(2013, 8, 1));
		status2.setUpdatedBy("test2");
		incomeJournalDAL.saveEmployeeFee(10, 1, Lists.newArrayList(status2));
		
		int hasJournal = 0;
		for (final EmployeeFee journal : incomeJournalDAL.getEmployeeFee(10, 1, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test2", journal.getUpdatedBy());
			Assert.assertEquals(10, journal.getDealerID().intValue());
		}
		Assert.assertEquals(1, hasJournal);
	} 
	
	@Test
	public void insertOneEmployeeFeeSummary() {
		final EmployeeFeeSummary status = new EmployeeFeeSummary();
		status.setDealerID(10);
		status.setId(1);
		status.setAmount(new BigDecimal("1234.343"));
		status.setDepartmentID(1);
		status.setValidDate(LocalDate.of(2013, 8, 1));
		status.setUpdatedBy("test");
		incomeJournalDAL.saveEmployeeFeeSummary(10, 1, Lists.newArrayList(status));
		
		int hasJournal = 0;
		for (final EmployeeFeeSummary journal : incomeJournalDAL.getEmployeeFeeSummary(10, 1, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.out.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test", journal.getUpdatedBy());
			Assert.assertEquals(10, journal.getDealerID().intValue());
		} 
		Assert.assertEquals(1, hasJournal);
	} 
	
	@Test
	public void insertTwoEmployeeFeeSummary() {
		final EmployeeFeeSummary status = new EmployeeFeeSummary();
		status.setDealerID(10);
		status.setId(1);
		status.setAmount(new BigDecimal("1234.343"));
		status.setDepartmentID(1);
		status.setValidDate(LocalDate.of(2013, 8, 1));
		status.setUpdatedBy("test");
		incomeJournalDAL.saveEmployeeFeeSummary(10, 1, Lists.newArrayList(status));
		
		final EmployeeFeeSummary status2 = new EmployeeFeeSummary();
		status2.setDealerID(10);
		status2.setId(1);
		status2.setAmount(new BigDecimal("5234.343"));
		status2.setDepartmentID(1);
		status2.setValidDate(LocalDate.of(2013, 8, 1));
		status2.setUpdatedBy("test2");
		incomeJournalDAL.saveEmployeeFeeSummary(10, 1, Lists.newArrayList(status2));
		
		int hasJournal = 0;
		for (final EmployeeFeeSummary journal : incomeJournalDAL.getEmployeeFeeSummary(10, 1, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test2", journal.getUpdatedBy());
			Assert.assertEquals(10, journal.getDealerID().intValue());
		}
		Assert.assertEquals(1, hasJournal);
	} 
	
	@Test
	public void insertOneInventoryDuration() {
		final InventoryDuration status = new InventoryDuration();
		status.setDealerID(10);
		status.setId(1);
		status.setDepartmentID(2);
		status.setAmount(new BigDecimal("1234.343"));
		status.setDurationID(1);
		status.setValidDate(LocalDate.of(2013, 8, 1));
		status.setUpdatedBy("test");
		incomeJournalDAL.saveInventoryDuration(10, 2, Lists.newArrayList(status));
		
		int hasJournal = 0;
		for (final InventoryDuration journal : incomeJournalDAL.getInventoryDuration(10, 2, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.out.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test", journal.getUpdatedBy());
			Assert.assertEquals(1, journal.getDurationID().intValue());
			Assert.assertEquals(10, journal.getDealerID().intValue());
		} 
		Assert.assertEquals(1, hasJournal);
	} 
	
	@Test
	public void insertTwoInventoryDuration() {
		final InventoryDuration status = new InventoryDuration();
		status.setDealerID(10);
		status.setId(1);
		status.setDepartmentID(2);
		status.setAmount(new BigDecimal("1234.343"));
		status.setDurationID(1);
		status.setValidDate(LocalDate.of(2013, 8, 1));
		status.setUpdatedBy("test");
		incomeJournalDAL.saveInventoryDuration(10, 2, Lists.newArrayList(status));
		
		final InventoryDuration status2 = new InventoryDuration();
		status2.setDealerID(10);
		status2.setId(1);
		status2.setDepartmentID(2);
		status2.setAmount(new BigDecimal("5234.343"));
		status2.setDurationID(1);
		status2.setValidDate(LocalDate.of(2013, 8, 1));
		status2.setUpdatedBy("test2");
		incomeJournalDAL.saveInventoryDuration(10, 2, Lists.newArrayList(status2));
		
		int hasJournal = 0;
		for (final InventoryDuration journal : incomeJournalDAL.getInventoryDuration(10, 2, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test2", journal.getUpdatedBy());
			Assert.assertEquals(10, journal.getDealerID().intValue());
		}
		Assert.assertEquals(1, hasJournal);
	} 
	
	@Test
	public void insertTwoInventoryDurationWithZeroAmount() {
		final InventoryDuration status = new InventoryDuration();
		status.setDealerID(10);
		status.setId(1);
		status.setDepartmentID(2);
		status.setAmount(new BigDecimal("1234.343"));
		status.setDurationID(1);
		status.setValidDate(LocalDate.of(2013, 8, 1));
		status.setUpdatedBy("test");
		incomeJournalDAL.saveInventoryDuration(10, 2, Lists.newArrayList(status));
		
		final InventoryDuration status2 = new InventoryDuration();
		status2.setDealerID(10);
		status2.setId(1);
		status2.setDepartmentID(2);
		status2.setAmount(null);
		status2.setDurationID(1);
		status2.setValidDate(LocalDate.of(2013, 8, 1));
		status2.setUpdatedBy("test2");
		incomeJournalDAL.saveInventoryDuration(10, 2, Lists.newArrayList(status2));
		
		int hasJournal = 0;
		for (final InventoryDuration journal : incomeJournalDAL.getInventoryDuration(10, 2, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test2", journal.getUpdatedBy());
			Assert.assertEquals(10, journal.getDealerID().intValue());
		}
		Assert.assertEquals(1, hasJournal);
	} 
	
	@Test
	public void insertOneHRAllocation() {
		final HumanResourceAllocation status = new HumanResourceAllocation();
		status.setDealerID(10);
		status.setId(1);
		status.setDepartmentID(2);
		status.setAllocation(new BigDecimal("1234.343"));

		status.setValidDate(LocalDate.of(2013, 8, 1));
		status.setUpdatedBy("test");
		incomeJournalDAL.saveHumanResourceAllocation(10, 2, Lists.newArrayList(status));
		
		int hasJournal = 0;
		for (final HumanResourceAllocation journal : incomeJournalDAL.getHumanResourceAllocation(10, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.out.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test", journal.getUpdatedBy());
			Assert.assertEquals(10, journal.getDealerID().intValue());
		} 
		Assert.assertEquals(1, hasJournal);
	} 
	
	@Test
	public void insertTwoHRAllocation() {
		final HumanResourceAllocation status = new HumanResourceAllocation();
		status.setDealerID(10);
		status.setId(1);
		status.setDepartmentID(2);
		status.setAllocation(new BigDecimal("1234.343"));
		status.setValidDate(LocalDate.of(2013, 8, 1));
		status.setUpdatedBy("test");
		incomeJournalDAL.saveHumanResourceAllocation(10, 2, Lists.newArrayList(status));
		
		final HumanResourceAllocation status2 = new HumanResourceAllocation();
		status2.setDealerID(10);
		status2.setId(1);
		status2.setDepartmentID(2);
		status2.setAllocation(new BigDecimal("2222"));
		status2.setValidDate(LocalDate.of(2013, 8, 1));
		status2.setUpdatedBy("test2");
		incomeJournalDAL.saveHumanResourceAllocation(10, 2, Lists.newArrayList(status2));
		
		int hasJournal = 0;
		for (final HumanResourceAllocation journal : incomeJournalDAL.getHumanResourceAllocation(10, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.out.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test2", journal.getUpdatedBy());
			Assert.assertEquals(10, journal.getDealerID().intValue());
		} 
		Assert.assertEquals(1, hasJournal);
	} 
}
