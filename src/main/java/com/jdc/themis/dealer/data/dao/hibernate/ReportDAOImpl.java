package com.jdc.themis.dealer.data.dao.hibernate;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.time.calendar.LocalDate;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.lambdaj.Lambda;

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
import com.jdc.themis.dealer.domain.TaxJournal;
import com.jdc.themis.dealer.domain.VehicleSalesJournal;
import com.jdc.themis.dealer.utils.Performance;
import com.jdc.themis.dealer.utils.Utils;

import fj.P1;
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

	private final ReentrantReadWriteLock revenueFactLock = new ReentrantReadWriteLock();
	private final ReentrantReadWriteLock expenseFactLock = new ReentrantReadWriteLock();

	@Override
	@Performance
	public void importVehicleSalesJournal(final LocalDate validDate) {
		logger.info("Importing vehicle sales journal to income revenue");

		final Collection<VehicleSalesJournal> list = incomeJournalDAL
				.getVehicleSalesJournal(validDate, Utils.currentTimestamp());

		final List<DealerIncomeRevenueFact> facts = Lists.newArrayList();
		for (final VehicleSalesJournal journal : list) {
			// verify report time
			final Option<ReportTime> reportTime = this.getReportTime(validDate)
					.orElse(new P1<Option<ReportTime>>(){

						@Override
						public Option<ReportTime> _1() {
							return ReportDAOImpl.this.addReportTime(validDate);
						}
						
					});

			final DealerIncomeRevenueFact fact = new DealerIncomeRevenueFact();
			fact.setAmount(journal.getAmount());
			fact.setCount(journal.getCount());
			fact.setMargin(journal.getMargin());
			fact.setTimeID(reportTime.some().getId());
			// verify report item here
			final Option<ReportItem> reportItem = 
					this.getReportItem(journal.getId(), "VehicleSalesJournal")
					.orElse(new P1<Option<ReportItem>>() {

						@Override
						public Option<ReportItem> _1() {
							logger.debug("saving journal {}", journal);
							return ReportDAOImpl.this
									.addReportItem(
											journal.getId(),
											refDataDAL.getVehicle(journal.getId()).some()
													.getName(),
											"VehicleSalesJournal",
											refDataDAL
													.getSalesServiceJournalCategory(
															refDataDAL
															.getVehicle(
																	journal.getId())
															.some()
															.getCategoryID()).some()
													.getName());
						}
						
					});
			
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

		final Integer revenueJournalType = refDataDAL
				.getEnumValue("JournalType", "Revenue").some().getValue();
		final Integer expenseJournalType = refDataDAL
				.getEnumValue("JournalType", "Expense").some().getValue();

		final Collection<SalesServiceJournal> list = incomeJournalDAL
				.getSalesServiceJournal(validDate, Utils.currentTimestamp());

		final List<DealerIncomeRevenueFact> revenueFacts = Lists.newArrayList();
		final List<DealerIncomeExpenseFact> expenseFacts = Lists.newArrayList();
		for (final SalesServiceJournal journal : list) {
			// verify report time
			final Option<ReportTime> reportTime = this.getReportTime(validDate).orElse(new P1<Option<ReportTime>>(){

				@Override
				public Option<ReportTime> _1() {
					return ReportDAOImpl.this.addReportTime(validDate);
				}
				
			});
			logger.debug("saving journal {}", journal);
			if (revenueJournalType.equals(refDataDAL
					.getSalesServiceJournalItem(journal.getId()).some()
					.getJournalType())) {
				final DealerIncomeRevenueFact fact = new DealerIncomeRevenueFact();
				fact.setAmount(journal.getAmount());
				fact.setCount(journal.getCount());
				fact.setMargin(journal.getMargin());
				fact.setTimeID(reportTime.some().getId());
				// verify report item here
				final Option<ReportItem> reportItem = this.getReportItem(
						journal.getId(), "SalesServiceJournal")
						.orElse(new P1<Option<ReportItem>>() {

							@Override
							public Option<ReportItem> _1() {
								return ReportDAOImpl.this
										.addReportItem(
												journal.getId(),
												refDataDAL
														.getSalesServiceJournalItem(
																journal.getId()).some()
														.getName(),
												"SalesServiceJournal",
												refDataDAL
														.getSalesServiceJournalCategory(
																refDataDAL
																		.getSalesServiceJournalItem(
																				journal.getId())
																		.some()
																		.getCategoryID())
														.some().getName());
							}
							
						});

				fact.setDealerID(journal.getDealerID());
				fact.setDepartmentID(journal.getDepartmentID());
				fact.setItemID(reportItem.some().getId());
				fact.setTimestamp(journal.getTimestamp());
				fact.setTimeEnd(journal.getTimeEnd());
				revenueFacts.add(fact);
			}
			if (expenseJournalType.equals(refDataDAL
					.getSalesServiceJournalItem(journal.getId()).some()
					.getJournalType())) {
				final DealerIncomeExpenseFact fact = new DealerIncomeExpenseFact();
				fact.setAmount(journal.getMargin()); // amount and count would
														// be zero for a expense
														// item
				fact.setTimeID(reportTime.some().getId());
				// verify report item here
				final Option<ReportItem> reportItem = this.getReportItem(
						journal.getId(), "SalesServiceJournal")
						.orElse(new P1<Option<ReportItem>>() {

							@Override
							public Option<ReportItem> _1() {
								return ReportDAOImpl.this
										.addReportItem(
												journal.getId(),
												refDataDAL
														.getSalesServiceJournalItem(
																journal.getId()).some()
														.getName(),
												"SalesServiceJournal",
												refDataDAL
														.getSalesServiceJournalCategory(
																refDataDAL
																		.getSalesServiceJournalItem(
																				journal.getId())
																		.some()
																		.getCategoryID())
														.some().getName());
							}
							
						});
				
				fact.setDealerID(journal.getDealerID());
				fact.setDepartmentID(journal.getDepartmentID());
				fact.setItemID(reportItem.some().getId());
				fact.setTimestamp(journal.getTimestamp());
				fact.setTimeEnd(journal.getTimeEnd());
				expenseFacts.add(fact);
			}

		}
		this.saveDealerIncomeRevenueFacts(revenueFacts);
		this.saveDealerIncomeExpenseFacts(expenseFacts);
	}

	@Override
	public void importGeneralJournal(final LocalDate validDate) {
		logger.info("Importing general journal to income revenue and expense");

		final Collection<GeneralJournal> list = incomeJournalDAL
				.getGeneralJournal(validDate, Utils.currentTimestamp());

		final Collection<GeneralJournal> revenueJournals = Lists.newArrayList();
		final Integer revenueJournalType = refDataDAL
				.getEnumValue("JournalType", "Revenue").some().getValue();
		for (final GeneralJournal journal : list) {
			if (refDataDAL.getGeneralJournalItem(journal.getId()).some()
					.getJournalType().equals(revenueJournalType)) {
				revenueJournals.add(journal);
			}
		}
		final Collection<GeneralJournal> expenseJournals = Lists.newArrayList();
		final Integer expenseJournalType = refDataDAL
				.getEnumValue("JournalType", "Expense").some().getValue();
		for (final GeneralJournal journal : list) {
			if (refDataDAL.getGeneralJournalItem(journal.getId()).some()
					.getJournalType().equals(expenseJournalType)) {
				expenseJournals.add(journal);
			}
		}

		final List<DealerIncomeExpenseFact> facts = Lists.newArrayList();
		for (final GeneralJournal journal : expenseJournals) {
			// verify report time
			final Option<ReportTime> reportTime = 
					this.getReportTime(validDate).orElse(new P1<Option<ReportTime>>() {

						@Override
						public Option<ReportTime> _1() {
							return ReportDAOImpl.this.addReportTime(validDate);
						}
						
					});

			final DealerIncomeExpenseFact fact = new DealerIncomeExpenseFact();
			fact.setAmount(journal.getAmount());
			fact.setTimeID(reportTime.some().getId());
			// verify report item here
			final Option<ReportItem> reportItem = this.getReportItem(journal.getId(),
					"GeneralJournal").orElse(new P1<Option<ReportItem>>() {

						@Override
						public Option<ReportItem> _1() {
							return ReportDAOImpl.this.addReportItem(
									journal.getId(),
									refDataDAL.getGeneralJournalItem(journal.getId())
											.some().getName(),
									"GeneralJournal",
									refDataDAL
											.getGeneralJournalCategory(
													refDataDAL
															.getGeneralJournalItem(
																	journal.getId()).some()
															.getCategoryID()).some()
											.getName());
						}
						
					});
		    
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
			final Option<ReportTime> reportTime = 
					this.getReportTime(validDate).orElse(new P1<Option<ReportTime>>() {

				@Override
				public Option<ReportTime> _1() {
					return ReportDAOImpl.this.addReportTime(validDate);
				}
				
			});

			final DealerIncomeRevenueFact fact = new DealerIncomeRevenueFact();
			fact.setAmount(journal.getAmount());
			fact.setMargin(BigDecimal.ZERO);
			fact.setCount(0);
			fact.setTimeID(reportTime.some().getId());
			// verify report item here
			final Option<ReportItem> reportItem = this.getReportItem(journal.getId(),
					"GeneralJournal").orElse(new P1<Option<ReportItem>>() {

						@Override
						public Option<ReportItem> _1() {
							return ReportDAOImpl.this.addReportItem(
									journal.getId(),
									refDataDAL.getGeneralJournalItem(journal.getId())
											.some().getName(),
									"GeneralJournal",
									refDataDAL
											.getGeneralJournalCategory(
													refDataDAL
															.getGeneralJournalItem(
																	journal.getId()).some()
															.getCategoryID()).some()
											.getName());
						}
						
					});

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
		return Option.<ReportTime> some(time);
	}

	@Override
	public Option<ReportItem> addReportItem(final Integer itemID,
			final String itemName, final String source, final String category) {
		final Session session = sessionFactory.getCurrentSession();
		final Integer reportItemSource = refDataDAL
				.getEnumValue("ReportItemSource", source).some().getValue();

		final ReportItem reportItem = new ReportItem();
		reportItem.setItemSource(reportItemSource);
		reportItem.setSourceItemID(itemID);
		reportItem.setName(itemName);
		reportItem.setItemCategory(category);
		session.save(reportItem);
		session.flush();
		return Option.<ReportItem> some(reportItem);
	}

	@Override
	public Option<ReportTime> getReportTime(final LocalDate validDate) {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<ReportTime> reportTimeList = session
				.createCriteria(ReportTime.class)
				.add(Restrictions.eq("validDate", validDate)).list();
		return Option.<ReportTime>iif(!reportTimeList.isEmpty(), new P1<ReportTime>() {

			@Override
			public ReportTime _1() {
				return reportTimeList.get(0);
			}
			
		});
	}

	@Override
	public Option<ReportItem> getReportItem(final Integer itemID,
			final String source) {
		final Session session = sessionFactory.getCurrentSession();
		final Integer reportItemSource = refDataDAL
				.getEnumValue("ReportItemSource", source).some().getValue();
		@SuppressWarnings("unchecked")
		final List<ReportItem> reportItems = session
				.createCriteria(ReportItem.class)
				.add(Restrictions.eq("sourceItemID", itemID))
				.add(Restrictions.eq("itemSource", reportItemSource)).list();
		
		return Option.<ReportItem>iif(!reportItems.isEmpty(), new P1<ReportItem>() {

			@Override
			public ReportItem _1() {
				return reportItems.get(0);
			}
			
		});
	}

	@Override
	public void saveDealerIncomeRevenueFacts(
			final Collection<DealerIncomeRevenueFact> journals) {
		revenueFactLock.writeLock().lock();
		try {
			final Session session = sessionFactory.getCurrentSession();
			for (DealerIncomeRevenueFact newJournal : journals) {
				// check whether this journal has been inserted before
				session.enableFilter(DealerIncomeRevenueFact.FILTER)
						.setParameter("timeID", newJournal.getTimeID())
						.setParameter("itemID", newJournal.getItemID())
						.setParameter("dealerID", newJournal.getDealerID())
						.setParameter("departmentID",
								newJournal.getDepartmentID())
						.setParameter("referenceTime", Utils.currentTimestamp()); // we
																					// are
																					// only
																					// interested
																					// in
																					// latest
																					// record
				@SuppressWarnings("unchecked")
				final List<DealerIncomeRevenueFact> list = session
						.createCriteria(DealerIncomeRevenueFact.class).list();
				if (!list.isEmpty()) {
					for (final DealerIncomeRevenueFact oldJournal : list) {
						// if we get here, it means we have inserted this fact
						// before
						if (oldJournal.getTimestamp().isBefore(
								newJournal.getTimestamp())) {
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
		} finally {
			revenueFactLock.writeLock().unlock();
		}
	}

	@Override
	public Collection<ReportTime> getAllReportTime() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<ReportTime> list = session.createCriteria(ReportTime.class)
				.list();
		return ImmutableList.copyOf(list);
	}

	@Override
	public Collection<ReportItem> getAllReportItem() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<ReportItem> list = session.createCriteria(ReportItem.class)
				.list();
		return ImmutableList.copyOf(list);
	}

	@Override
	public Collection<ReportTime> getReportTime(Integer year,
			Option<Integer> monthOfYear) {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria criteria = session.createCriteria(ReportTime.class);
		criteria.add(Restrictions.eq("year", year));
		if (monthOfYear.isSome()) {
			criteria.add(Restrictions.eq("monthOfYear", monthOfYear.some()));
		}
		@SuppressWarnings("unchecked")
		final List<ReportTime> list = criteria.list();
		return list;
	}
	public Collection<ReportTime> getReportTime(Integer year,
			Collection<Integer> monthOfYear) {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria criteria = session.createCriteria(ReportTime.class);
		criteria.add(Restrictions.eq("year", year));
		if ( !monthOfYear.isEmpty() ) {
			criteria.add(Restrictions.in("monthOfYear", monthOfYear));
		} 
		@SuppressWarnings("unchecked")
		final List<ReportTime> list = criteria.list();
		return list;
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
		final Map<Long, ReportItem> map = Maps.uniqueIndex(getAllReportItem(),
				GetReportItemFunction.INSTANCE);
		
		return Option.<ReportItem>iif(map.containsKey(id), map.get(id));
	}

	@Override
	public void saveDealerIncomeExpenseFacts(
			Collection<DealerIncomeExpenseFact> journals) {
		expenseFactLock.writeLock().lock();
		try {
			final Session session = sessionFactory.getCurrentSession();
			for (DealerIncomeExpenseFact newJournal : journals) {
				// check whether this journal has been inserted before
				session.enableFilter(DealerIncomeExpenseFact.FILTER)
						.setParameter("timeID", newJournal.getTimeID())
						.setParameter("itemID", newJournal.getItemID())
						.setParameter("dealerID", newJournal.getDealerID())
						.setParameter("departmentID",
								newJournal.getDepartmentID())
						.setParameter("referenceTime", Utils.currentTimestamp()); // we
																					// are
																					// only
																					// interested
																					// in
																					// latest
																					// record
				@SuppressWarnings("unchecked")
				final List<DealerIncomeExpenseFact> list = session
						.createCriteria(DealerIncomeExpenseFact.class).list();
				if (!list.isEmpty()) {
					for (final DealerIncomeExpenseFact oldJournal : list) {
						// if we get here, it means we have inserted this fact
						// before
						if (oldJournal.getTimestamp().isBefore(
								newJournal.getTimestamp())) {
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
		} finally {
			expenseFactLock.writeLock().unlock();
		}
	}

	@Override
	public void importTaxJournal(final LocalDate validDate) {
		logger.info("Importing tax journal to income expense");

		final Collection<TaxJournal> list = incomeJournalDAL.getTaxJournal(
				validDate, Utils.currentTimestamp());

		final List<DealerIncomeExpenseFact> facts = Lists.newArrayList();
		for (final TaxJournal journal : list) {
			// verify report time
			final Option<ReportTime> reportTime = 
					this.getReportTime(validDate).orElse(new P1<Option<ReportTime>>() {

						@Override
						public Option<ReportTime> _1() {
							return ReportDAOImpl.this.addReportTime(validDate);
						}
						
					});

			final DealerIncomeExpenseFact fact = new DealerIncomeExpenseFact();
			fact.setAmount(journal.getAmount());
			fact.setTimeID(reportTime.some().getId());
			// verify report item here
			final Option<ReportItem> reportItem = this.getReportItem(journal.getId(),
					"TaxJournal").orElse(new P1<Option<ReportItem>>() {

						@Override
						public Option<ReportItem> _1() {
							return ReportDAOImpl.this.addReportItem(
									journal.getId(),
									refDataDAL.getTaxJournalItem(journal.getId()).some()
											.getName(),
									"TaxJournal",
									null);
						}
						
					});

			fact.setDealerID(journal.getDealerID());
			fact.setItemID(reportItem.some().getId());
			fact.setTimestamp(journal.getTimestamp());
			fact.setTimeEnd(journal.getTimeEnd());
			facts.add(fact);
		}
		this.saveDealerIncomeExpenseFacts(facts);
	}

	@Override
	@Performance
	public Collection<DealerIncomeExpenseFact> getDealerIncomeExpenseFacts(
			Integer year, Collection<Integer> monthOfYear,
			Collection<Integer> departmentID, Collection<Integer> itemSource,
			Collection<String> itemCategory, Collection<Integer> itemID) {
		Preconditions.checkNotNull(year, "year can't be null");
		expenseFactLock.readLock().lock();
		try {
			final Session session = sessionFactory.getCurrentSession();
			final Collection<ReportTime> reportTimes = getReportTime(year,
					monthOfYear);
			if (reportTimes.isEmpty()) {
				return Lists.newArrayList();
			}

			final List<DealerIncomeExpenseFact> facts = Lists.newArrayList();
			session.enableFilter(DealerIncomeExpenseFact.FILTER_REFTIME)
					.setParameter("referenceTime", Utils.currentTimestamp());

			final Criteria criteria = session
					.createCriteria(DealerIncomeExpenseFact.class);
			criteria.add(Restrictions.in("timeID",
					Lambda.extractProperty(reportTimes, "id")));
			if (!departmentID.isEmpty()) {
				criteria.add(Restrictions.in("departmentID",
						departmentID));
			}
			if ( !itemID.isEmpty() ) {
				criteria.add(Restrictions.in("itemID",
					itemID));
			}
			
			@SuppressWarnings("unchecked")
			final List<DealerIncomeExpenseFact> list = criteria.list();
			session.disableFilter(DealerIncomeExpenseFact.FILTER_REFTIME);

			for (final DealerIncomeExpenseFact fact : list) {
				final ReportItem item = getReportItem(fact.getItemID()).some();
				if (!itemCategory.isEmpty()
						&& !itemCategory.contains(item.getItemCategory())) {
					continue;
				}
				if (!itemSource.isEmpty()
						&& !itemSource.contains(item.getItemSource())) {
					continue;
				}
				facts.add(fact);
			}
			return facts;
		} finally {
			expenseFactLock.readLock().unlock();
		}
	}
	
	@Override
	@Performance
	public Collection<DealerIncomeRevenueFact> getDealerIncomeRevenueFacts(
			Integer year, Collection<Integer> monthOfYear,
			Collection<Integer> departmentID, Collection<Integer> itemSource,
			Collection<String> itemCategory, Collection<Integer> itemID) {
		Preconditions.checkNotNull(year, "year can't be null");
		expenseFactLock.readLock().lock();
		try {
			final Session session = sessionFactory.getCurrentSession();
			final Collection<ReportTime> reportTimes = getReportTime(year,
					monthOfYear);
			if (reportTimes.isEmpty()) {
				return Lists.newArrayList();
			}

			final List<DealerIncomeRevenueFact> facts = Lists.newArrayList();
			session.enableFilter(DealerIncomeRevenueFact.FILTER_REFTIME)
					.setParameter("referenceTime", Utils.currentTimestamp());

			final Criteria criteria = session
					.createCriteria(DealerIncomeRevenueFact.class);
			criteria.add(Restrictions.in("timeID",
					Lambda.extractProperty(reportTimes, "id")));
			if ( !departmentID.isEmpty() ) {
				criteria.add(Restrictions.in("departmentID",
						departmentID));
			}
			if ( !itemID.isEmpty() ) {
				criteria.add(Restrictions.in("itemID",
					itemID));
			}
			
			@SuppressWarnings("unchecked")
			final List<DealerIncomeRevenueFact> list = criteria.list();
			session.disableFilter(DealerIncomeRevenueFact.FILTER_REFTIME);

			for (final DealerIncomeRevenueFact fact : list) {
				final ReportItem item = getReportItem(fact.getItemID()).some();
				if (!itemCategory.isEmpty()
						&& !itemCategory.contains(item.getItemCategory())) {
					continue;
				}
				if (!itemSource.isEmpty()
						&& !itemSource.contains(item.getItemSource())) {
					continue;
				}
				facts.add(fact);
			}
			return facts;
		} finally {
			expenseFactLock.readLock().unlock();
		}
	}

}
