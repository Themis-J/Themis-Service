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
		taxJournal.setValidDate(LocalDate.of(2013, 8, 1));
		taxJournal.setUpdateBy("test");
		incomeJournalDAL.saveTaxJournal(1, Lists.newArrayList(taxJournal));
		
		int hasJournal = 0;
		for (final TaxJournal journal : incomeJournalDAL.getTaxJournal(1, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.out.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test", journal.getUpdateBy());
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
		taxJournal1.setUpdateBy("test1");
		incomeJournalDAL.saveTaxJournal(1, Lists.newArrayList(taxJournal1));
		
		int hasJournal = 0;
		for (final TaxJournal journal : incomeJournalDAL.getTaxJournal(1, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.out.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test1", journal.getUpdateBy());
		}
		Assert.assertEquals(1, hasJournal);
		
		final TaxJournal taxJournal2 = new TaxJournal();
		taxJournal2.setAmount(new BigDecimal("1235.01"));
		taxJournal2.setDealerID(1);
		taxJournal2.setId(1);
		taxJournal2.setValidDate(LocalDate.of(2013, 8, 1));
		taxJournal2.setUpdateBy("test2");
		incomeJournalDAL.saveTaxJournal(1, Lists.newArrayList(taxJournal2));
		
		hasJournal = 0;
		for (final TaxJournal journal : incomeJournalDAL.getTaxJournal(1, LocalDate.of(2013, 8, 1))) {
			hasJournal++;
			System.out.println(journal);
			Assert.assertNotNull(journal);
			Assert.assertEquals("test2", journal.getUpdateBy());
		}
		Assert.assertEquals(1, hasJournal);
	} 
}
