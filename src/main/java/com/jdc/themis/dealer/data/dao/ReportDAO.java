package com.jdc.themis.dealer.data.dao;

import java.util.Collection;
import javax.time.calendar.LocalDate;

import com.jdc.themis.dealer.domain.DealerIncomeExpenseFact;
import com.jdc.themis.dealer.domain.DealerIncomeRevenueFact;
import com.jdc.themis.dealer.domain.ReportItem;
import com.jdc.themis.dealer.domain.ReportTime;

import fj.data.Option;

public interface ReportDAO {

	void saveDealerIncomeRevenueFacts(Collection<DealerIncomeRevenueFact> journals);
	
	Collection<DealerIncomeRevenueFact> getDealerIncomeRevenueFacts(
			Integer year,
			Collection<Integer> monthOfYear, 
			Collection<Integer> departmentID,
			Collection<Integer> itemSource,
			Collection<String> itemCategory, 
			Collection<Integer> itemID, 
			Collection<Integer> dealerID);
	
	void saveDealerIncomeExpenseFacts(Collection<DealerIncomeExpenseFact> journals);
	
	Collection<DealerIncomeExpenseFact> getDealerIncomeExpenseFacts(
			Integer year,
			Collection<Integer> monthOfYear, 
			Collection<Integer> departmentID, 
			Collection<Integer> itemSource,
			Collection<String> itemCategory, 
			Collection<Integer> itemID, 
			Collection<Integer> dealerID);
	
	void importVehicleSalesJournal(LocalDate validDate);
	
	void importSalesServiceJournal(LocalDate validDate);
	
	void importGeneralJournal(LocalDate validDate);
	
	void importTaxJournal(LocalDate validDate);
	
	Option<ReportTime> getReportTime(LocalDate validDate);
	
	Collection<ReportTime> getReportTime(Integer year, Option<Integer> monthOfYear);
	
	Collection<ReportTime> getAllReportTime();
	
	Option<ReportTime> addReportTime(LocalDate validDate);
	
	Option<ReportItem> addReportItem(Integer itemID, String itemName, String source, String category);
	
	Option<ReportItem> getReportItem(Integer itemID, String source);
	
	Option<ReportItem> getReportItem(Long id);
	
	Collection<ReportItem> getAllReportItem();
}
