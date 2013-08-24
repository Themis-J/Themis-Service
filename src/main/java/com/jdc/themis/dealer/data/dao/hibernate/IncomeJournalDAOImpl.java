package com.jdc.themis.dealer.data.dao.hibernate;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.time.Instant;
import javax.time.calendar.LocalDate;
import javax.time.calendar.LocalDateTime;
import javax.time.calendar.TimeZone;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.jdc.themis.dealer.data.dao.IncomeJournalDAO;
import com.jdc.themis.dealer.domain.DealerEntryItemStatus;
import com.jdc.themis.dealer.domain.TaxJournal;

@Service
public class IncomeJournalDAOImpl implements IncomeJournalDAO {
	private final static Logger logger = LoggerFactory.getLogger(RefDataDAOImpl.class);
	
	// We are using a local in memory lock now for updating each db resource. 
	// If we are going to move to multi-server solutions, we probably need better solution here. 
	// for example, use optimistic lock or each db exclusive lock.
	private Map<Integer, ReentrantReadWriteLock> taxJournalLocks = Maps.newHashMap();
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private final static Integer DEFAULT_INCOME_TAX = 1; // we know our system only have one type of tax now...
	private final static Instant INFINITE_TIMEEND = LocalDateTime.parse("9999-01-01T00:00:00.000").atZone(TimeZone.UTC).toInstant();
	
	private void checkTaxLock(Integer dealerID) {
		synchronized(this) {
			if ( !taxJournalLocks.containsKey(dealerID) ) {
				taxJournalLocks.put(dealerID, new ReentrantReadWriteLock());
			}
		}
	}
	@Override
	public Instant saveTaxJournal(Integer dealerID, Collection<TaxJournal> journals) {
		checkTaxLock(dealerID);
		taxJournalLocks.get(dealerID).writeLock().lock();
		try {
			final Session session = sessionFactory.getCurrentSession();
			Instant currentTimestamp = null;
			for (TaxJournal newTaxJournal: journals) {
				if ( newTaxJournal.getId() == null ) {
					// set default tax item id
					newTaxJournal.setId(DEFAULT_INCOME_TAX);
				}
				// get new updated timestamp
				// Kai: if we have multiple servers or database is on different machine as application server, 
				// then we need modify this to use database timestamp.
				currentTimestamp = Instant.millis(new Date().getTime());
				
				// check whether this journal has been inserted before
				session.enableFilter("filter")
					.setParameter("id", newTaxJournal.getId())
					.setParameter("dealerID", newTaxJournal.getDealerID())
					.setParameter("referenceDate", newTaxJournal.getValidDate())
					.setParameter("referenceTime", currentTimestamp);
				@SuppressWarnings("unchecked")
				final List<TaxJournal> list = session.createCriteria(TaxJournal.class).list();
				for ( final TaxJournal oldTaxJournal : list ) {
					// oh, we found a tax journal already inserted by user before, this is an update!
					if ( currentTimestamp.isBefore(oldTaxJournal.getTimestamp()) ) {
						logger.warn("Current time is older than the one in database, which should not happen. {}", oldTaxJournal, currentTimestamp);
						throw new RuntimeException("Current time is older than the one in database");
					} else {
						oldTaxJournal.setTimeEnd(currentTimestamp);
						session.saveOrUpdate(oldTaxJournal);
					}
				}
				session.disableFilter("filter");
				
				newTaxJournal.setTimestamp(currentTimestamp);
				newTaxJournal.setTimeEnd(INFINITE_TIMEEND);
				
				session.save(newTaxJournal);
				session.flush();
			}
			return currentTimestamp;
		} finally {
			taxJournalLocks.get(dealerID).writeLock().unlock();
		}
	}

	@Override
	public Collection<TaxJournal> getTaxJournal(Integer dealerID,
			LocalDate validDate) {
		checkTaxLock(dealerID);
		taxJournalLocks.get(dealerID).readLock().lock();
		try {
			final Session session = sessionFactory.getCurrentSession();
			final Instant currentTimestamp = Instant.millis(new Date().getTime());
			session.enableFilter("filter")
			.setParameter("id", DEFAULT_INCOME_TAX)
			.setParameter("dealerID", dealerID)
			.setParameter("referenceDate", validDate)
			.setParameter("referenceTime", currentTimestamp);
			@SuppressWarnings("unchecked")
			final List<TaxJournal> list = session.createCriteria(TaxJournal.class).list();
			session.disableFilter("filter");
			
			return ImmutableList.copyOf(list);
		} finally {
			taxJournalLocks.get(dealerID).readLock().unlock();
		}
	}

	@Override
	public Instant saveDealerEntryItemStatus(Integer dealerID,
			Collection<DealerEntryItemStatus> journals) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<DealerEntryItemStatus> getDealerEntryItemStatus(
			Integer dealerID, LocalDate validDate) {
		// TODO Auto-generated method stub
		return null;
	}

}
