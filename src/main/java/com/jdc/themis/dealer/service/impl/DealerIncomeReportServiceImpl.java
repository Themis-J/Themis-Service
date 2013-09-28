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

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import com.jdc.themis.dealer.data.dao.ReportDAO;
import com.jdc.themis.dealer.domain.DealerIncomeExpenseFact;
import com.jdc.themis.dealer.domain.DealerIncomeRevenueFact;
import com.jdc.themis.dealer.report.DealerReportCalculator;
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
		Preconditions.checkArgument(!LocalDate.parse(request.getToDate()).isAfter(today), "cannot import future date");
		
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

	private enum GetDealerIDFromRevenueFunction implements
			Function<DealerIncomeRevenueFact, Integer> {
		INSTANCE;

		@Override
		public Integer apply(final DealerIncomeRevenueFact item) {
			return item.getDealerID();
		}
	}

	private enum GetDealerIDFromExpenseFunction implements
			Function<DealerIncomeExpenseFact, Integer> {
		INSTANCE;

		@Override
		public Integer apply(final DealerIncomeExpenseFact item) {
			return item.getDealerID();
		}
	}

	@Override
	@Performance
	public QueryReportDataResponse queryOverallIncomeReport(
			final Integer year,
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
					Option.<ReportDataDetail> none(), departmentID, denominator, JournalOp.SUM);
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
					year, monthOfYear,
					Option.<ReportDataDetail> none(), departmentID, denominator, JournalOp.AVG);
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
			final Option<Integer> denominatorIDOption,
			final JournalOp op) {
		final DealerReportCalculator calculator = new DealerReportCalculator(refDataDAL.getDealers().getItems(), year);
		calculator.withMonth(monthOfYearOption);
		calculator.withPrevious(previousDetailOption);
		calculator.withDenominator(denominatorIDOption);
		
		// Get all revenues
		final Collection<DealerIncomeRevenueFact> revenueFacts = reportDAL
				.getDealerIncomeRevenueFacts(
						year,
						Lists.newArrayList(monthOfYearOption.isSome() ? new Integer[] { monthOfYearOption
								.some() } : new Integer[] {}), 
						Lists.newArrayList(departmentIDOption.isSome() ? new Integer[] { departmentIDOption.some() }
										: new Integer[] {}), // department id
						Lists.newArrayList(new Integer[] {}), 
						Lists.newArrayList(new String[] { "新轿车零售", "新货车零售",
										"附加产品业务", "二手车零售", "维修收入", "配件收入", "钣喷收入", "新车其它收入", "二手车其它收入", "维修其它收入", "维修其它收入", "钣喷其它收入", "租恁收入" }),
						Lists.newArrayList(new Integer[] {}), Lists.newArrayList(new Integer[] {}));

		final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFacts = Multimaps
				.index(revenueFacts, GetDealerIDFromRevenueFunction.INSTANCE);
		// Get all expenses
		final Collection<DealerIncomeExpenseFact> expenseFacts = reportDAL
				.getDealerIncomeExpenseFacts(
						year,
						Lists.newArrayList(monthOfYearOption.isSome() ? new Integer[] { monthOfYearOption
								.some() } : new Integer[] {}), 
								Lists.newArrayList(departmentIDOption.isSome() ? new Integer[] { departmentIDOption.some() }
								: new Integer[] {}), // department id
						Lists.newArrayList(new Integer[] {}), 
						Lists.newArrayList(new String[] { "变动费用", "销售费用",
										"人工费用", "半固定费用", "固定费用" }), 
						Lists.newArrayList(new Integer[] {}), Lists.newArrayList(new Integer[] {}));

		final ImmutableListMultimap<Integer, DealerIncomeExpenseFact> dealerExpenseFacts = Multimaps
				.index(expenseFacts, GetDealerIDFromExpenseFunction.INSTANCE);

		calculator.calcRevenues(dealerRevenueFacts, op)
			.calcMargins(dealerRevenueFacts, op)
			.calcExpenses(dealerExpenseFacts, op)
			.calcOpProfit();
		
		if ( departmentIDOption.isNone() ) {
			// calculate net profit
			final Collection<DealerIncomeRevenueFact> otherRevenueFacts = reportDAL
					.getDealerIncomeRevenueFacts(
							year,
							Lists.newArrayList(monthOfYearOption.isSome() ? new Integer[] { monthOfYearOption
									.some() } : new Integer[] {}), Lists
									.newArrayList(new Integer[] {}), Lists
									.newArrayList(new Integer[] {}), Lists
									.newArrayList(new String[] { "非经营性损益进项", "其它进项" }),
							Lists.newArrayList(new Integer[] {}), Lists.newArrayList(new Integer[] {}));
			final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> otherDealerRevenueFacts = Multimaps
					.index(otherRevenueFacts, GetDealerIDFromRevenueFunction.INSTANCE);
			final Collection<DealerIncomeExpenseFact> otherExpenseFacts = reportDAL
					.getDealerIncomeExpenseFacts(
							year,
							Lists.newArrayList(monthOfYearOption.isSome() ? new Integer[] { monthOfYearOption
									.some() } : new Integer[] {}), Lists
									.newArrayList(new Integer[] {}), Lists
									.newArrayList(new Integer[] {}), Lists
									.newArrayList(new String[] { "非经营性损益削项", "其它削项" }), Lists
									.newArrayList(new Integer[] {}), Lists.newArrayList(new Integer[] {}));
			final ImmutableListMultimap<Integer, DealerIncomeExpenseFact> otherDealerExpenseFacts = Multimaps
					.index(otherExpenseFacts, GetDealerIDFromExpenseFunction.INSTANCE);
			calculator.calcNetProfit(otherDealerRevenueFacts, otherDealerExpenseFacts, op);
		}
		if ( denominatorIDOption.isSome() ) {
			calculator.prepareDenominators()
				.adjustExpenseByDenominator()
				.adjustMarginByDenominator()
				.adjustNetProfitByDenominator()
				.adjustOpProfitByDenominator();
		}
		return calculator.getReportDetail();
	}

	@Override
	public QueryReportDataResponse queryDepartmentIncomeReport(
			final Integer year,
			final Option<Integer> dealerID, 
			final Option<Integer> monthOfYear) {
		Preconditions.checkNotNull(year, "year can't be null");
		Preconditions.checkNotNull(dealerID, "dealer id can't be null");
		final QueryReportDataResponse response = new QueryReportDataResponse();
		response.setReportName("DepartmentIncomeReport");

		if (monthOfYear.isNone()) {
			int previousYear = year - 1;
			final ReportDataDetail reportDetailPreviousYear = getDepartmentReportDataDetail(
					previousYear, dealerID, Option.<Integer> none(),  
					Option.<ReportDataDetail> none(), JournalOp.SUM);
			reportDetailPreviousYear.setYear(previousYear);
			response.getDetail().add(reportDetailPreviousYear);

			final ReportDataDetail reportDetailCurrentYear = getDepartmentReportDataDetail(
					year, dealerID, Option.<Integer> none(),
					Option.<ReportDataDetail> some(reportDetailPreviousYear),
					JournalOp.SUM);
			reportDetailCurrentYear.setYear(year);
			response.getDetail().add(reportDetailCurrentYear);
		} else {
			final ReportDataDetail reportDetailMonthlyAvg = getDepartmentReportDataDetail(
					year, dealerID, monthOfYear,
					Option.<ReportDataDetail> none(), JournalOp.AVG);
			response.getDetail().add(reportDetailMonthlyAvg);

			final ReportDataDetail reportDetailCurrentMonth = getDepartmentReportDataDetail(
					year, dealerID, monthOfYear,
					Option.<ReportDataDetail> some(reportDetailMonthlyAvg),
					JournalOp.SUM);
			response.getDetail().add(reportDetailCurrentMonth);
		}
		return response;
	}

	private ReportDataDetail getDepartmentReportDataDetail(final Integer year,
			final Option<Integer> dealerIDOption, 
			final Option<Integer> monthOfYearOption,
			final Option<ReportDataDetail> previousDetailOption,
			final JournalOp op) {
		final DepartmentReportCalculator calculator = new DepartmentReportCalculator(refDataDAL.getDepartments().getItems(), year);
		calculator.withMonth(monthOfYearOption);
		calculator.withPrevious(previousDetailOption);
		
		// Get all revenues
		final Collection<DealerIncomeRevenueFact> revenueFacts = reportDAL
				.getDealerIncomeRevenueFacts(
						year,
						Lists.newArrayList(monthOfYearOption.isSome() ? new Integer[] { monthOfYearOption
								.some() } : new Integer[] {}), 
						Lists.newArrayList(new Integer[] {}), // department id
						Lists.newArrayList(new Integer[] {}), 
						Lists.newArrayList(new String[] { "新轿车零售", "新货车零售",
										"附加产品业务", "二手车零售", "维修收入", "配件收入", "钣喷收入", "新车其它收入", "二手车其它收入", "维修其它收入", "维修其它收入", "钣喷其它收入", "租恁收入" }),
						Lists.newArrayList(new Integer[] {}), 
						Lists.newArrayList(dealerIDOption.isSome() ? new Integer[]{dealerIDOption.some()} : new Integer[]{}));

		final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFacts = Multimaps
				.index(revenueFacts, GetDealerIDFromRevenueFunction.INSTANCE);
		// Get all expenses
		final Collection<DealerIncomeExpenseFact> expenseFacts = reportDAL
				.getDealerIncomeExpenseFacts(
						year,
						Lists.newArrayList(monthOfYearOption.isSome() ? new Integer[] { monthOfYearOption
								.some() } : new Integer[] {}), 
								Lists.newArrayList(new Integer[] {}), // department id
						Lists.newArrayList(new Integer[] {}), 
						Lists.newArrayList(new String[] { "变动费用", "销售费用",
										"人工费用", "半固定费用", "固定费用" }), 
						Lists.newArrayList(new Integer[] {}), 
						Lists.newArrayList(dealerIDOption.isSome() ? new Integer[]{dealerIDOption.some()} : new Integer[]{}));

		final ImmutableListMultimap<Integer, DealerIncomeExpenseFact> dealerExpenseFacts = Multimaps
				.index(expenseFacts, GetDealerIDFromExpenseFunction.INSTANCE);

		calculator.calcRevenues(dealerRevenueFacts, op)
			.calcMargins(dealerRevenueFacts, op)
			.calcExpenses(dealerExpenseFacts, op)
			.calcOpProfit();
		
		return calculator.getReportDetail();
	}

	@Override
	public QueryReportDataResponse querySalesReport(final Integer year,
			final Option<Integer> monthOfYear, final Option<Integer> departmentID,
			final Option<Integer> denominator) {
		// TODO Auto-generated method stub
		return null;
	}
}
