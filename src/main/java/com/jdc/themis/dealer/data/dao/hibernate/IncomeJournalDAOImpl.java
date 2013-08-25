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

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.jdc.themis.dealer.data.dao.IncomeJournalDAO;
import com.jdc.themis.dealer.domain.DealerEntryItemStatus;
import com.jdc.themis.dealer.domain.TaxJournal;
import com.jdc.themis.dealer.utils.Utils;

/**
 * Data access layer for income journal items. 
 * 
 * @author Kai Chen
 *
 */
@Service
public class IncomeJournalDAOImpl implements IncomeJournalDAO {
	private final static Logger logger = LoggerFactory.getLogger(RefDataDAOImpl.class);
	
	// We are using a local in memory lock now for updating each db resource. 
	// If we are going to move to multi-server solutions, we probably need better solution here. 
	// for example, use optimistic lock or each db exclusive lock.
	private Map<String, Map<String, ReentrantReadWriteLock>> journalLocks = Maps.newHashMap();
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private final static Integer DEFAULT_INCOME_TAX_ID = 1; // we know our system only have one type of tax now...
	// notice that database record must have UTC timezone for comparison
	private final static Instant INFINITE_TIMEEND = LocalDateTime.parse("9999-01-01T00:00:00.000").atZone(TimeZone.UTC).toInstant();
	
	private void checkJournalLock(String lockKey, String key) {
		synchronized(this) {
			if ( !journalLocks.containsKey(lockKey) ) {
				final Map<String, ReentrantReadWriteLock> locks = Maps.newHashMap();
				journalLocks.put(lockKey, locks);
			}
			if ( !journalLocks.get(lockKey).containsKey(key) ) {
				journalLocks.get(lockKey).put(key, new ReentrantReadWriteLock());
			}
		}
	}
	
	interface JournalRunnable<T> {
		T run();
	}
	
	// This is immutable and thread safe
	class ExecutorWithJournalLock {
		
		public <T> T executeWithWriteLock(String lockKey, String key, JournalRunnable<T> runnable) {
			checkJournalLock(lockKey, key);
			journalLocks.get(lockKey).get(key).writeLock().lock();
			try {
				return runnable.run();
			} finally {
				journalLocks.get(lockKey).get(key).writeLock().unlock();
			}
		}
		
		public <T> T executeWithReadLock(String lockKey, String key, JournalRunnable<T> runnable) {
			checkJournalLock(lockKey, key);
			journalLocks.get(lockKey).get(key).readLock().lock();
			try {
				return runnable.run();
			} finally {
				journalLocks.get(lockKey).get(key).readLock().unlock();
			}
		}
		
	}
	
	private final ExecutorWithJournalLock executor = new ExecutorWithJournalLock();
	
	@Override
	public Instant saveTaxJournal(final Integer dealerID, final Collection<TaxJournal> journals) {
		return executor.executeWithWriteLock(TaxJournal.class.getName(), dealerID.toString(), new JournalRunnable<Instant>() {

			@Override
			public Instant run() {
				final Session session = sessionFactory.getCurrentSession();
				Instant currentTimestamp = null;
				for (TaxJournal newJournal: journals) {
					Preconditions.checkArgument(dealerID.equals(newJournal.getDealerID()), "DealerID doesn't match what in the journal");
					if ( newJournal.getId() == null ) {
						// set default tax item id
						newJournal.setId(DEFAULT_INCOME_TAX_ID);
					}
					// get new updated timestamp
					// Kai: if we have multiple servers or database is on different machine as application server, 
					// then we need modify this to use database timestamp.
					currentTimestamp = Instant.millis(new Date().getTime());
					
					// check whether this journal has been inserted before
					session.enableFilter(TaxJournal.FILTER)
						.setParameter("id", newJournal.getId())
						.setParameter("dealerID", newJournal.getDealerID())
						.setParameter("referenceDate", newJournal.getValidDate())
						.setParameter("referenceTime", currentTimestamp);
					@SuppressWarnings("unchecked")
					final List<TaxJournal> list = session.createCriteria(TaxJournal.class).list();
					for ( final TaxJournal oldJournal : list ) {
						// oh, we found a tax journal already inserted by user before, this is an update!
						if ( oldJournal.getTimeEnd().isBefore(INFINITE_TIMEEND) ) {
							logger.warn("TimeEnd of the one in database is closed already. {}, {}", oldJournal, currentTimestamp);
						} else {
							// close old journal record by closing its time end
							oldJournal.setTimeEnd(currentTimestamp);
							session.saveOrUpdate(oldJournal);
						} 	
					} 
					session.disableFilter(TaxJournal.FILTER);
					
					// insert the new journal
					newJournal.setTimestamp(currentTimestamp);
					newJournal.setTimeEnd(INFINITE_TIMEEND);
					session.save(newJournal);
					session.flush();
				}
				return currentTimestamp;
			}
			
		});
		
	}

	@Override
	public Collection<TaxJournal> getTaxJournal(final Integer dealerID,
			final LocalDate validDate) {
		return executor.executeWithReadLock(TaxJournal.class.getName(), dealerID.toString(), new JournalRunnable<Collection<TaxJournal>>() {

			@Override
			public Collection<TaxJournal> run() {
				final Session session = sessionFactory.getCurrentSession();
				final Instant currentTimestamp = Instant.millis(new Date().getTime());
				session.enableFilter(TaxJournal.FILTER)
				.setParameter("id", DEFAULT_INCOME_TAX_ID)
				.setParameter("dealerID", dealerID)
				.setParameter("referenceDate", validDate)
				.setParameter("referenceTime", currentTimestamp);
				@SuppressWarnings("unchecked")
				final List<TaxJournal> list = session.createCriteria(TaxJournal.class).list();
				session.disableFilter(TaxJournal.FILTER);
				
				return ImmutableList.copyOf(list);
			}
			
		});
		
	}

	@Override
	public Instant saveDealerEntryItemStatus(final Integer dealerID,
			final Collection<DealerEntryItemStatus> journals) {
		return executor.executeWithWriteLock(TaxJournal.class.getName(), dealerID.toString(), new JournalRunnable<Instant>() {

			@Override
			public Instant run() {
				final Session session = sessionFactory.getCurrentSession();
				Instant currentTimestamp = null;
				for (DealerEntryItemStatus newJournal: journals) {
					Preconditions.checkArgument(dealerID.equals(newJournal.getDealerID()), "DealerID doesn't match what in the journal");
					currentTimestamp = Utils.currentTimestamp();
					
					// check whether this journal has been inserted before
					session.enableFilter(DealerEntryItemStatus.FILER_SINGLEENTRYITEM)
						.setParameter("entryItemID", newJournal.getEntryItemID())
						.setParameter("dealerID", newJournal.getDealerID())
						.setParameter("referenceDate", newJournal.getValidDate())
						.setParameter("referenceTime", currentTimestamp);
					@SuppressWarnings("unchecked")
					final List<DealerEntryItemStatus> list = session.createCriteria(DealerEntryItemStatus.class).list();
					for ( final DealerEntryItemStatus oldJournal : list ) {
						if ( oldJournal.getTimeEnd().isBefore(INFINITE_TIMEEND) ) {
							logger.warn("TimeEnd of the one in database is closed already. {}, {}", oldJournal, currentTimestamp);
						} else {
							oldJournal.setTimeEnd(currentTimestamp);
							session.saveOrUpdate(oldJournal);
						} 	
					} 
					session.disableFilter(DealerEntryItemStatus.FILER_SINGLEENTRYITEM);
					newJournal.setTimestamp(currentTimestamp);
					newJournal.setTimeEnd(INFINITE_TIMEEND);
					session.save(newJournal);
					session.flush();
				}
				return currentTimestamp;
			}
			
		});
		
	}

	@Override
	public Collection<DealerEntryItemStatus> getDealerEntryItemStatus(
			final Integer dealerID, final LocalDate validDate) {
		return executor.executeWithReadLock(DealerEntryItemStatus.class.getName(), dealerID.toString(), new JournalRunnable<Collection<DealerEntryItemStatus>>() {

			@Override
			public Collection<DealerEntryItemStatus> run() {
				final Session session = sessionFactory.getCurrentSession();
				final Instant currentTimestamp = Instant.millis(new Date().getTime());
				session.enableFilter(DealerEntryItemStatus.FILTER)
				.setParameter("dealerID", dealerID)
				.setParameter("referenceDate", validDate)
				.setParameter("referenceTime", currentTimestamp);
				@SuppressWarnings("unchecked")
				final List<DealerEntryItemStatus> list = session.createCriteria(DealerEntryItemStatus.class).list();
				session.disableFilter(DealerEntryItemStatus.FILTER);
				
				return ImmutableList.copyOf(list);
			}
			
		});
		
	}

}
