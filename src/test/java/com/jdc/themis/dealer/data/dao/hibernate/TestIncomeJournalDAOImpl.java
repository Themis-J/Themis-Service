package com.jdc.themis.dealer.data.dao.hibernate;

import java.math.BigDecimal;

import javax.time.calendar.LocalDate;
import javax.time.calendar.LocalDateTime;
import javax.time.calendar.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.jdc.themis.dealer.data.dao.IncomeJournalDAO;
import com.jdc.themis.dealer.domain.TaxJournal;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
    "/META-INF/test-database-config.xml" })
@Transactional
public class TestIncomeJournalDAOImpl {

	@Autowired
	private IncomeJournalDAO incomeJournalDAL;
	
	@Test
	public void insertOneTaxJournal() {
		final TaxJournal taxJournal = new TaxJournal();
		taxJournal.setAmount(new BigDecimal("1234.01"));
		taxJournal.setDealerID(1);
		taxJournal.setId(1);
		taxJournal.setUpdateBy("test");
		taxJournal.setTimestamp(LocalDateTime.parse("2013-08-03T10:15:30.000").atZone(TimeZone.UTC).toInstant());
		taxJournal.setTimeEnd(LocalDateTime.parse("9999-01-01T00:00:00.000").atZone(TimeZone.UTC).toInstant());
		incomeJournalDAL.saveTaxJournal(Lists.newArrayList(taxJournal));
		
		for (final TaxJournal journal : incomeJournalDAL.getTaxJournal(1, LocalDate.of(2013, 8, 10))) {
			System.out.println(journal);
		}
	} 
	
}
