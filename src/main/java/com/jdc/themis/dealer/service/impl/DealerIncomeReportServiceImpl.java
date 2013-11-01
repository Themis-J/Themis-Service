package com.jdc.themis.dealer.service.impl;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.time.calendar.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimaps;
import com.jdc.themis.dealer.data.dao.ReportDAO;
import com.jdc.themis.dealer.domain.DealerIncomeExpenseFact;
import com.jdc.themis.dealer.domain.DealerIncomeRevenueFact;
import com.jdc.themis.dealer.report.DealerReportCalculator;
import com.jdc.themis.dealer.report.DealerSalesReportCalculator;
import com.jdc.themis.dealer.report.DepartmentReportCalculator;
import com.jdc.themis.dealer.report.JournalOp;
import com.jdc.themis.dealer.service.DealerIncomeReportService;
import com.jdc.themis.dealer.service.RefDataQueryService;
import com.jdc.themis.dealer.utils.Performance;
import com.jdc.themis.dealer.web.domain.ImportReportDataRequest;
import com.jdc.themis.dealer.web.domain.QueryDealerExpensePercentageResponse;
import com.jdc.themis.dealer.web.domain.QueryDealerIncomeResponse;
import com.jdc.themis.dealer.web.domain.QueryDealerSalesResponse;
import com.jdc.themis.dealer.web.domain.QueryDepartmentIncomeResponse;
import com.jdc.themis.dealer.web.domain.ReportDealerDataList;
import com.jdc.themis.dealer.web.domain.ReportDealerSalesDataList;
import com.jdc.themis.dealer.web.domain.ReportDepartmentDataList;

import fj.data.Option;

@Service
public class DealerIncomeReportServiceImpl implements DealerIncomeReportService {
	private final static Logger logger = LoggerFactory
			.getLogger(DealerIncomeReportServiceImpl.class);

	@Autowired
	private ReportDAO reportDAL;
	@Autowired
	private RefDataQueryService refDataDAL;

	// add this for test
	public void setRefDataDAL(RefDataQueryService refDataDAL) {
		this.refDataDAL = refDataDAL;
	}
	// add this for test
	public void setReportDAL(ReportDAO reportDAL) {
		this.reportDAL = reportDAL;
	}

	/**
	 * Import report data by given dates.
	 * 
	 * User can provide a range of dates, i.e. 'fromDate' and 'toDate'.
	 */
	@Override
	@Performance
	public void importReportData(final ImportReportDataRequest request) {
		Preconditions.checkNotNull(request.getFromDate(),
				"from date can't be null");
		Preconditions
				.checkNotNull(request.getToDate(), "to date can't be null");
		final Calendar c = new GregorianCalendar();
		c.setTime(new Date());
		final LocalDate today = LocalDate.of(c.get(Calendar.YEAR),
				c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
		Preconditions.checkArgument(!LocalDate.parse(request.getToDate())
				.isAfter(today), "cannot import future date");

		final LocalDate fromDate = LocalDate.parse(request.getFromDate());
		int counter = 0;
		while (true) {
			final LocalDate currentDate = fromDate.plusMonths(counter);
			if (currentDate.isAfter(LocalDate.parse(request.getToDate()))) {
				break;
			}
			logger.debug("importing report data for date {}", currentDate);
			reportDAL.importVehicleSalesJournal(currentDate);
			reportDAL.importSalesServiceJournal(currentDate);
			reportDAL.importGeneralJournal(currentDate);
			reportDAL.importTaxJournal(currentDate);
			reportDAL.importHRAllocation(currentDate);
			counter++;
		}
	}

	/**
	 * Query overall income report data. 
	 */
	@Override
	@Performance
	public QueryDealerIncomeResponse queryOverallIncomeReport(final Integer year,
			final Option<Integer> monthOfYear,
			final Option<Integer> departmentID,
			final Option<Integer> denominator) {
		Preconditions.checkNotNull(year, "year can't be null");
		final QueryDealerIncomeResponse response = new QueryDealerIncomeResponse();
		response.setReportName("OverallIncomeReport");

		if (monthOfYear.isNone()) {
			int previousYear = year - 1;
			final ReportDealerDataList reportDetailPreviousYear = getDealerReportDataDetail(
					previousYear, Option.<Integer> none(),
					Option.<ReportDealerDataList> none(), departmentID,
					denominator, JournalOp.SUM);
			reportDetailPreviousYear.setYear(previousYear);
			response.getDetail().add(reportDetailPreviousYear);

			final ReportDealerDataList reportDetailCurrentYear = getDealerReportDataDetail(
					year, Option.<Integer> none(),
					Option.<ReportDealerDataList> some(reportDetailPreviousYear),
					departmentID, denominator, JournalOp.SUM);
			reportDetailCurrentYear.setYear(year);
			response.getDetail().add(reportDetailCurrentYear);
		} else {
			final ReportDealerDataList reportDetailMonthlyAvg = getDealerReportDataDetail(
					year, monthOfYear, Option.<ReportDealerDataList> none(),
					departmentID, denominator, JournalOp.AVG);
			response.getDetail().add(reportDetailMonthlyAvg);

			final ReportDealerDataList reportDetailCurrentMonth = getDealerReportDataDetail(
					year, monthOfYear,
					Option.<ReportDealerDataList> some(reportDetailMonthlyAvg),
					departmentID, denominator, JournalOp.SUM);
			response.getDetail().add(reportDetailCurrentMonth);
		}
		return response;
	}

	@Override
	public QueryDealerExpensePercentageResponse queryOverallExpensePercentageReport(
			final Integer year, final Integer monthOfYear, final Option<Integer> denominator, final Option<Integer> categoryID, final Option<Integer> itemID) {
		Preconditions.checkNotNull(year, "year can't be null");
		final QueryDealerExpensePercentageResponse response = new QueryDealerExpensePercentageResponse();
		response.setReportName("OverallExpensePercentageReport");
		// TODO:
		return response;
	}

	/**
	 * Query department income report data.
	 */
	@Override
	@Performance
	public QueryDepartmentIncomeResponse queryDepartmentIncomeReport(
			final Integer year, final Option<Integer> monthOfYear, 
			final Option<Integer> dealerID, final Option<Integer> departmentID) {
		Preconditions.checkNotNull(year, "year can't be null");
		final QueryDepartmentIncomeResponse response = new QueryDepartmentIncomeResponse();
		response.setReportName("DepartmentIncomeReport");

		if (monthOfYear.isNone()) {
			int previousYear = year - 1;
			final ReportDepartmentDataList reportDetailPreviousYear = getDepartmentReportDataDetail(
					previousYear, dealerID, departmentID, Option.<Integer> none(),
					Option.<ReportDepartmentDataList> none(), JournalOp.SUM);
			reportDetailPreviousYear.setYear(previousYear);
			response.getDetail().add(reportDetailPreviousYear);
			final ReportDepartmentDataList reportDetailCurrentYear = getDepartmentReportDataDetail(
					year, dealerID, departmentID, Option.<Integer> none(),
					Option.<ReportDepartmentDataList> some(reportDetailPreviousYear),
					JournalOp.SUM);
			reportDetailCurrentYear.setYear(year);
			response.getDetail().add(reportDetailCurrentYear);
		} else {
			final ReportDepartmentDataList reportDetailMonthlyAvg = getDepartmentReportDataDetail(
					year, dealerID, departmentID, monthOfYear,
					Option.<ReportDepartmentDataList> none(), JournalOp.AVG);
			response.getDetail().add(reportDetailMonthlyAvg);

			final ReportDepartmentDataList reportDetailCurrentMonth = getDepartmentReportDataDetail(
					year, dealerID, departmentID, monthOfYear,
					Option.<ReportDepartmentDataList> some(reportDetailMonthlyAvg),
					JournalOp.SUM);
			response.getDetail().add(reportDetailCurrentMonth);
		}
		return response;
	}

	/**
	 * Query sales report data. 
	 */
	@Override
	@Performance
	public QueryDealerSalesResponse queryDealerSalesReport(final Integer year,
			final Option<Integer> monthOfYear,
			final Option<Integer> departmentID) {
		Preconditions.checkNotNull(year, "year can't be null");
		final QueryDealerSalesResponse response = new QueryDealerSalesResponse();
		response.setReportName("SalesReport");

		if (monthOfYear.isNone()) {
			int previousYear = year - 1;
			final ReportDealerSalesDataList reportDetailPreviousYear = getDealerSalesReportDataDetail(
					previousYear, Option.<Integer> none(),
					Option.<ReportDealerSalesDataList> none(), departmentID,
					JournalOp.SUM);
			reportDetailPreviousYear.setYear(previousYear);
			response.getDetail().add(reportDetailPreviousYear);

			final ReportDealerSalesDataList reportDetailCurrentYear = getDealerSalesReportDataDetail(
					year, Option.<Integer> none(),
					Option.<ReportDealerSalesDataList> some(reportDetailPreviousYear),
					departmentID, JournalOp.SUM);
			reportDetailCurrentYear.setYear(year);
			response.getDetail().add(reportDetailCurrentYear);
		} else {
			final ReportDealerSalesDataList reportDetailMonthlyAvg = getDealerSalesReportDataDetail(
					year, monthOfYear, Option.<ReportDealerSalesDataList> none(),
					departmentID, JournalOp.AVG);
			response.getDetail().add(reportDetailMonthlyAvg);

			final ReportDealerSalesDataList reportDetailCurrentMonth = getDealerSalesReportDataDetail(
					year, monthOfYear,
					Option.<ReportDealerSalesDataList> some(reportDetailMonthlyAvg),
					departmentID, JournalOp.SUM);
			response.getDetail().add(reportDetailCurrentMonth);
		}
		return response;
	}

	/** Private functions **/
	
	private ReportDepartmentDataList getDepartmentReportDataDetail(final Integer year,
			final Option<Integer> dealerIDOption,
			final Option<Integer> departmentIDOption,
			final Option<Integer> monthOfYearOption,
			final Option<ReportDepartmentDataList> previousDetailOption,
			final JournalOp op) {
		final DepartmentReportCalculator calculator = new DepartmentReportCalculator(
				refDataDAL.getDepartments().getItems(), year)
				.withMonth(monthOfYearOption)
				.withPrevious(previousDetailOption);

		// Get all revenues
		final DealerIncomeFactsQueryBuilder queryBuilder = 
				new DealerIncomeFactsQueryBuilder(reportDAL);
		queryBuilder.withYear(year);
		if ( monthOfYearOption.isSome() ) {
			if ( JournalOp.AVG.equals(op) ) {
				queryBuilder.withLessThanMonthOfYear(monthOfYearOption.some());
			} else {
				queryBuilder.withMonthOfYear(monthOfYearOption.some());
			} 
		} 
		if ( departmentIDOption.isSome() ) {
			queryBuilder.withDepartmentID(departmentIDOption.some());
		}
		if ( dealerIDOption.isSome() ) {
			queryBuilder.withDealerID(dealerIDOption.some());
		}
		final Collection<DealerIncomeRevenueFact> revenueFacts =
				queryBuilder.withItemCategory("新轿车零售")
								.withItemCategory("新货车零售")
								.withItemCategory("附加产品业务")
								.withItemCategory("二手车零售")
								.withItemCategory("维修收入")
								.withItemCategory("配件收入")
								.withItemCategory("钣喷收入")
								.withItemCategory("新车其它收入")
								.withItemCategory("二手车其它收入")
								.withItemCategory("维修其它收入")
								.withItemCategory("钣喷其它收入")
								.withItemCategory("租恁收入").queryRevenues();
		
		final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFacts = Multimaps
				.index(revenueFacts,
						GetDepartmentIDFromRevenueFunction.INSTANCE);
		// Get all expenses
		final Collection<DealerIncomeExpenseFact> expenseFacts =
				queryBuilder.clear().withItemCategory("变动费用")
								.withItemCategory("销售费用")
								.withItemCategory("人工费用")
								.withItemCategory("半固定费用")
								.withItemCategory("固定费用").queryExpenses();

		final ImmutableListMultimap<Integer, DealerIncomeExpenseFact> dealerExpenseFacts = Multimaps
				.index(expenseFacts,
						GetDepartmentIDFromExpenseFunction.INSTANCE);

		calculator.calcRevenues(dealerRevenueFacts, op)
				.calcMargins(dealerRevenueFacts, op)
				.calcExpenses(dealerExpenseFacts, op).calcOpProfit();

		return calculator.getReportDetail();
	}

	private ReportDealerSalesDataList getDealerSalesReportDataDetail(final Integer year,
			final Option<Integer> monthOfYearOption,
			final Option<ReportDealerSalesDataList> previousDetailOption,
			final Option<Integer> departmentIDOption, final JournalOp op) {
		final DealerSalesReportCalculator calculator = new DealerSalesReportCalculator(
				refDataDAL.getDealers().getItems(), year)
				.withMonth(monthOfYearOption)
				.withPrevious(previousDetailOption);

		final DealerIncomeFactsQueryBuilder revenueFactsQueryBuilder = 
				new DealerIncomeFactsQueryBuilder(reportDAL);
		revenueFactsQueryBuilder.withYear(year);
		if ( monthOfYearOption.isSome() ) {
			if ( JournalOp.AVG.equals(op) ) {
				revenueFactsQueryBuilder.withLessThanMonthOfYear(monthOfYearOption.some());
			} else {
				revenueFactsQueryBuilder.withMonthOfYear(monthOfYearOption.some());
			} 
		} 
		if ( departmentIDOption.isSome() ) {
			revenueFactsQueryBuilder.withDepartmentID(departmentIDOption.some());
		}
		final Collection<DealerIncomeRevenueFact> overallSalesFacts = revenueFactsQueryBuilder.withItemCategory("新轿车零售")
										.withItemCategory("新货车零售")
										.withItemCategory("附加产品业务")
										.withItemCategory("二手车零售")
										.withItemCategory("新车其它收入")
										.withItemCategory("二手车其它收入").queryRevenues();
		
		final Collection<DealerIncomeRevenueFact> retailFacts = revenueFactsQueryBuilder.clear()
								.withItemCategory("新轿车零售")
								.withItemCategory("新货车零售").queryRevenues();
		
		final Collection<DealerIncomeRevenueFact> wholesaleFacts = revenueFactsQueryBuilder.clear()
								.withItemCategory("新车其它收入")
								.withItemCategory("二手车其它收入")
								.withItemID(refDataDAL
									.getSalesServiceRevenueItem(
										"大客户采购（租车公司，政府机关）",
										"新车其它收入").getId().longValue())
								.withItemID(refDataDAL
										.getSalesServiceRevenueItem("批发销售",
										"二手车其它收入").getId().longValue()).queryRevenues();

		final Collection<DealerIncomeRevenueFact> otherFacts = revenueFactsQueryBuilder.clear()
								.withItemCategory("新车其它收入")
								.withItemID(refDataDAL
										.getSalesServiceRevenueItem("他店调车",
												"新车其它收入").getId().longValue()).queryRevenues();

		calculator
				.calcOverall(
						Multimaps.index(overallSalesFacts,
								GetDealerIDFromRevenueFunction.INSTANCE), op)
				.calcRetail(
						Multimaps.index(retailFacts,
								GetDealerIDFromRevenueFunction.INSTANCE), op)
				.calcWholesale(
						Multimaps.index(wholesaleFacts,
								GetDealerIDFromRevenueFunction.INSTANCE), op)
				.calcOther(
						Multimaps.index(otherFacts,
								GetDealerIDFromRevenueFunction.INSTANCE), op);

		return calculator.getReportDetail();
	}

	private ReportDealerDataList getDealerReportDataDetail(final Integer year,
			final Option<Integer> monthOfYearOption,
			final Option<ReportDealerDataList> previousDetailOption,
			final Option<Integer> departmentIDOption,
			final Option<Integer> denominatorIDOption, final JournalOp op) {
		final DealerReportCalculator calculator = new DealerReportCalculator(
				refDataDAL.getDealers().getItems(), year)
					.withMonth(monthOfYearOption)
					.withPrevious(previousDetailOption)
					.withDenominator(denominatorIDOption);

		// Get all revenues
		final DealerIncomeFactsQueryBuilder queryBuilder = 
				new DealerIncomeFactsQueryBuilder(reportDAL).withYear(year);
		if ( monthOfYearOption.isSome() ) {
			if ( JournalOp.AVG.equals(op) ) {
				queryBuilder.withLessThanMonthOfYear(monthOfYearOption.some());
			} else {
				queryBuilder.withMonthOfYear(monthOfYearOption.some());
			} 
		} 
		if ( departmentIDOption.isSome() ) {
			queryBuilder.withDepartmentID(departmentIDOption.some());
		}
		final Collection<DealerIncomeRevenueFact> revenueFacts = queryBuilder.withItemCategory("新轿车零售")
								.withItemCategory("新货车零售")
								.withItemCategory("附加产品业务")
								.withItemCategory("二手车零售")
								.withItemCategory("维修收入")
								.withItemCategory("配件收入")
								.withItemCategory("钣喷收入")
								.withItemCategory("新车其它收入")
								.withItemCategory("二手车其它收入")
								.withItemCategory("维修其它收入")
								.withItemCategory("钣喷其它收入")
								.withItemCategory("租恁收入").queryRevenues();

		final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFacts = Multimaps
				.index(revenueFacts, GetDealerIDFromRevenueFunction.INSTANCE);
		// Get all expenses
		final Collection<DealerIncomeExpenseFact> expenseFacts = 
												queryBuilder.clear() // clear the builder for next query
															.withItemCategory("变动费用")
															.withItemCategory("销售费用")
															.withItemCategory("人工费用")
															.withItemCategory("半固定费用")
															.withItemCategory("固定费用")
															.queryExpenses();

		final ImmutableListMultimap<Integer, DealerIncomeExpenseFact> dealerExpenseFacts = Multimaps
				.index(expenseFacts, GetDealerIDFromExpenseFunction.INSTANCE);

		calculator.calcRevenues(dealerRevenueFacts, op)
				.calcMargins(dealerRevenueFacts, op)
				.calcExpenses(dealerExpenseFacts, op).calcOpProfit();

		if (departmentIDOption.isNone()) {
			// calculate net profit
			final DealerIncomeFactsQueryBuilder otherQueryBuilder = 
					new DealerIncomeFactsQueryBuilder(reportDAL).withYear(year);
			if ( monthOfYearOption.isSome() ) {
				otherQueryBuilder.withMonthOfYear(monthOfYearOption.some());
			} 
			final Collection<DealerIncomeRevenueFact> otherRevenueFacts = otherQueryBuilder.withItemCategory("非经营性损益进项")
									.withItemCategory("非销售类返利").queryRevenues();

			final Collection<DealerIncomeExpenseFact> otherExpenseFacts = otherQueryBuilder.clear()// clear the builder for next query
								.withItemCategory("非经营性损益削项")
								.withItemCategory("员工分红").queryExpenses();
			
			calculator.calcNetProfit(Multimaps.index(otherRevenueFacts,GetDealerIDFromRevenueFunction.INSTANCE),
									Multimaps.index(otherExpenseFacts,GetDealerIDFromExpenseFunction.INSTANCE), op);
		}
		if (denominatorIDOption.isSome()) {
			calculator.prepareDenominators().adjustExpenseByDenominator()
					.adjustMarginByDenominator().adjustNetProfitByDenominator()
					.adjustOpProfitByDenominator();
		}
		return calculator.getReportDetail();
	}
	
}
