package com.jdc.themis.dealer.data.dao;

import java.util.Collection;

import javax.time.Instant;
import javax.time.calendar.LocalDate;

import com.jdc.themis.dealer.domain.DealerEntryItemStatus;
import com.jdc.themis.dealer.domain.TaxJournal;

public interface IncomeJournalDAO {

	Instant saveTaxJournal(Integer dealerID, Collection<TaxJournal> journals);
	
	Collection<TaxJournal> getTaxJournal(Integer dealerID, LocalDate validDate);
	
	Instant saveDealerEntryItemStatus(Integer dealerID, Collection<DealerEntryItemStatus> journals);
	
	Collection<DealerEntryItemStatus> getDealerEntryItemStatus(Integer dealerID, LocalDate validDate);
	
}
