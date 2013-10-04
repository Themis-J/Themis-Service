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
import com.jdc.themis.dealer.web.domain.QueryReportDataResponse;
import com.jdc.themis.dealer.web.domain.ReportDataDetail;

import fj.data.Option;

@Service
public class DealerIncomeReportServiceImpl implements DealerIncomeReportService {
	private final static Logger logger = LoggerFactory
			.getLogger(DealerIncomeReportServiceImpl.class);

	@Autowired
	private ReportDAO reportDAL;
	@Autowired
	private RefDataQueryService refDataDAL;

	public void setRefDataDAL(RefDataQueryService refDataDAL) {
		this.refDataDAL = refDataDAL;
	}

	public void setReportDAL(ReportDAO reportDAL) {
		this.reportDAL = reportDAL;
	}

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
			counter++;
		}
	}

	@Override
	@Performance
	public QueryReportDataResponse queryOverallIncomeReport(final Integer year,
			final Option<Integer> monthOfYear,
			final Option<Integer> departmentID,
			final Option<Integer> denominator) {
		Preconditions.checkNotNull(year, "year can't be null");
		final QueryReportDataResponse response = new QueryReportDataResponse();
		response.setReportName("OverallIncomeReport");

		if (monthOfYear.isNone()) {
			int previousYear = year - 1;
			final ReportDataDetail reportDetailPreviousYear = getDealerReportDataDetail(
					previousYear, Option.<Integer> none(),
					Option.<ReportDataDetail> none(), departmentID,
					denominator, JournalOp.SUM);
			reportDetailPreviousYear.setYear(previousYear);
			response.getDetail().add(reportDetailPreviousYear);

			final ReportDataDetail reportDetailCurrentYear = getDealerReportDataDetail(
					year, Option.<Integer> none(),
					Option.<ReportDataDetail> some(reportDetailPreviousYear),
					departmentID, denominator, JournalOp.SUM);
			reportDetailCurrentYear.setYear(year);
			response.getDetail().add(reportDetailCurrentYear);
		} else {
			final ReportDataDetail reportDetailMonthlyAvg = getDealerReportDataDetail(
					year, monthOfYear, Option.<ReportDataDetail> none(),
					departmentID, denominator, JournalOp.AVG);
			response.getDetail().add(reportDetailMonthlyAvg);

			final ReportDataDetail reportDetailCurrentMonth = getDealerReportDataDetail(
					year, monthOfYear,
					Option.<ReportDataDetail> some(reportDetailMonthlyAvg),
					departmentID, denominator, JournalOp.SUM);
			response.getDetail().add(reportDetailCurrentMonth);
		}
		return response;
	}

	private ReportDataDetail getDealerReportDataDetail(final Integer year,
			final Option<Integer> monthOfYearOption,
			final Option<ReportDataDetail> previousDetailOption,
			final Option<Integer> departmentIDOption,
			final Option<Integer> denominatorIDOption, final JournalOp op) {
		final DealerReportCalculator calculator = new DealerReportCalculator(
				refDataDAL.getDealers().getItems(), year);
		calculator.withMonth(monthOfYearOption);
		calculator.withPrevious(previousDetailOption);
		calculator.withDenominator(denominatorIDOption);

		// Get all revenues
		final DealerIncomeFactsQueryBuilder queryBuilder = 
				new DealerIncomeFactsQueryBuilder(reportDAL);
		queryBuilder.withYear(year);
		if ( monthOfYearOption.isSome() ) {
			queryBuilder.withMonthOfYear(monthOfYearOption.some());
		} 
		if ( departmentIDOption.isSome() ) {
			queryBuilder.withDepartmentID(departmentIDOption.some());
		}
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
								.withItemCategory("租恁收入");
		final Collection<DealerIncomeRevenueFact> revenueFacts = queryBuilder.queryRevenues();

		final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFacts = Multimaps
				.index(revenueFacts, GetDealerIDFromRevenueFunction.INSTANCE);
		// Get all expenses
		final Collection<DealerIncomeExpenseFact> expenseFacts = 
												queryBuilder.clear()
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
					new DealerIncomeFactsQueryBuilder(reportDAL);
			otherQueryBuilder.withYear(year);
			if ( monthOfYearOption.isSome() ) {
				otherQueryBuilder.withMonthOfYear(monthOfYearOption.some());
			} 
			otherQueryBuilder.withItemCategory("非经营性损益进项")
									.withItemCategory("非销售类返利");
			final Collection<DealerIncomeRevenueFact> otherRevenueFacts = otherQueryBuilder.queryRevenues();
			
			final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> otherDealerRevenueFacts = Multimaps
					.index(otherRevenueFacts,
							GetDealerIDFromRevenueFunction.INSTANCE);
			otherQueryBuilder.clear()
								.withItemCategory("非经营性损益削项")
								.withItemCategory("员工分红");
			final Collection<DealerIncomeExpenseFact> otherExpenseFacts = otherQueryBuilder.queryExpenses();
			
			final ImmutableListMultimap<Integer, DealerIncomeExpenseFact> otherDealerExpenseFacts = Multimaps
					.index(otherExpenseFacts,
							GetDealerIDFromExpenseFunction.INSTANCE);
			calculator.calcNetProfit(otherDealerRevenueFacts,
					otherDealerExpenseFacts, op);
		}
		if (denominatorIDOption.isSome()) {
			calculator.prepareDenominators().adjustExpenseByDenominator()
					.adjustMarginByDenominator().adjustNetProfitByDenominator()
					.adjustOpProfitByDenominator();
		}
		return calculator.getReportDetail();
	}

	@Override
	public QueryReportDataResponse queryDepartmentIncomeReport(
			final Integer year, final Option<Integer> monthOfYear, 
			final Option<Integer> dealerID, final Option<Integer> departmentID) {
		Preconditions.checkNotNull(year, "year can't be null");
		Preconditions.checkNotNull(dealerID, "dealer id can't be null");
		final QueryReportDataResponse response = new QueryReportDataResponse();
		response.setReportName("DepartmentIncomeReport");

		if (monthOfYear.isNone()) {
			int previousYear = year - 1;
			final ReportDataDetail reportDetailPreviousYear = getDepartmentReportDataDetail(
					previousYear, dealerID, departmentID, Option.<Integer> none(),
					Option.<ReportDataDetail> none(), JournalOp.SUM);
			reportDetailPreviousYear.setYear(previousYear);
			response.getDetail().add(reportDetailPreviousYear);
			final ReportDataDetail reportDetailCurrentYear = getDepartmentReportDataDetail(
					year, dealerID, departmentID, Option.<Integer> none(),
					Option.<ReportDataDetail> some(reportDetailPreviousYear),
					JournalOp.SUM);
			reportDetailCurrentYear.setYear(year);
			response.getDetail().add(reportDetailCurrentYear);
		} else {
			final ReportDataDetail reportDetailMonthlyAvg = getDepartmentReportDataDetail(
					year, dealerID, departmentID, monthOfYear,
					Option.<ReportDataDetail> none(), JournalOp.AVG);
			response.getDetail().add(reportDetailMonthlyAvg);

			final ReportDataDetail reportDetailCurrentMonth = getDepartmentReportDataDetail(
					year, dealerID, departmentID, monthOfYear,
					Option.<ReportDataDetail> some(reportDetailMonthlyAvg),
					JournalOp.SUM);
			response.getDetail().add(reportDetailCurrentMonth);
		}
		return response;
	}

	private ReportDataDetail getDepartmentReportDataDetail(final Integer year,
			final Option<Integer> dealerIDOption,
			final Option<Integer> departmentIDOption,
			final Option<Integer> monthOfYearOption,
			final Option<ReportDataDetail> previousDetailOption,
			final JournalOp op) {
		final DepartmentReportCalculator calculator = new DepartmentReportCalculator(
				refDataDAL.getDepartments().getItems(), year);
		calculator.withMonth(monthOfYearOption);
		calculator.withPrevious(previousDetailOption);

		// Get all revenues
		final DealerIncomeFactsQueryBuilder queryBuilder = 
				new DealerIncomeFactsQueryBuilder(reportDAL);
		queryBuilder.withYear(year);
		if ( monthOfYearOption.isSome() ) {
			queryBuilder.withMonthOfYear(monthOfYearOption.some());
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

	@Override
	public QueryReportDataResponse querySalesReport(final Integer year,
			final Option<Integer> monthOfYear,
			final Option<Integer> departmentID) {
		Preconditions.checkNotNull(year, "year can't be null");
		final QueryReportDataResponse response = new QueryReportDataResponse();
		response.setReportName("SalesReport");

		if (monthOfYear.isNone()) {
			int previousYear = year - 1;
			final ReportDataDetail reportDetailPreviousYear = getDealerSalesReportDataDetail(
					previousYear, Option.<Integer> none(),
					Option.<ReportDataDetail> none(), departmentID,
					JournalOp.SUM);
			reportDetailPreviousYear.setYear(previousYear);
			response.getDetail().add(reportDetailPreviousYear);

			final ReportDataDetail reportDetailCurrentYear = getDealerSalesReportDataDetail(
					year, Option.<Integer> none(),
					Option.<ReportDataDetail> some(reportDetailPreviousYear),
					departmentID, JournalOp.SUM);
			reportDetailCurrentYear.setYear(year);
			response.getDetail().add(reportDetailCurrentYear);
		} else {
			final ReportDataDetail reportDetailMonthlyAvg = getDealerSalesReportDataDetail(
					year, monthOfYear, Option.<ReportDataDetail> none(),
					departmentID, JournalOp.AVG);
			response.getDetail().add(reportDetailMonthlyAvg);

			final ReportDataDetail reportDetailCurrentMonth = getDealerSalesReportDataDetail(
					year, monthOfYear,
					Option.<ReportDataDetail> some(reportDetailMonthlyAvg),
					departmentID, JournalOp.SUM);
			response.getDetail().add(reportDetailCurrentMonth);
		}
		return response;
	}

	private ReportDataDetail getDealerSalesReportDataDetail(final Integer year,
			final Option<Integer> monthOfYearOption,
			final Option<ReportDataDetail> previousDetailOption,
			final Option<Integer> departmentIDOption, final JournalOp op) {
		final DealerSalesReportCalculator calculator = new DealerSalesReportCalculator(
				refDataDAL.getDealers().getItems(), year);
		calculator.withMonth(monthOfYearOption);
		calculator.withPrevious(previousDetailOption);

		final DealerIncomeFactsQueryBuilder revenueFactsQueryBuilder = 
				new DealerIncomeFactsQueryBuilder(reportDAL);
		revenueFactsQueryBuilder.withYear(year);
		if ( monthOfYearOption.isSome() ) {
			revenueFactsQueryBuilder.withMonthOfYear(monthOfYearOption.some());
		} 
		if ( departmentIDOption.isSome() ) {
			revenueFactsQueryBuilder.withDepartmentID(departmentIDOption.some());
		}
		revenueFactsQueryBuilder.withItemCategory("新轿车零售")
										.withItemCategory("新货车零售")
										.withItemCategory("附加产品业务")
										.withItemCategory("二手车零售")
										.withItemCategory("新车其它收入")
										.withItemCategory("二手车其它收入");
		final Collection<DealerIncomeRevenueFact> overallSalesFacts = revenueFactsQueryBuilder.queryRevenues();
		
		revenueFactsQueryBuilder.clear()
								.withItemCategory("新轿车零售")
								.withItemCategory("新货车零售");
		final Collection<DealerIncomeRevenueFact> retailFacts = revenueFactsQueryBuilder.queryRevenues();
		
		revenueFactsQueryBuilder.clear()
								.withItemCategory("新车其它收入")
								.withItemCategory("二手车其它收入")
								.withItemID(refDataDAL
									.getSalesServiceRevenueItem(
										"大客户采购（租车公司，政府机关）",
										"新车其它收入").getId().longValue())
								.withItemID(refDataDAL
										.getSalesServiceRevenueItem("批发销售",
										"二手车其它收入").getId().longValue());
		final Collection<DealerIncomeRevenueFact> wholesaleFacts = revenueFactsQueryBuilder.queryRevenues();

		revenueFactsQueryBuilder.clear()
								.withItemCategory("新车其它收入")
								.withItemID(refDataDAL
										.getSalesServiceRevenueItem("他店调车",
												"新车其它收入").getId().longValue());
		final Collection<DealerIncomeRevenueFact> otherFacts = revenueFactsQueryBuilder.queryRevenues();

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
}
