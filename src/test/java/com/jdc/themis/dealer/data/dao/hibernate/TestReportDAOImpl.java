package com.jdc.themis.dealer.data.dao.hibernate;

import java.math.BigDecimal;

import javax.time.calendar.LocalDate;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.jdc.themis.dealer.data.dao.IncomeJournalDAO;
import com.jdc.themis.dealer.data.dao.ReportDAO;
import com.jdc.themis.dealer.domain.DealerIncomeRevenueFact;
import com.jdc.themis.dealer.domain.ReportItem;
import com.jdc.themis.dealer.domain.ReportTime;
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

	@Before
	public void setUp() {
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
	}

	@Test
	public void importVehicleSalesJournal() {
		reportDAL.importVehicleSalesJournal(LocalDate.of(2013, 8, 1));
		reportDAL.importVehicleSalesJournal(LocalDate.of(2013, 7, 1));
		// force to populate twice
		reportDAL.importVehicleSalesJournal(LocalDate.of(2013, 8, 1));
		reportDAL.importVehicleSalesJournal(LocalDate.of(2013, 7, 1));
		
		int hasJournal = 0;
		for (final DealerIncomeRevenueFact journal : 
			reportDAL.getDealerIncomeRevenueFacts(LocalDate.of(2013, 8, 1), Option.<Integer>none())) {
			hasJournal++;
			System.err.println(journal);
			Assert.assertNotNull(journal);
		} 
		Assert.assertEquals(2, hasJournal);
		
		hasJournal = 0;
		for (final DealerIncomeRevenueFact journal : 
			reportDAL.getDealerIncomeRevenueFacts(LocalDate.of(2013, 7, 1), Option.<Integer>none())) {
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
}
