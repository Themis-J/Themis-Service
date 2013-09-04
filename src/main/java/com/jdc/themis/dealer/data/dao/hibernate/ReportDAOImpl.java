package com.jdc.themis.dealer.data.dao.hibernate;

import java.util.Collection;
import java.util.List;

import javax.time.calendar.LocalDate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.jdc.themis.dealer.data.dao.IncomeJournalDAO;
import com.jdc.themis.dealer.data.dao.RefDataDAO;
import com.jdc.themis.dealer.data.dao.ReportDAO;
import com.jdc.themis.dealer.domain.DealerIncomeRevenueFact;
import com.jdc.themis.dealer.domain.ReportItem;
import com.jdc.themis.dealer.domain.ReportTime;
import com.jdc.themis.dealer.domain.SalesServiceJournal;
import com.jdc.themis.dealer.domain.VehicleSalesJournal;
import com.jdc.themis.dealer.utils.Performance;
import com.jdc.themis.dealer.utils.Utils;

import fj.data.Option;

@Service
public class ReportDAOImpl implements ReportDAO {
	private final static Logger logger = LoggerFactory
			.getLogger(RefDataDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private RefDataDAO refDataDAL;
	@Autowired
	private IncomeJournalDAO incomeJournalDAL;

	public IncomeJournalDAO getIncomeJournalDAL() {
		return incomeJournalDAL;
	}

	public void setIncomeJournalDAL(final IncomeJournalDAO incomeJournalDAL) {
		this.incomeJournalDAL = incomeJournalDAL;
	}

	public RefDataDAO getRefDataDAL() {
		return refDataDAL;
	}

	public void setRefDataDAL(final RefDataDAO refDataDAL) {
		this.refDataDAL = refDataDAL;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@Performance
	public synchronized void importVehicleSalesJournal(final LocalDate validDate) {
		logger.info("Importing vehicle sales journal to income revenue");
		
		final Collection<VehicleSalesJournal> list = incomeJournalDAL.getVehicleSalesJournal(validDate, Utils.currentTimestamp());

		final List<DealerIncomeRevenueFact> facts = Lists.newArrayList();
		for (final VehicleSalesJournal journal : list) {
			// verify report time
			Option<ReportTime> reportTime = this.getReportTime(validDate);
			if ( reportTime.isNone() ) {
				reportTime = this.addReportTime(validDate);
			} 
			
			final DealerIncomeRevenueFact fact = new DealerIncomeRevenueFact();
			fact.setAmount(journal.getAmount());
			fact.setCount(journal.getCount());
			fact.setMargin(journal.getMargin());
			fact.setTimeID(reportTime.some().getId());
			// verify report item here
			Option<ReportItem> reportItem = this.getReportItem(journal.getId(), "VehicleSalesJournal");
			if ( reportItem.isNone() ) {
				reportItem = 
						this.addReportItem(journal.getId(), 
								refDataDAL.getVehicle(journal.getId()).some().getName(), "VehicleSalesJournal");
			} 
			fact.setDealerID(journal.getDealerID());
			fact.setDepartmentID(journal.getDepartmentID());
			fact.setItemID(reportItem.some().getId());
			fact.setTimestamp(journal.getTimestamp());
			fact.setTimeEnd(journal.getTimeEnd());
			facts.add(fact);
		}
		this.saveDealerIncomeRevenueFacts(facts);
	}

	@Override
	@Performance
	public synchronized void importSalesServiceJournal(final LocalDate validDate) {
		logger.info("Importing sales & service journal to income revenue");
		
		final Collection<SalesServiceJournal> list = incomeJournalDAL.getSalesServiceJournal(validDate, Utils.currentTimestamp());

		final List<DealerIncomeRevenueFact> facts = Lists.newArrayList();
		for (final SalesServiceJournal journal : list) {
			// verify report time
			Option<ReportTime> reportTime = this.getReportTime(validDate);
			if ( reportTime.isNone() ) {
				reportTime = this.addReportTime(validDate);
			} 
			
			final DealerIncomeRevenueFact fact = new DealerIncomeRevenueFact();
			fact.setAmount(journal.getAmount());
			fact.setCount(journal.getCount());
			fact.setMargin(journal.getMargin());
			fact.setTimeID(reportTime.some().getId());
			// verify report item here
			Option<ReportItem> reportItem = this.getReportItem(journal.getId(), "SalesServiceJournal");
			if ( reportItem.isNone() ) {
				reportItem = 
						this.addReportItem(journal.getId(), 
								refDataDAL.getVehicle(journal.getId()).some().getName(), "SalesServiceJournal");
			} 
			fact.setDealerID(journal.getDealerID());
			fact.setDepartmentID(journal.getDepartmentID());
			fact.setItemID(reportItem.some().getId());
			fact.setTimestamp(journal.getTimestamp());
			fact.setTimeEnd(journal.getTimeEnd());
			facts.add(fact);
		}
		this.saveDealerIncomeRevenueFacts(facts);
	}

	@Override
	public synchronized void importGeneralJournal(final LocalDate validDate) {
		// TODO Auto-generated method stub

	}

	@Override
	public Option<ReportTime> addReportTime(final LocalDate validDate) {
		final Session session = sessionFactory.getCurrentSession();
		final ReportTime time = new ReportTime();
		time.setValidDate(validDate);
		time.setMonthOfYear(validDate.getMonthOfYear().getValue());
		time.setYear(validDate.getYear());
		session.save(time);
		session.flush();
		return Option.<ReportTime>some(time);
	}

	@Override
	public Option<ReportItem> addReportItem(final Integer itemID, final String itemName, final String source) {
		final Session session = sessionFactory.getCurrentSession();
		final Integer reportItemSource = refDataDAL.getEnumValue("ReportItemSource", source).some().getValue();
		
		final ReportItem reportItem = new ReportItem();
		reportItem.setItemSource(reportItemSource);
		reportItem.setSourceItemID(itemID);
		reportItem.setName(itemName);
		session.save(reportItem);
		session.flush();
		return Option.<ReportItem>some(reportItem);
	}

	@Override
	public Option<ReportTime> getReportTime(final LocalDate validDate) {
		final Session session = sessionFactory.getCurrentSession();
		session.enableFilter(ReportTime.FILTER)
			.setParameter("referenceDate", validDate);
		@SuppressWarnings("unchecked")
		final List<ReportTime> reportTimeList = session.createCriteria(
				ReportTime.class).list();
		if ( reportTimeList.size() == 0 ) {
			return Option.<ReportTime>none();
		}
		session.disableFilter(ReportTime.FILTER);
		
		return Option.<ReportTime>some(reportTimeList.get(0));
	}

	@Override
	public Option<ReportItem> getReportItem(final Integer itemID, final String source) {
		final Session session = sessionFactory.getCurrentSession();
		final Integer reportItemSource = refDataDAL.getEnumValue("ReportItemSource", source).some().getValue();
		session.enableFilter(ReportItem.FILTER)
		.setParameter("sourceItemID", itemID)
		.setParameter("itemSource", reportItemSource);
		@SuppressWarnings("unchecked")
		final List<ReportItem> reportItems = session.createCriteria(
			ReportItem.class).list();
		session.disableFilter(ReportItem.FILTER);
		if ( reportItems.size() == 0 ) {
			return Option.<ReportItem>none();
		}
		return Option.<ReportItem>some(reportItems.get(0));
	}

	@Override
	public void saveDealerIncomeRevenueFacts(
			final Collection<DealerIncomeRevenueFact> journals) {
		final Session session = sessionFactory.getCurrentSession();
		for (DealerIncomeRevenueFact newJournal: journals) {
			// check whether this journal has been inserted before
			session.enableFilter(DealerIncomeRevenueFact.FILTER)
				.setParameter("timeID", newJournal.getTimeID())
				.setParameter("itemID", newJournal.getItemID())
				.setParameter("dealerID", newJournal.getDealerID())
				.setParameter("departmentID", newJournal.getDepartmentID())
				.setParameter("referenceTime", Utils.currentTimestamp()); // we are only interested in latest record
			@SuppressWarnings("unchecked")
			final List<DealerIncomeRevenueFact> list = session.createCriteria(DealerIncomeRevenueFact.class).list();
			if ( list.size() != 0 ) {
				for ( final DealerIncomeRevenueFact oldJournal : list ) {
					// if we get here, it means we have inserted this fact before
					if ( oldJournal.getTimestamp().isBefore(newJournal.getTimestamp()) ) {
						oldJournal.setTimeEnd(newJournal.getTimestamp());
						session.saveOrUpdate(oldJournal); 	
					} 
					// ignore if we've already got this journal in table
				} 
			} else {
				session.save(newJournal);
			}
			session.disableFilter(DealerIncomeRevenueFact.FILTER);
			
			session.flush();
		}
	}

	@Override
	public Collection<DealerIncomeRevenueFact> getDealerIncomeRevenueFacts(
			final LocalDate validDate, final Option<Integer> departmentID) {
		final Session session = sessionFactory.getCurrentSession();
		final Option<ReportTime> reportTime = getReportTime(validDate);
		if ( reportTime.isNone() ) {
			return Lists.newArrayList();
		} 
		
		if ( departmentID.isSome() ) {
			session.enableFilter(DealerIncomeRevenueFact.FILTER_DEP)
					.setParameter("timeID", reportTime.some().getId())
					.setParameter("referenceTime", Utils.currentTimestamp())
					.setParameter("departmentID", departmentID.some());

			@SuppressWarnings("unchecked")
			final List<DealerIncomeRevenueFact> list = session.createCriteria(DealerIncomeRevenueFact.class).list();
			session.disableFilter(DealerIncomeRevenueFact.FILTER_DEP);
			return list;
		} else {
			session.enableFilter(DealerIncomeRevenueFact.FILTER_ALL)
						.setParameter("timeID", reportTime.some().getId())
						.setParameter("referenceTime", Utils.currentTimestamp()); // we are only interested in latest record
			@SuppressWarnings("unchecked")
			final List<DealerIncomeRevenueFact> list = session.createCriteria(DealerIncomeRevenueFact.class).list();
			session.disableFilter(DealerIncomeRevenueFact.FILTER_ALL);
			return list;	
		}
	}

	@Override
	public Collection<ReportTime> getAllReportTime() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<ReportTime> list = session.createCriteria(ReportTime.class).list();
		return ImmutableList.copyOf(list);
	}

	@Override
	public Collection<ReportItem> getAllReportItem() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<ReportItem> list = session.createCriteria(ReportItem.class).list();
		return ImmutableList.copyOf(list);
	}

}
