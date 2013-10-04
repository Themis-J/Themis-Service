package com.jdc.themis.dealer.app;

import java.math.BigDecimal;
import java.util.Random;

import javax.time.calendar.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.jdc.themis.dealer.data.dao.IncomeJournalDAO;
import com.jdc.themis.dealer.domain.DealerEntryItemStatus;
import com.jdc.themis.dealer.domain.GeneralJournal;
import com.jdc.themis.dealer.domain.SalesServiceJournal;
import com.jdc.themis.dealer.domain.VehicleSalesJournal;

/**
 * Auto-client to generate random journals for tests.
 * 
 * @author chen386_2000
 *
 */
@Service
public class ReportDataGenerator {

	@Autowired
	private IncomeJournalDAO incomeJournalDAL;
	
	@Transactional
	public void generateEntryItemStatus() {
		final int numberOfRecords = 1;
		//final int startYear = 2012;
		//final int maxYearOffset = 2;
		//final int maxMonth = 10;
		//final int maxEntryItemID = 2;
		//final int maxDealerID = 2;
		
		//final Random r = new Random();
		
		for ( int i = 0; i <= numberOfRecords; i++ ) {
			final DealerEntryItemStatus journal = new DealerEntryItemStatus();
			journal.setUpdateBy("chenkai");
			//journal.setDealerID(r.nextInt(maxDealerID) + 1);
			journal.setDealerID(2);
			journal.setEntryItemID(1);
			int monthOfYear = 10;//r.nextInt(maxMonth) + 1;
			int year = 2012;//startYear + r.nextInt(maxYearOffset);
			int dayOfMonth = 1;
			journal.setValidDate(LocalDate.of(year, monthOfYear, dayOfMonth));
			System.err.println(journal);
			this.incomeJournalDAL.saveDealerEntryItemStatus(journal.getDealerID(), Lists.newArrayList(journal));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} 
	}
	
	@Transactional
	public void generateRevenues() {
		final int numberOfRecords = 100;
		final int startYear = 2012;
		final int maxYearOffset = 2;
		final int maxMonth = 10;
		final int maxVehicleID = 40;
		final int maxDealerID = 20;
		final Double amountMultiplier = 20000D;
		final Double marginMultiplier = 10000D;
		final int startCount = 200;
		
		final Random r = new Random();
		
		// generate VehicleSalesJournals
		for ( int i = 0; i <= numberOfRecords; i++ ) {
			final VehicleSalesJournal journal = new VehicleSalesJournal();
			journal.setDepartmentID(1); // new vehicle deparment is only 1 now
			journal.setUpdatedBy("chenkai");
			journal.setDealerID(r.nextInt(maxDealerID) + 1);
			journal.setId(r.nextInt(maxVehicleID) + 1);
			journal.setAmount(new BigDecimal("" + (amountMultiplier * r.nextDouble())));
			journal.setMargin(new BigDecimal("" + (marginMultiplier * r.nextDouble())));
			journal.setCount(r.nextInt(1000) + startCount);
			int monthOfYear = r.nextInt(maxMonth) + 1;
			int year = startYear + r.nextInt(maxYearOffset);
			int dayOfMonth = 1;
			journal.setValidDate(LocalDate.of(year, monthOfYear, dayOfMonth));
			System.err.println(journal);
			this.incomeJournalDAL.saveVehicleSalesJournal(journal.getDealerID(), journal.getDepartmentID(), Lists.newArrayList(journal));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} 
		
		// generate SalesServiceJournals
		for ( int i = 0; i <= numberOfRecords; i++ ) {
			final SalesServiceJournal journal = new SalesServiceJournal();
			int departmentID = r.nextInt(8);
			if (departmentID <= 1) {
				departmentID = 7;
			} 
			journal.setDepartmentID(departmentID); // new vehicle deparment is only 1 now
			journal.setUpdatedBy("chenkai2");
			journal.setDealerID(r.nextInt(maxDealerID) + 1);
			journal.setId(r.nextInt(37) + 1);
			journal.setAmount(new BigDecimal("" + (amountMultiplier * r.nextDouble())));
			journal.setMargin(new BigDecimal("" + (marginMultiplier * r.nextDouble())));
			journal.setCount(r.nextInt(1000) + startCount);
			int monthOfYear = r.nextInt(maxMonth) + 1;
			int year = startYear + r.nextInt(maxYearOffset);
			int dayOfMonth = 1;
			journal.setValidDate(LocalDate.of(year, monthOfYear, dayOfMonth));
			System.err.println(journal);
			this.incomeJournalDAL.saveSalesServiceJournal(journal.getDealerID(), journal.getDepartmentID(), Lists.newArrayList(journal));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} 
	}
	
	@Transactional
	public void generateExpenses() {
		final int numberOfRecords = 100;
		final int startYear = 2012;
		final int maxYearOffset = 2;
		final int maxMonth = 10;
		final int maxDealerID = 20;
		final Double amountMultiplier = 20000D;
		
		final Random r = new Random();
		
		// generate GeneralJournals
		for ( int i = 0; i <= numberOfRecords; i++ ) {
			final GeneralJournal journal = new GeneralJournal();
			int departmentID = r.nextInt(8);
			if (departmentID <= 1) {
				departmentID = 7;
			} 
			journal.setDepartmentID(departmentID); 
			journal.setUpdatedBy("chenkai");
			journal.setDealerID(r.nextInt(maxDealerID) + 1);
			journal.setId(r.nextInt(58) + 1);
			journal.setAmount(new BigDecimal("" + (amountMultiplier * r.nextDouble())));
			int monthOfYear = r.nextInt(maxMonth) + 1;
			int year = startYear + r.nextInt(maxYearOffset);
			int dayOfMonth = 1;
			journal.setValidDate(LocalDate.of(year, monthOfYear, dayOfMonth));
			System.err.println(journal);
			this.incomeJournalDAL.saveGeneralJournal(journal.getDealerID(), journal.getDepartmentID(), Lists.newArrayList(journal));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} 
	}
}
