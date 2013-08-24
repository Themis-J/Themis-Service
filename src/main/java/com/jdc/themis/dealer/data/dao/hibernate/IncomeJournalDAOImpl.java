package com.jdc.themis.dealer.data.dao.hibernate;

import java.util.Collection;
import java.util.List;

import javax.time.calendar.LocalDate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;
import com.jdc.themis.dealer.data.dao.IncomeJournalDAO;
import com.jdc.themis.dealer.domain.SalesServiceJournalItem;
import com.jdc.themis.dealer.domain.TaxJournal;

@Service
public class IncomeJournalDAOImpl implements IncomeJournalDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void saveTaxJournal(Collection<TaxJournal> journals) {
		final Session session = sessionFactory.getCurrentSession();
		for (TaxJournal journal: journals) {
			session.save(journal);
		}
	}

	@Override
	public Collection<TaxJournal> getTaxJournal(Integer dealerID,
			LocalDate validDate) {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<TaxJournal> list = session.createCriteria(TaxJournal.class).list();
		return ImmutableList.copyOf(list);
	}

}
