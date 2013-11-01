package com.jdc.themis.dealer.data.dao.hibernate;

import static com.jdc.themis.dealer.domain.TemporalEntity.INFINITE_TIMEEND;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.time.Instant;
import javax.time.calendar.LocalDate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.jdc.themis.dealer.data.dao.IncomeJournalDAO;
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
import com.jdc.themis.dealer.utils.Performance;
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
	
	@Autowired
	private SessionFactory sessionFactory;

	private final static Integer DEFAULT_INCOME_TAX_ID = 1; // we know our system only have one type of tax now...
	
	@Override
	@Performance
	public Instant saveTaxJournal(final Integer dealerID, final Collection<TaxJournal> journals) {
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
			// flush old journal changes firstly to avoid conflicts with new journals
			session.flush();
			session.disableFilter(TaxJournal.FILTER);
			
			// insert the new journal
			newJournal.setTimestamp(currentTimestamp);
			newJournal.setTimeEnd(INFINITE_TIMEEND);
			session.save(newJournal);
			session.flush();
		}
		return currentTimestamp;
	}

	@Override
	@Performance
	public Collection<TaxJournal> getTaxJournal(final Integer dealerID,
			final LocalDate validDate) {
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

	@Override
	@Performance
	public Instant saveDealerEntryItemStatus(final Integer dealerID,
			final Collection<DealerEntryItemStatus> journals) {
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
			session.flush();
			session.disableFilter(DealerEntryItemStatus.FILER_SINGLEENTRYITEM);
			newJournal.setTimestamp(currentTimestamp);
			newJournal.setTimeEnd(INFINITE_TIMEEND);
			session.save(newJournal);
			session.flush();
		}
		return currentTimestamp;
	}

	@Override
	@Performance
	public Collection<DealerEntryItemStatus> getDealerEntryItemStatus(
			final Integer dealerID, final LocalDate validDate) {
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

	@Override
	@Performance
	public Instant saveVehicleSalesJournal(final Integer dealerID,
			final Integer departmentID, final Collection<VehicleSalesJournal> journals) {
		final Session session = sessionFactory.getCurrentSession();
		Instant currentTimestamp = null;
		for (VehicleSalesJournal newJournal: journals) {
			Preconditions.checkArgument(dealerID.equals(newJournal.getDealerID()), "DealerID doesn't match what in the journal");
			Preconditions.checkArgument(departmentID.equals(newJournal.getDepartmentID()), "DepartmentID doesn't match what in the journal");
			currentTimestamp = Utils.currentTimestamp();
			
			// check whether this journal has been inserted before
			session.enableFilter(VehicleSalesJournal.FILTER_SINGLEITEM)
				.setParameter("id", newJournal.getId())
				.setParameter("dealerID", newJournal.getDealerID())
				.setParameter("departmentID", newJournal.getDepartmentID())
				.setParameter("referenceDate", newJournal.getValidDate())
				.setParameter("referenceTime", currentTimestamp);
			@SuppressWarnings("unchecked")
			final List<VehicleSalesJournal> list = session.createCriteria(VehicleSalesJournal.class).list();
			for ( final VehicleSalesJournal oldJournal : list ) {
				if ( oldJournal.getTimeEnd().isBefore(INFINITE_TIMEEND) ) {
					logger.warn("TimeEnd of the one in database is closed already. {}, {}", oldJournal, currentTimestamp);
				} else {
					oldJournal.setTimeEnd(currentTimestamp);
					session.saveOrUpdate(oldJournal);
				} 	
			} 
			session.flush();
			session.disableFilter(VehicleSalesJournal.FILTER_SINGLEITEM);
			newJournal.setTimestamp(currentTimestamp);
			if ( newJournal.getAmount() == null ) {
				newJournal.setAmount(BigDecimal.ZERO);
			}
			if ( newJournal.getMargin() == null ) {
				newJournal.setMargin(BigDecimal.ZERO);
			}
			if ( newJournal.getCount() == null ) {
				newJournal.setCount(0);
			}
			newJournal.setTimeEnd(INFINITE_TIMEEND);
		    logger.debug("saving new journal {}", newJournal);
			session.save(newJournal);
			session.flush();
		}
		return currentTimestamp;
	}

	@Override
	@Performance
	public Collection<VehicleSalesJournal> getVehicleSalesJournal(
			final Integer dealerID, final Integer departmentID, final LocalDate validDate) {
		final Session session = sessionFactory.getCurrentSession();
		final Instant currentTimestamp = Instant.millis(new Date().getTime());
		session.enableFilter(VehicleSalesJournal.FILTER)
			.setParameter("dealerID", dealerID)
			.setParameter("departmentID", departmentID)
			.setParameter("referenceDate", validDate)
			.setParameter("referenceTime", currentTimestamp);
		@SuppressWarnings("unchecked")
		final List<VehicleSalesJournal> list = session.createCriteria(VehicleSalesJournal.class).list();
		session.disableFilter(VehicleSalesJournal.FILTER);
		
		return ImmutableList.copyOf(list);
	}

	@Override
	@Performance
	public Instant saveSalesServiceJournal(final Integer dealerID,
			final Integer departmentID, final Collection<SalesServiceJournal> journals) {
		final Session session = sessionFactory.getCurrentSession();
		Instant currentTimestamp = null;
		for (SalesServiceJournal newJournal: journals) {
			Preconditions.checkArgument(dealerID.equals(newJournal.getDealerID()), "DealerID doesn't match what in the journal");
			Preconditions.checkArgument(departmentID.equals(newJournal.getDepartmentID()), "DepartmentID doesn't match what in the journal");
			currentTimestamp = Utils.currentTimestamp();
			
			// check whether this journal has been inserted before
			session.enableFilter(SalesServiceJournal.FILTER_SINGLEITEM)
				.setParameter("id", newJournal.getId())
				.setParameter("dealerID", newJournal.getDealerID())
				.setParameter("departmentID", newJournal.getDepartmentID())
				.setParameter("referenceDate", newJournal.getValidDate())
				.setParameter("referenceTime", currentTimestamp);
			@SuppressWarnings("unchecked")
			final List<SalesServiceJournal> list = session.createCriteria(SalesServiceJournal.class).list();
			for ( final SalesServiceJournal oldJournal : list ) {
				if ( oldJournal.getTimeEnd().isBefore(INFINITE_TIMEEND) ) {
					logger.warn("TimeEnd of the one in database is closed already. {}, {}", oldJournal, currentTimestamp);
				} else {
					oldJournal.setTimeEnd(currentTimestamp);
					session.saveOrUpdate(oldJournal);
				} 	
			} 
			session.flush();
			session.disableFilter(SalesServiceJournal.FILTER_SINGLEITEM);
			newJournal.setTimestamp(currentTimestamp);
			if ( newJournal.getAmount() == null ) {
				newJournal.setAmount(BigDecimal.ZERO);
			}
			if ( newJournal.getMargin() == null ) {
				newJournal.setMargin(BigDecimal.ZERO);
			}
			if ( newJournal.getCount() == null ) {
				newJournal.setCount(0);
			}
			newJournal.setTimeEnd(INFINITE_TIMEEND);
			session.save(newJournal);
			session.flush();
		}
		return currentTimestamp;
	}

	@Override
	@Performance
	public Collection<SalesServiceJournal> getSalesServiceJournal(
			final Integer dealerID, final Integer departmentID, final LocalDate validDate) {
		final Session session = sessionFactory.getCurrentSession();
		final Instant currentTimestamp = Instant.millis(new Date().getTime());
		session.enableFilter(SalesServiceJournal.FILTER)
			.setParameter("dealerID", dealerID)
			.setParameter("departmentID", departmentID)
			.setParameter("referenceDate", validDate)
			.setParameter("referenceTime", currentTimestamp);
		@SuppressWarnings("unchecked")
		final List<SalesServiceJournal> list = session.createCriteria(SalesServiceJournal.class).list();
		session.disableFilter(SalesServiceJournal.FILTER);
		
		return ImmutableList.copyOf(list);
	}

	@Override
	public Instant saveGeneralJournal(final Integer dealerID, final Integer departmentID,
			final Collection<GeneralJournal> journals) {
		final Session session = sessionFactory.getCurrentSession();
		Instant currentTimestamp = null;
		for (GeneralJournal newJournal: journals) {
			Preconditions.checkArgument(dealerID.equals(newJournal.getDealerID()), "DealerID doesn't match what in the journal");
			Preconditions.checkArgument(departmentID.equals(newJournal.getDepartmentID()), "DepartmentID doesn't match what in the journal");
			currentTimestamp = Utils.currentTimestamp();
			
			// check whether this journal has been inserted before
			session.enableFilter(GeneralJournal.FILTER_SINGLEITEM)
				.setParameter("id", newJournal.getId())
				.setParameter("dealerID", newJournal.getDealerID())
				.setParameter("departmentID", newJournal.getDepartmentID())
				.setParameter("referenceDate", newJournal.getValidDate())
				.setParameter("referenceTime", currentTimestamp);
			@SuppressWarnings("unchecked")
			final List<GeneralJournal> list = session.createCriteria(GeneralJournal.class).list();
			for ( final GeneralJournal oldJournal : list ) {
				if ( oldJournal.getTimeEnd().isBefore(INFINITE_TIMEEND) ) {
					logger.warn("TimeEnd of the one in database is closed already. {}, {}", oldJournal, currentTimestamp);
				} else {
					oldJournal.setTimeEnd(currentTimestamp);
					session.saveOrUpdate(oldJournal);
				} 	
			} 
			session.flush();
			session.disableFilter(GeneralJournal.FILTER_SINGLEITEM);
			newJournal.setTimestamp(currentTimestamp);
			if ( newJournal.getAmount() == null ) {
				newJournal.setAmount(BigDecimal.ZERO);
			}
			newJournal.setTimeEnd(INFINITE_TIMEEND);
			session.save(newJournal);
			session.flush();
		}
		return currentTimestamp;
	}

	@Override
	public Collection<GeneralJournal> getGeneralJournal(final Integer dealerID,
			final Integer departmentID, final LocalDate validDate) {
		final Session session = sessionFactory.getCurrentSession();
		final Instant currentTimestamp = Instant.millis(new Date().getTime());
		session.enableFilter(GeneralJournal.FILTER)
			.setParameter("dealerID", dealerID)
			.setParameter("departmentID", departmentID)
			.setParameter("referenceDate", validDate)
			.setParameter("referenceTime", currentTimestamp);
		@SuppressWarnings("unchecked")
		final List<GeneralJournal> list = session.createCriteria(GeneralJournal.class).list();
		session.disableFilter(GeneralJournal.FILTER);
		
		return ImmutableList.copyOf(list);
	}

	@Override
	public Instant saveAccountReceivableDuration(final Integer dealerID,
			final Collection<AccountReceivableDuration> journals) {
		final Session session = sessionFactory.getCurrentSession();
		Instant currentTimestamp = null;
		for (AccountReceivableDuration newJournal: journals) {
			Preconditions.checkArgument(dealerID.equals(newJournal.getDealerID()), "DealerID doesn't match what in the journal");
			currentTimestamp = Utils.currentTimestamp();
			
			// check whether this journal has been inserted before
			session.enableFilter(AccountReceivableDuration.FILTER_SINGLEITEM)
				.setParameter("id", newJournal.getId())
				.setParameter("durationID", newJournal.getDurationID())
				.setParameter("dealerID", newJournal.getDealerID())
				.setParameter("referenceDate", newJournal.getValidDate())
				.setParameter("referenceTime", currentTimestamp);
			@SuppressWarnings("unchecked")
			final List<AccountReceivableDuration> list = session.createCriteria(AccountReceivableDuration.class).list();
			for ( final AccountReceivableDuration oldJournal : list ) {
				if ( oldJournal.getTimeEnd().isBefore(INFINITE_TIMEEND) ) {
					logger.warn("TimeEnd of the one in database is closed already. {}, {}", oldJournal, currentTimestamp);
				} else {
					oldJournal.setTimeEnd(currentTimestamp);
					session.saveOrUpdate(oldJournal);
				} 	
			} 
			session.flush();
			session.disableFilter(AccountReceivableDuration.FILTER_SINGLEITEM);
			newJournal.setTimestamp(currentTimestamp);
			if ( newJournal.getAmount() == null ) {
				newJournal.setAmount(BigDecimal.ZERO);
			}
			newJournal.setTimeEnd(INFINITE_TIMEEND);
			session.save(newJournal);
			session.flush();
		}
		return currentTimestamp;
	}

	@Override
	public Collection<AccountReceivableDuration> getAccountReceivableDuration(
			final Integer dealerID, final LocalDate validDate) {
		final Session session = sessionFactory.getCurrentSession();
		final Instant currentTimestamp = Instant.millis(new Date().getTime());
		session.enableFilter(AccountReceivableDuration.FILTER)
			.setParameter("dealerID", dealerID)
			.setParameter("referenceDate", validDate)
			.setParameter("referenceTime", currentTimestamp);
		@SuppressWarnings("unchecked")
		final List<AccountReceivableDuration> list = session.createCriteria(AccountReceivableDuration.class).list();
		session.disableFilter(AccountReceivableDuration.FILTER);
		
		return ImmutableList.copyOf(list);
	}

	@Override
	public Instant saveHumanResourceAllocation(final Integer dealerID,
			final Integer departmentID, final Collection<HumanResourceAllocation> journals) {
		final Session session = sessionFactory.getCurrentSession();
		Instant currentTimestamp = null;
		for (HumanResourceAllocation newJournal: journals) {
			Preconditions.checkArgument(dealerID.equals(newJournal.getDealerID()), "DealerID doesn't match what in the journal");
			Preconditions.checkArgument(departmentID.equals(newJournal.getDepartmentID()), "DepartmentID doesn't match what in the journal");
			currentTimestamp = Utils.currentTimestamp();
			
			// check whether this journal has been inserted before
			session.enableFilter(HumanResourceAllocation.FILTER_SINGLEITEM)
				.setParameter("id", newJournal.getId())
				.setParameter("dealerID", newJournal.getDealerID())
				.setParameter("departmentID", newJournal.getDepartmentID())
				.setParameter("referenceDate", newJournal.getValidDate())
				.setParameter("referenceTime", currentTimestamp);
			@SuppressWarnings("unchecked")
			final List<HumanResourceAllocation> list = session.createCriteria(HumanResourceAllocation.class).list();
			for ( final HumanResourceAllocation oldJournal : list ) {
				if ( oldJournal.getTimeEnd().isBefore(INFINITE_TIMEEND) ) {
					logger.warn("TimeEnd of the one in database is closed already. {}, {}", oldJournal, currentTimestamp);
				} else {
					oldJournal.setTimeEnd(currentTimestamp);
					session.saveOrUpdate(oldJournal);
				} 	
			} 
			session.flush();
			session.disableFilter(HumanResourceAllocation.FILTER_SINGLEITEM);
			newJournal.setTimestamp(currentTimestamp);
			if ( newJournal.getAllocation() == null ) {
				newJournal.setAllocation(BigDecimal.ZERO);
			}
			newJournal.setTimeEnd(INFINITE_TIMEEND);
			session.save(newJournal);
			session.flush();
		}
		return currentTimestamp;
	}

	@Override
	public Collection<HumanResourceAllocation> getHumanResourceAllocation(
			final Integer dealerID, final LocalDate validDate) {
		final Session session = sessionFactory.getCurrentSession();
		final Instant currentTimestamp = Instant.millis(new Date().getTime());
		session.enableFilter(HumanResourceAllocation.FILTER)
			.setParameter("dealerID", dealerID)
			.setParameter("referenceDate", validDate)
			.setParameter("referenceTime", currentTimestamp);
		@SuppressWarnings("unchecked")
		final List<HumanResourceAllocation> list = session.createCriteria(HumanResourceAllocation.class).list();
		session.disableFilter(HumanResourceAllocation.FILTER);
		
		return ImmutableList.copyOf(list);
	}

	@Override
	public Instant saveInventoryDuration(final Integer dealerID,
			final Integer departmentID, final Collection<InventoryDuration> journals) {
		final Session session = sessionFactory.getCurrentSession();
		Instant currentTimestamp = null;
		for (InventoryDuration newJournal: journals) {
			Preconditions.checkArgument(dealerID.equals(newJournal.getDealerID()), "DealerID doesn't match what in the journal");
			Preconditions.checkArgument(departmentID.equals(newJournal.getDepartmentID()), "DepartmentID doesn't match what in the journal");
			currentTimestamp = Utils.currentTimestamp();
			
			// check whether this journal has been inserted before
			session.enableFilter(InventoryDuration.FILTER_SINGLEITEM)
				.setParameter("id", newJournal.getId())
				.setParameter("dealerID", newJournal.getDealerID())
				.setParameter("durationID", newJournal.getDurationID())
				.setParameter("departmentID", newJournal.getDepartmentID())
				.setParameter("referenceDate", newJournal.getValidDate())
				.setParameter("referenceTime", currentTimestamp);
			@SuppressWarnings("unchecked")
			final List<InventoryDuration> list = session.createCriteria(InventoryDuration.class).list();
			for ( final InventoryDuration oldJournal : list ) {
				if ( oldJournal.getTimeEnd().isBefore(INFINITE_TIMEEND) ) {
					logger.warn("TimeEnd of the one in database is closed already. {}, {}", oldJournal, currentTimestamp);
				} else {
					oldJournal.setTimeEnd(currentTimestamp);
					session.saveOrUpdate(oldJournal);
				} 	
			} 
			session.flush();
			session.disableFilter(InventoryDuration.FILTER_SINGLEITEM);
			newJournal.setTimestamp(currentTimestamp);
			if ( newJournal.getAmount() == null ) {
				newJournal.setAmount(BigDecimal.ZERO);
			}

			newJournal.setTimeEnd(INFINITE_TIMEEND);
			session.save(newJournal);
			session.flush();
		}
		return currentTimestamp;
	}

	@Override
	public Collection<InventoryDuration> getInventoryDuration(final Integer dealerID,
			final Integer departmentID, final LocalDate validDate) {
		final Session session = sessionFactory.getCurrentSession();
		final Instant currentTimestamp = Instant.millis(new Date().getTime());
		session.enableFilter(InventoryDuration.FILTER)
			.setParameter("dealerID", dealerID)
			.setParameter("departmentID", departmentID)
			.setParameter("referenceDate", validDate)
			.setParameter("referenceTime", currentTimestamp);
		@SuppressWarnings("unchecked")
		final List<InventoryDuration> list = session.createCriteria(InventoryDuration.class).list();
		session.disableFilter(InventoryDuration.FILTER);
		
		return ImmutableList.copyOf(list);
	}

	@Override
	public Instant saveEmployeeFee(final Integer dealerID, final Integer departmentID,
			final Collection<EmployeeFee> journals) {
		final Session session = sessionFactory.getCurrentSession();
		Instant currentTimestamp = null;
		for (EmployeeFee newJournal: journals) {
			Preconditions.checkArgument(dealerID.equals(newJournal.getDealerID()), "DealerID doesn't match what in the journal");
			Preconditions.checkArgument(departmentID.equals(newJournal.getDepartmentID()), "DepartmentID doesn't match what in the journal");
			currentTimestamp = Utils.currentTimestamp();
			
			// check whether this journal has been inserted before
			session.enableFilter(EmployeeFee.FILTER_SINGLEITEM)
				.setParameter("id", newJournal.getId())
				.setParameter("dealerID", newJournal.getDealerID())
				.setParameter("departmentID", newJournal.getDepartmentID())
				.setParameter("feeTypeID", newJournal.getFeeTypeID())
				.setParameter("referenceDate", newJournal.getValidDate())
				.setParameter("referenceTime", currentTimestamp);
			@SuppressWarnings("unchecked")
			final List<EmployeeFee> list = session.createCriteria(EmployeeFee.class).list();
			for ( final EmployeeFee oldJournal : list ) {
				if ( oldJournal.getTimeEnd().isBefore(INFINITE_TIMEEND) ) {
					logger.warn("TimeEnd of the one in database is closed already. {}, {}", oldJournal, currentTimestamp);
				} else {
					oldJournal.setTimeEnd(currentTimestamp);
					session.saveOrUpdate(oldJournal);
				} 	
			} 
			session.flush();
			session.disableFilter(EmployeeFee.FILTER_SINGLEITEM);
			newJournal.setTimestamp(currentTimestamp);
			if ( newJournal.getAmount() == null ) {
				newJournal.setAmount(BigDecimal.ZERO);
			}

			newJournal.setTimeEnd(INFINITE_TIMEEND);
			session.save(newJournal);
			session.flush();
		}
		return currentTimestamp;
	}

	@Override
	public Collection<EmployeeFee> getEmployeeFee(final Integer dealerID,
			final Integer departmentID, final LocalDate validDate) {
		final Session session = sessionFactory.getCurrentSession();
		final Instant currentTimestamp = Instant.millis(new Date().getTime());
		session.enableFilter(EmployeeFee.FILTER)
			.setParameter("dealerID", dealerID)
			.setParameter("departmentID", departmentID)
			.setParameter("referenceDate", validDate)
			.setParameter("referenceTime", currentTimestamp);
		@SuppressWarnings("unchecked")
		final List<EmployeeFee> list = session.createCriteria(EmployeeFee.class).list();
		session.disableFilter(EmployeeFee.FILTER);
		
		return ImmutableList.copyOf(list);
	}

	@Override
	public Instant saveEmployeeFeeSummary(final Integer dealerID,
			final Integer departmentID, final Collection<EmployeeFeeSummary> journals) {
		final Session session = sessionFactory.getCurrentSession();
		Instant currentTimestamp = null;
		for (EmployeeFeeSummary newJournal: journals) {
			Preconditions.checkArgument(dealerID.equals(newJournal.getDealerID()), "DealerID doesn't match what in the journal");
			Preconditions.checkArgument(departmentID.equals(newJournal.getDepartmentID()), "DepartmentID doesn't match what in the journal");
			currentTimestamp = Utils.currentTimestamp();
			
			// check whether this journal has been inserted before
			session.enableFilter(EmployeeFeeSummary.FILTER_SINGLEITEM)
				.setParameter("id", newJournal.getId())
				.setParameter("dealerID", newJournal.getDealerID())
				.setParameter("departmentID", newJournal.getDepartmentID())
				.setParameter("referenceDate", newJournal.getValidDate())
				.setParameter("referenceTime", currentTimestamp);
			@SuppressWarnings("unchecked")
			final List<EmployeeFeeSummary> list = session.createCriteria(EmployeeFeeSummary.class).list();
			for ( final EmployeeFeeSummary oldJournal : list ) {
				if ( oldJournal.getTimeEnd().isBefore(INFINITE_TIMEEND) ) {
					logger.warn("TimeEnd of the one in database is closed already. {}, {}", oldJournal, currentTimestamp);
				} else {
					oldJournal.setTimeEnd(currentTimestamp);
					session.saveOrUpdate(oldJournal);
				} 	
			} 
			session.flush();
			session.disableFilter(EmployeeFeeSummary.FILTER_SINGLEITEM);
			newJournal.setTimestamp(currentTimestamp);
			if ( newJournal.getAmount() == null ) {
				newJournal.setAmount(BigDecimal.ZERO);
			}
			
			newJournal.setTimeEnd(INFINITE_TIMEEND);
			session.save(newJournal);
			session.flush();
		}
		return currentTimestamp;
	}

	@Override
	public Collection<EmployeeFeeSummary> getEmployeeFeeSummary(
			final Integer dealerID, final Integer departmentID, final LocalDate validDate) {
		final Session session = sessionFactory.getCurrentSession();
		final Instant currentTimestamp = Instant.millis(new Date().getTime());
		session.enableFilter(EmployeeFeeSummary.FILTER)
			.setParameter("dealerID", dealerID)
			.setParameter("departmentID", departmentID)
			.setParameter("referenceDate", validDate)
			.setParameter("referenceTime", currentTimestamp);
		@SuppressWarnings("unchecked")
		final List<EmployeeFeeSummary> list = session.createCriteria(EmployeeFeeSummary.class).list();
		session.disableFilter(EmployeeFeeSummary.FILTER);
		
		return ImmutableList.copyOf(list);
	}

	@Override
	public Collection<VehicleSalesJournal> getVehicleSalesJournal(
			final LocalDate validDate, final Instant timestamp) {
		final Session session = sessionFactory.getCurrentSession();
		session.enableFilter(VehicleSalesJournal.FILTER_VALIDATE)
			.setParameter("referenceDate", validDate)
			.setParameter("referenceTime", timestamp);
		@SuppressWarnings("unchecked")
		final List<VehicleSalesJournal> list = session.createCriteria(
				VehicleSalesJournal.class).list();
		session.disableFilter(VehicleSalesJournal.FILTER_VALIDATE);
		return list;
	}

	@Override
	public Collection<SalesServiceJournal> getSalesServiceJournal(
			final LocalDate validDate, final Instant timestamp) {
		final Session session = sessionFactory.getCurrentSession();
		session.enableFilter(SalesServiceJournal.FILTER_VALIDATE)
			.setParameter("referenceDate", validDate)
			.setParameter("referenceTime", timestamp);
		@SuppressWarnings("unchecked")
		final List<SalesServiceJournal> list = session.createCriteria(
				SalesServiceJournal.class).list();
		session.disableFilter(SalesServiceJournal.FILTER_VALIDATE);
		return list;
	}

	@Override
	public Collection<GeneralJournal> getGeneralJournal(final LocalDate validDate,
			final Instant timestamp) {
		final Session session = sessionFactory.getCurrentSession();
		session.enableFilter(GeneralJournal.FILTER_VALIDATE)
			.setParameter("referenceDate", validDate)
			.setParameter("referenceTime", timestamp);
		@SuppressWarnings("unchecked")
		final List<GeneralJournal> list = session.createCriteria(
				GeneralJournal.class).list();
		session.disableFilter(GeneralJournal.FILTER_VALIDATE);
		return list;
	}

	@Override
	public Collection<TaxJournal> getTaxJournal(final LocalDate validDate,
			final Instant timestamp) {
		final Session session = sessionFactory.getCurrentSession();
		session.enableFilter(TaxJournal.FILTER_VALIDATE)
			.setParameter("referenceDate", validDate)
			.setParameter("referenceTime", timestamp);
		@SuppressWarnings("unchecked")
		final List<TaxJournal> list = session.createCriteria(
				TaxJournal.class).list();
		session.disableFilter(TaxJournal.FILTER_VALIDATE);
		return list;
	}

	@Override
	public Collection<HumanResourceAllocation> getHumanResourceAllocation(
			final LocalDate validDate, final Instant timestamp) {
		final Session session = sessionFactory.getCurrentSession();
		session.enableFilter(HumanResourceAllocation.FILTER_VALIDATE)
			.setParameter("referenceDate", validDate)
			.setParameter("referenceTime", timestamp);
		@SuppressWarnings("unchecked")
		final List<HumanResourceAllocation> list = session.createCriteria(
				HumanResourceAllocation.class).list();
		session.disableFilter(HumanResourceAllocation.FILTER_VALIDATE);
		return list;
	}

}
