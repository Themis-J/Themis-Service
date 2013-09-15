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
import com.jdc.themis.dealer.data.dao.ReportDAO;
import com.jdc.themis.dealer.domain.DealerIncomeExpenseFact;
import com.jdc.themis.dealer.domain.DealerIncomeRevenueFact;
import com.jdc.themis.dealer.domain.GeneralJournal;
import com.jdc.themis.dealer.domain.ReportItem;
import com.jdc.themis.dealer.domain.ReportTime;
import com.jdc.themis.dealer.domain.SalesServiceJournal;
import com.jdc.themis.dealer.domain.VehicleSalesJournal;

import fj.data.Option;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
    "classpath:test-database-config.xml" })
@Transactional
public class TestReportDAOImpl {
	@Autowired
	private ReportDAO reportDAL;
	@Autowired
	private IncomeJournalDAO incomeJournalDAL;

	@Test
	public void importVehicleSalesJournal() {
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
		status2.setDealerID(11);
		status2.setId(1);
		status2.setAmount(new BigDecimal("5234.343"));
		status2.setMargin(new BigDecimal("2234.343"));
		status2.setCount(1123432);
		status2.setDepartmentID(3);
		status2.setValidDate(LocalDate.of(2013, 8, 1));
		status2.setUpdatedBy("test2");
		incomeJournalDAL.saveVehicleSalesJournal(11, 3, Lists.newArrayList(status2));
		
		final VehicleSalesJournal status3 = new VehicleSalesJournal();
		status3.setDealerID(10);
		status3.setId(2);
		status3.setAmount(new BigDecimal("335"));
		status3.setMargin(new BigDecimal("22"));
		status3.setCount(12);
		status3.setDepartmentID(3);
		status3.setValidDate(LocalDate.of(2013, 7, 1));
		status3.setUpdatedBy("test");
		incomeJournalDAL.saveVehicleSalesJournal(10, 3, Lists.newArrayList(status3));
		
		reportDAL.importVehicleSalesJournal(LocalDate.of(2013, 8, 1));
		reportDAL.importVehicleSalesJournal(LocalDate.of(2013, 7, 1));
		// force to populate twice
		reportDAL.importVehicleSalesJournal(LocalDate.of(2013, 8, 1));
		reportDAL.importVehicleSalesJournal(LocalDate.of(2013, 7, 1));
		
		int hasJournal = 0;
		for (final DealerIncomeRevenueFact journal : 
			reportDAL.getDealerIncomeRevenueFacts(2013, 
					Lists.newArrayList(new Integer[]{8}), 
					Lists.newArrayList(new Integer[]{}), 
					Lists.newArrayList(new Integer[]{}), 
					Lists.newArrayList(new String[]{}), 
					Lists.newArrayList(new Integer[]{}))) {
			hasJournal++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
		} 
		Assert.assertEquals(2, hasJournal);
		
		hasJournal = 0;
		for (final DealerIncomeRevenueFact journal : 
			reportDAL.getDealerIncomeRevenueFacts(2013, 
					Lists.newArrayList(new Integer[]{7}), 
					Lists.newArrayList(new Integer[]{}), 
					Lists.newArrayList(new Integer[]{}), 
					Lists.newArrayList(new String[]{}), 
					Lists.newArrayList(new Integer[]{}))) {
			hasJournal++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
		} 
		Assert.assertEquals(1, hasJournal);
		
		int hasReportItem = 0;
		for (final ReportItem journal : 
			reportDAL.getAllReportItem()) {
			hasReportItem++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
		} 
		Assert.assertEquals(2, hasReportItem);
		
		int hasReportTime = 0;
		for (final ReportTime journal : 
			reportDAL.getAllReportTime()) {
			hasReportTime++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
		} 
		Assert.assertEquals(2, hasReportTime);
	} 
	
	@Test
	public void importSalesServiceJournal() {
		final SalesServiceJournal status4 = new SalesServiceJournal();
		status4.setDealerID(10);
		status4.setId(1);
		status4.setAmount(new BigDecimal("1234.343"));
		status4.setMargin(new BigDecimal("2234.343"));
		status4.setCount(12);
		status4.setDepartmentID(1);
		status4.setValidDate(LocalDate.of(2013, 8, 1));
		status4.setUpdatedBy("test");
		incomeJournalDAL.saveSalesServiceJournal(10, 1, Lists.newArrayList(status4));
		
		final SalesServiceJournal status5 = new SalesServiceJournal();
		status5.setDealerID(11);
		status5.setId(1);
		status5.setAmount(new BigDecimal("5234.343"));
		status5.setMargin(new BigDecimal("2234.343"));
		status5.setCount(1123432);
		status5.setDepartmentID(3);
		status5.setValidDate(LocalDate.of(2013, 8, 1));
		status5.setUpdatedBy("test2");
		incomeJournalDAL.saveSalesServiceJournal(11, 3, Lists.newArrayList(status5));
		
		final SalesServiceJournal status6 = new SalesServiceJournal();
		status6.setDealerID(10);
		status6.setId(2);
		status6.setAmount(new BigDecimal("335"));
		status6.setMargin(new BigDecimal("22"));
		status6.setCount(12);
		status6.setDepartmentID(3);
		status6.setValidDate(LocalDate.of(2013, 7, 1));
		status6.setUpdatedBy("test");
		incomeJournalDAL.saveSalesServiceJournal(10, 3, Lists.newArrayList(status6));
		
		final SalesServiceJournal status7 = new SalesServiceJournal();
		status7.setDealerID(10);
		status7.setId(3); // this is a expense item
		status7.setAmount(BigDecimal.ZERO);
		status7.setMargin(new BigDecimal("335"));
		status7.setCount(0);
		status7.setDepartmentID(3);
		status7.setValidDate(LocalDate.of(2013, 7, 1));
		status7.setUpdatedBy("test");
		incomeJournalDAL.saveSalesServiceJournal(10, 3, Lists.newArrayList(status7));
		
		reportDAL.importSalesServiceJournal(LocalDate.of(2013, 8, 1));
		reportDAL.importSalesServiceJournal(LocalDate.of(2013, 7, 1));
		// force to populate twice
		reportDAL.importSalesServiceJournal(LocalDate.of(2013, 8, 1));
		reportDAL.importSalesServiceJournal(LocalDate.of(2013, 7, 1));
		
		int hasJournal = 0;
		for (final DealerIncomeRevenueFact journal : 
			reportDAL.getDealerIncomeRevenueFacts(2013, 
					Lists.newArrayList(new Integer[]{8}), 
					Lists.newArrayList(new Integer[]{}), 
					Lists.newArrayList(new Integer[]{}), 
					Lists.newArrayList(new String[]{}), 
					Lists.newArrayList(new Integer[]{}))) {
			hasJournal++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
		} 
		Assert.assertEquals(2, hasJournal);
		
		hasJournal = 0;
		for (final DealerIncomeExpenseFact journal : 
			reportDAL.getDealerIncomeExpenseFacts(2013, Lists.newArrayList(new Integer[]{7}), 
					Lists.newArrayList(new Integer[]{}), 
					Lists.newArrayList(new Integer[]{}), 
					Lists.newArrayList(new String[]{}), 
					Lists.newArrayList(new Integer[]{}))) {
			hasJournal++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals(new BigDecimal("335"), journal.getAmount());
		} 
		Assert.assertEquals(1, hasJournal);
		
		hasJournal = 0;
		for (final DealerIncomeRevenueFact journal : 
			reportDAL.getDealerIncomeRevenueFacts(2013, 
					Lists.newArrayList(new Integer[]{7}), 
					Lists.newArrayList(new Integer[]{}), 
					Lists.newArrayList(new Integer[]{}), 
					Lists.newArrayList(new String[]{}), 
					Lists.newArrayList(new Integer[]{}))) {
			hasJournal++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
		} 
		Assert.assertEquals(1, hasJournal);
		
		int hasReportItem = 0;
		for (final ReportItem journal : 
			reportDAL.getAllReportItem()) {
			hasReportItem++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
		} 
		Assert.assertEquals(3, hasReportItem);
		
		int hasReportTime = 0;
		for (final ReportTime journal : 
			reportDAL.getAllReportTime()) {
			hasReportTime++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
		} 
		Assert.assertEquals(2, hasReportTime);
	} 
	
	@Test
	public void importGeneralJournal() {
		final GeneralJournal status4 = new GeneralJournal();
		status4.setDealerID(10);
		status4.setId(1);
		status4.setAmount(new BigDecimal("1234.343"));
		status4.setDepartmentID(1);
		status4.setValidDate(LocalDate.of(2013, 8, 1));
		status4.setUpdatedBy("test");
		incomeJournalDAL.saveGeneralJournal(10, 1, Lists.newArrayList(status4));
		
		final GeneralJournal status5 = new GeneralJournal();
		status5.setDealerID(11);
		status5.setId(1);
		status5.setAmount(new BigDecimal("5234.343"));
		status5.setDepartmentID(3);
		status5.setValidDate(LocalDate.of(2013, 8, 1));
		status5.setUpdatedBy("test2");
		incomeJournalDAL.saveGeneralJournal(11, 3, Lists.newArrayList(status5));
		
		final GeneralJournal status6 = new GeneralJournal();
		status6.setDealerID(10);
		status6.setId(1);
		status6.setAmount(new BigDecimal("335"));
		status6.setDepartmentID(3);
		status6.setValidDate(LocalDate.of(2013, 7, 1));
		status6.setUpdatedBy("test");
		incomeJournalDAL.saveGeneralJournal(10, 3, Lists.newArrayList(status6));
		
		final GeneralJournal status7 = new GeneralJournal();
		status7.setDealerID(10);
		status7.setId(2);
		status7.setAmount(new BigDecimal("335"));
		status7.setDepartmentID(3);
		status7.setValidDate(LocalDate.of(2013, 7, 1));
		status7.setUpdatedBy("test");
		incomeJournalDAL.saveGeneralJournal(10, 3, Lists.newArrayList(status7));
		
		reportDAL.importGeneralJournal(LocalDate.of(2013, 8, 1));
		reportDAL.importGeneralJournal(LocalDate.of(2013, 7, 1));
		// force to populate twice
		reportDAL.importGeneralJournal(LocalDate.of(2013, 8, 1));
		reportDAL.importGeneralJournal(LocalDate.of(2013, 7, 1));
		
		int hasJournal = 0;
		for (final DealerIncomeRevenueFact journal : 
			reportDAL.getDealerIncomeRevenueFacts(2013, 
					Lists.newArrayList(new Integer[]{8}), 
					Lists.newArrayList(new Integer[]{}), 
					Lists.newArrayList(new Integer[]{}), 
					Lists.newArrayList(new String[]{}), 
					Lists.newArrayList(new Integer[]{}))) {
			hasJournal++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
		} 
		Assert.assertEquals(2, hasJournal);
		
		hasJournal = 0;
		for (final DealerIncomeRevenueFact journal : 
			reportDAL.getDealerIncomeRevenueFacts(2013, 
					Lists.newArrayList(new Integer[]{8}), 
					Lists.newArrayList(new Integer[]{3}), 
					Lists.newArrayList(new Integer[]{}), 
					Lists.newArrayList(new String[]{}), 
					Lists.newArrayList(new Integer[]{}))) {
			hasJournal++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
		} 
		Assert.assertEquals(1, hasJournal);
		
		hasJournal = 0;
		for (final DealerIncomeRevenueFact journal : 
			reportDAL.getDealerIncomeRevenueFacts(2013, 
					Lists.newArrayList(new Integer[]{7}), 
					Lists.newArrayList(new Integer[]{}), 
					Lists.newArrayList(new Integer[]{}), 
					Lists.newArrayList(new String[]{}), 
					Lists.newArrayList(new Integer[]{}))) {
			hasJournal++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
		} 
		Assert.assertEquals(1, hasJournal);
		
		hasJournal = 0;
		for (final DealerIncomeExpenseFact journal : 
			reportDAL.getDealerIncomeExpenseFacts(2013, Lists.newArrayList(new Integer[]{7}), 
					Lists.newArrayList(new Integer[]{}), 
					Lists.newArrayList(new Integer[]{}), 
					Lists.newArrayList(new String[]{}), 
					Lists.newArrayList(new Integer[]{}))) {
			hasJournal++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
		} 
		Assert.assertEquals(1, hasJournal);
		
		hasJournal = 0;
		for (final DealerIncomeExpenseFact journal : 
			reportDAL.getDealerIncomeExpenseFacts(2013, Lists.newArrayList(new Integer[]{7}), 
					Lists.newArrayList(new Integer[]{}), 
					Lists.newArrayList(new Integer[]{}), 
					Lists.newArrayList(new String[]{}), 
					Lists.newArrayList(new Integer[]{}))) {
			hasJournal++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
		} 
		Assert.assertEquals(1, hasJournal);
		
		int hasReportItem = 0;
		for (final ReportItem journal : 
			reportDAL.getAllReportItem()) {
			hasReportItem++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
		} 
		Assert.assertEquals(2, hasReportItem);
		
		Assert.assertEquals("GeneralJournalItem1", reportDAL.getReportItem(1, "GeneralJournal").some().getName());
		
		final ReportItem item = reportDAL.getReportItem(1, "GeneralJournal").some();
		Assert.assertEquals("GeneralJournalItem1", reportDAL.getReportItem(item.getId()).some().getName());
		
		int hasReportTime = 0;
		for (final ReportTime journal : 
			reportDAL.getAllReportTime()) {
			hasReportTime++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
		} 
		Assert.assertEquals(2, reportDAL.getReportTime(2013, Option.<Integer>none()).size());
		Assert.assertEquals(1, reportDAL.getReportTime(2013, Option.<Integer>some(8)).size());
		Assert.assertEquals(2, hasReportTime);
	} 
}
