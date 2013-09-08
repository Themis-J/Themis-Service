package com.jdc.themis.dealer.data.dao.hibernate;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.time.calendar.LocalDate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jdc.themis.dealer.data.dao.IncomeJournalDAO;
import com.jdc.themis.dealer.data.dao.RefDataDAO;
import com.jdc.themis.dealer.data.dao.ReportDAO;
import com.jdc.themis.dealer.domain.DealerIncomeExpenseFact;
import com.jdc.themis.dealer.domain.DealerIncomeRevenueFact;
import com.jdc.themis.dealer.domain.GeneralJournal;
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

	@Override
	@Performance
	public void importVehicleSalesJournal(final LocalDate validDate) {
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
								refDataDAL.getVehicle(journal.getId()).some().getName(), 
								"VehicleSalesJournal",
								refDataDAL.getSalesServiceJournalCategory(journal.getId()).some().getName());
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
	public void importSalesServiceJournal(final LocalDate validDate) {
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
								refDataDAL.getSalesServiceJournalItem(journal.getId()).some().getName(), 
								"SalesServiceJournal", 
								refDataDAL.getSalesServiceJournalCategory(journal.getId()).some().getName());
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
	public void importGeneralJournal(final LocalDate validDate) {
		logger.info("Importing general journal to income revenue");
		
		final Collection<GeneralJournal> list = incomeJournalDAL.getGeneralJournal(validDate, Utils.currentTimestamp());
		
		final Collection<GeneralJournal> revenueJournals = Lists.newArrayList();
		final Integer revenueCategory = refDataDAL.getEnumValue("JournalType", "Revenue").some().getValue();
		for (final GeneralJournal journal : list) {
			if ( refDataDAL.getGeneralJournalCategory(
					refDataDAL.getGeneralJournalItem(
							journal.getId()).some().getCategoryID()).some().getCategoryType()
								.equals(revenueCategory) ) {
				revenueJournals.add(journal);
			}
		}
		final Collection<GeneralJournal> expenseJournals = Lists.newArrayList();
		final Integer expenseCategory = refDataDAL.getEnumValue("JournalType", "Expense").some().getValue();
		for (final GeneralJournal journal : list) {
			if ( refDataDAL.getGeneralJournalCategory(
					refDataDAL.getGeneralJournalItem(
							journal.getId()).some().getCategoryID()).some().getCategoryType()
								.equals(expenseCategory) ) {
				expenseJournals.add(journal);
			}
		}
		
		
		final List<DealerIncomeExpenseFact> facts = Lists.newArrayList();
		for (final GeneralJournal journal : expenseJournals) {
			// verify report time
			Option<ReportTime> reportTime = this.getReportTime(validDate);
			if ( reportTime.isNone() ) {
				reportTime = this.addReportTime(validDate);
			} 
			
			final DealerIncomeExpenseFact fact = new DealerIncomeExpenseFact();
			fact.setAmount(journal.getAmount());
			fact.setTimeID(reportTime.some().getId());
			// verify report item here
			Option<ReportItem> reportItem = this.getReportItem(journal.getId(), "GeneralJournal");
			if ( reportItem.isNone() ) {
				reportItem = 
						this.addReportItem(journal.getId(), 
								refDataDAL.getGeneralJournalItem(journal.getId()).some().getName(), 
								"GeneralJournal", 
								refDataDAL.getGeneralJournalCategory(journal.getId()).some().getName());
			} 
			fact.setDealerID(journal.getDealerID());
			fact.setDepartmentID(journal.getDepartmentID());
			fact.setItemID(reportItem.some().getId());
			fact.setTimestamp(journal.getTimestamp());
			fact.setTimeEnd(journal.getTimeEnd());
			facts.add(fact);
		}
		
		this.saveDealerIncomeExpenseFacts(facts);
		
		final List<DealerIncomeRevenueFact> revenueFacts = Lists.newArrayList();
		for (final GeneralJournal journal : revenueJournals) {
			// verify report time
			Option<ReportTime> reportTime = this.getReportTime(validDate);
			if ( reportTime.isNone() ) {
				reportTime = this.addReportTime(validDate);
			} 
			
			final DealerIncomeRevenueFact fact = new DealerIncomeRevenueFact();
			fact.setAmount(journal.getAmount());
			fact.setMargin(BigDecimal.ZERO);
			fact.setCount(0);
			fact.setTimeID(reportTime.some().getId());
			// verify report item here
			Option<ReportItem> reportItem = this.getReportItem(journal.getId(), "GeneralJournal");
			if ( reportItem.isNone() ) {
				reportItem = 
						this.addReportItem(journal.getId(), 
								refDataDAL.getGeneralJournalItem(journal.getId()).some().getName(), 
								"GeneralJournal", 
								refDataDAL.getGeneralJournalCategory(journal.getId()).some().getName());
			} 
			fact.setDealerID(journal.getDealerID());
			fact.setDepartmentID(journal.getDepartmentID());
			fact.setItemID(reportItem.some().getId());
			fact.setTimestamp(journal.getTimestamp());
			fact.setTimeEnd(journal.getTimeEnd());
			revenueFacts.add(fact);
		}
		this.saveDealerIncomeRevenueFacts(revenueFacts);
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
	public Option<ReportItem> addReportItem(final Integer itemID, final String itemName, final String source, final String category) {
		final Session session = sessionFactory.getCurrentSession();
		final Integer reportItemSource = refDataDAL.getEnumValue("ReportItemSource", source).some().getValue();
		
		final ReportItem reportItem = new ReportItem();
		reportItem.setItemSource(reportItemSource);
		reportItem.setSourceItemID(itemID);
		reportItem.setName(itemName);
		reportItem.setItemCategory(category);
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
		if ( reportTimeList.isEmpty() ) {
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
		if ( reportItems.isEmpty() ) {
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
			if ( !list.isEmpty() ) {
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
			final Integer year, final Option<Integer> monthOfYear, final Option<Integer> departmentID) {
		Preconditions.checkNotNull(year, "year can't be null");
		final Session session = sessionFactory.getCurrentSession();
		final Collection<ReportTime> reportTimes = getReportTime(year, monthOfYear);
		if ( reportTimes.isEmpty() ) {
			return Lists.newArrayList();
		} 
		
		final List<DealerIncomeRevenueFact> facts = Lists.newArrayList();
		for (final ReportTime reportTime : reportTimes) {
			if ( departmentID.isSome() ) {
				session.enableFilter(DealerIncomeRevenueFact.FILTER_DEP)
						.setParameter("timeID", reportTime.getId())
						.setParameter("referenceTime", Utils.currentTimestamp())
						.setParameter("departmentID", departmentID.some());
	
				@SuppressWarnings("unchecked")
				final List<DealerIncomeRevenueFact> list = session.createCriteria(DealerIncomeRevenueFact.class).list();
				session.disableFilter(DealerIncomeRevenueFact.FILTER_DEP);
				facts.addAll(list);
			} else {
				session.enableFilter(DealerIncomeRevenueFact.FILTER_ALL)
							.setParameter("timeID", reportTime.getId())
							.setParameter("referenceTime", Utils.currentTimestamp()); // we are only interested in latest record
				@SuppressWarnings("unchecked")
				final List<DealerIncomeRevenueFact> list = session.createCriteria(DealerIncomeRevenueFact.class).list();
				session.disableFilter(DealerIncomeRevenueFact.FILTER_ALL);
				facts.addAll(list);
			}
		} 
		return facts;
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

	@Override
	public Collection<ReportTime> getReportTime(Integer year,
			Option<Integer> monthOfYear) {
		final Session session = sessionFactory.getCurrentSession();
		if ( monthOfYear.isNone() ) {
			session.enableFilter(ReportTime.FILTER_YEAR)
					.setParameter("year", year);

			@SuppressWarnings("unchecked")
			final List<ReportTime> list = session.createCriteria(ReportTime.class).list();
			session.disableFilter(ReportTime.FILTER_YEAR);
			return list;
		} else {
			session.enableFilter(ReportTime.FILTER_MONTH)
						.setParameter("year", year)
						.setParameter("monthOfYear", monthOfYear.some()); 
			@SuppressWarnings("unchecked")
			List<ReportTime> list = session.createCriteria(ReportTime.class).list();
			session.disableFilter(ReportTime.FILTER_MONTH);
			return list;
		}
		
	}

	private enum GetReportItemFunction implements Function<ReportItem, Long> {
	    INSTANCE;

	    @Override
	    public Long apply(ReportItem item) {
	        return item.getId();
	    }
	}
	@Override
	public Option<ReportItem> getReportItem(Long id) {
		final Map<Long, ReportItem> map = Maps.uniqueIndex(getAllReportItem(), GetReportItemFunction.INSTANCE);
		if ( !map.containsKey(id) ) {
			return Option.<ReportItem>none();
		}
		return Option.<ReportItem>some(map.get(id));
	}

	@Override
	public void saveDealerIncomeExpenseFacts(
			Collection<DealerIncomeExpenseFact> journals) {
		final Session session = sessionFactory.getCurrentSession();
		for (DealerIncomeExpenseFact newJournal: journals) {
			// check whether this journal has been inserted before
			session.enableFilter(DealerIncomeExpenseFact.FILTER)
				.setParameter("timeID", newJournal.getTimeID())
				.setParameter("itemID", newJournal.getItemID())
				.setParameter("dealerID", newJournal.getDealerID())
				.setParameter("departmentID", newJournal.getDepartmentID())
				.setParameter("referenceTime", Utils.currentTimestamp()); // we are only interested in latest record
			@SuppressWarnings("unchecked")
			final List<DealerIncomeExpenseFact> list = session.createCriteria(DealerIncomeExpenseFact.class).list();
			if ( !list.isEmpty() ) {
				for ( final DealerIncomeExpenseFact oldJournal : list ) {
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
			session.disableFilter(DealerIncomeExpenseFact.FILTER);
			
			session.flush();
		}
	}

	@Override
	public Collection<DealerIncomeExpenseFact> getDealerIncomeExpenseFacts(
			final Integer year, final Option<Integer> monthOfYear,
			final Option<Integer> departmentID) {
		Preconditions.checkNotNull(year, "year can't be null");
		final Session session = sessionFactory.getCurrentSession();
		final Collection<ReportTime> reportTimes = getReportTime(year, monthOfYear);
		if ( reportTimes.isEmpty() ) {
			return Lists.newArrayList();
		} 
		
		final List<DealerIncomeExpenseFact> facts = Lists.newArrayList();
		for (final ReportTime reportTime : reportTimes) {
			if ( departmentID.isSome() ) {
				session.enableFilter(DealerIncomeExpenseFact.FILTER_DEP)
						.setParameter("timeID", reportTime.getId())
						.setParameter("referenceTime", Utils.currentTimestamp())
						.setParameter("departmentID", departmentID.some());
	
				@SuppressWarnings("unchecked")
				final List<DealerIncomeExpenseFact> list = session.createCriteria(DealerIncomeExpenseFact.class).list();
				session.disableFilter(DealerIncomeExpenseFact.FILTER_DEP);
				facts.addAll(list);
			} else {
				session.enableFilter(DealerIncomeExpenseFact.FILTER_ALL)
							.setParameter("timeID", reportTime.getId())
							.setParameter("referenceTime", Utils.currentTimestamp()); // we are only interested in latest record
				@SuppressWarnings("unchecked")
				final List<DealerIncomeExpenseFact> list = session.createCriteria(DealerIncomeExpenseFact.class).list();
				session.disableFilter(DealerIncomeExpenseFact.FILTER_ALL);
				facts.addAll(list);
			}
		} 
		return facts;
	}

}
