package com.jdc.themis.dealer.data.dao;

import java.util.Collection;

import javax.time.Instant;
import javax.time.calendar.LocalDate;

import com.jdc.themis.dealer.domain.DealerEntryItemStatus;
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
	
}
