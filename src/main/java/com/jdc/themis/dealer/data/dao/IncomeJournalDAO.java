package com.jdc.themis.dealer.data.dao;

import java.util.Collection;

import javax.time.Instant;
import javax.time.calendar.LocalDate;

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

/**
 * Income journal data access layer.
 * 
 * @author Kai Chen
 *
 */
public interface IncomeJournalDAO {

	Instant saveTaxJournal(Integer dealerID, Collection<TaxJournal> journals);
	
	Collection<TaxJournal> getTaxJournal(Integer dealerID, LocalDate validDate);
	
	Instant saveDealerEntryItemStatus(Integer dealerID, Collection<DealerEntryItemStatus> journals);
	
	Collection<DealerEntryItemStatus> getDealerEntryItemStatus(Integer dealerID, LocalDate validDate);
	
	Instant saveVehicleSalesJournal(Integer dealerID, Integer departmentID, Collection<VehicleSalesJournal> journals);
	
	Collection<VehicleSalesJournal> getVehicleSalesJournal(Integer dealerID, Integer departmentID, LocalDate validDate);
	
	Instant saveSalesServiceJournal(Integer dealerID, Integer departmentID, Collection<SalesServiceJournal> journals);
	
	Collection<SalesServiceJournal> getSalesServiceJournal(Integer dealerID, Integer departmentID, LocalDate validDate);
	
	Instant saveGeneralJournal(Integer dealerID, Integer departmentID, Collection<GeneralJournal> journals);
	
	Collection<GeneralJournal> getGeneralJournal(Integer dealerID, Integer departmentID, LocalDate validDate);
	
	Instant saveAccountReceivableDuration(Integer dealerID, Collection<AccountReceivableDuration> journals);
	
	Collection<AccountReceivableDuration> getAccountReceivableDuration(Integer dealerID, LocalDate validDate);
	
	Instant saveHumanResourceAllocation(Integer dealerID, Integer departmentID, Collection<HumanResourceAllocation> journals);
	
	Collection<HumanResourceAllocation> getHumanResourceAllocation(Integer dealerID, Integer departmentID, LocalDate validDate);
	
	Instant saveInventoryDuration(Integer dealerID, Integer departmentID, Collection<InventoryDuration> journals);
	
	Collection<InventoryDuration> getInventoryDuration(Integer dealerID, Integer departmentID, LocalDate validDate);
	
	Instant saveEmployeeFee(Integer dealerID, Integer departmentID, Collection<EmployeeFee> journals);
	
	Collection<EmployeeFee> getEmployeeFee(Integer dealerID, Integer departmentID, LocalDate validDate);
	
	Instant saveEmployeeFeeSummary(Integer dealerID, Integer departmentID, Collection<EmployeeFeeSummary> journals);
	
	Collection<EmployeeFeeSummary> getEmployeeFeeSummary(Integer dealerID, Integer departmentID, LocalDate validDate);
	
}
