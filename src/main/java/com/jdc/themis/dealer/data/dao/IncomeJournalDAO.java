package com.jdc.themis.dealer.data.dao;

import java.util.Collection;

import javax.time.calendar.LocalDate;

import com.jdc.themis.dealer.domain.TaxJournal;

public interface IncomeJournalDAO {

	void saveTaxJournal(Collection<TaxJournal> journals);
	
	Collection<TaxJournal> getTaxJournal(Integer dealerID, LocalDate validDate);
	
}
