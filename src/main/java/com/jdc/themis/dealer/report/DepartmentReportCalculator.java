package com.jdc.themis.dealer.report;


import static com.jdc.themis.dealer.report.ReportUtils.calcPercentage;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

import ch.lambdaj.Lambda;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Maps;
import com.jdc.themis.dealer.domain.DealerIncomeExpenseFact;
import com.jdc.themis.dealer.domain.DealerIncomeRevenueFact;
import com.jdc.themis.dealer.web.domain.DepartmentDetail;
import com.jdc.themis.dealer.web.domain.ReportDataDepartmentDetail;
import com.jdc.themis.dealer.web.domain.ReportDataDetailAmount;
import com.jdc.themis.dealer.web.domain.ReportDepartmentDataList;

import fj.data.Option;

/**
 * Calculate the income report numbers by department. 
 * 
 * The class is not thread-safe.
 * 
 * @author Kai Chen
 *
 */
public class DepartmentReportCalculator {

	/**
	 * Mapping between department id and department report detail.
	 */
	private final Map<Integer, ReportDataDepartmentDetail> departmentDetails = Maps.newHashMap();
	
	private Integer year;
	private Option<Integer> monthOfYear = Option.<Integer>none();
	private Option<Map<Integer, ReportDataDepartmentDetail>> dealerPreviousDetailOption = Option
			.<Map<Integer, ReportDataDepartmentDetail>> none();
	
	public DepartmentReportCalculator(final Collection<DepartmentDetail> departments, final Integer year) {
		for (final DepartmentDetail department : departments) {
			departmentDetails.put(department.getId(), new ReportDataDepartmentDetail());
			departmentDetails.get(department.getId()).setId(department.getId());
			departmentDetails.get(department.getId()).setName(department.getName());
		}
		
		this.year = year;
		
	}
	
	private enum GetDepartmentIDFromReportDetailFunction implements
			Function<ReportDataDepartmentDetail, Integer> {
		INSTANCE;

		@Override
		public Integer apply(final ReportDataDepartmentDetail item) {
			return item.getId();
		}
	}

	/**
	 * Populate and return a report details with time and dealer report numbers. 
	 * 
	 * @return
	 */
	public ReportDepartmentDataList getReportDetail() {
		final ReportDepartmentDataList reportDetail = new ReportDepartmentDataList();
		reportDetail.setYear(year);
		if ( monthOfYear.isSome() ) {
			reportDetail.setMonth(monthOfYear.some());
		} 
		reportDetail.getDetail().addAll(departmentDetails.values());
		return reportDetail;
	}
	
	
	/**
	 * Set month of year. 
	 * 
	 * Note that it doesn't do NULL value check!
	 * 
	 * @param monthOfYear
	 * @return
	 */
	public DepartmentReportCalculator withMonth(final Option<Integer> monthOfYear) {
		this.monthOfYear = monthOfYear;
		return this;
	}
	/**
	 * Set previous dealer report details.
	 * 
	 * @param previousDetail
	 * @return
	 */
	public DepartmentReportCalculator withPrevious(final Option<ReportDepartmentDataList> previousDetail) {
		if ( previousDetail.isNone() ) {
			return this;
		}
		final Map<Integer, ReportDataDepartmentDetail> dealerPreviousDetails = Maps
				.uniqueIndex(previousDetail.some().getDetail(),
						GetDepartmentIDFromReportDetailFunction.INSTANCE);
		dealerPreviousDetailOption = Option
				.<Map<Integer, ReportDataDepartmentDetail>> some(dealerPreviousDetails);
		return this;
	}
	
	/**
	 * Calculate and populate dealer expense details, including reference, amount and percentage if previous dealer info is provided. 
	 * 
	 * Amount could be "avg" or "sum" of the year's monthly amounts depending on parameter "op". 
	 * 
	 * @param dealerExpenseFacts
	 * @param op
	 * @return
	 */
	public DepartmentReportCalculator calcExpenses(
			final ImmutableListMultimap<Integer, DealerIncomeExpenseFact> dealerExpenseFacts,
			final JournalOp op) {
		for (final Integer departmentID : dealerExpenseFacts.keySet()) {
			final BigDecimal totalExpense = Lambda.sumFrom(
					dealerExpenseFacts.get(departmentID),
					DealerIncomeExpenseFact.class).getAmount();
			final ReportDataDetailAmount amount = new ReportDataDetailAmount();
			amount.setAmount(op == JournalOp.SUM ? totalExpense.doubleValue()
					: totalExpense.doubleValue() / (monthOfYear.some() * 1.0));

			if (dealerPreviousDetailOption.isSome()) {
				amount.setPercentage(calcPercentage(amount.getAmount(), dealerPreviousDetailOption.some().get(departmentID)
								.getExpense().getAmount()));
			}
			departmentDetails.get(departmentID).setExpense(amount);
		}
		return this;
	}
	
	/**
	 * Calculate the margin. 
	 * 
	 * @param dealerRevenueFacts
	 * @param op
	 * @return
	 */
	public DepartmentReportCalculator calcMargins(
			final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFacts,
			final JournalOp op) {
		for (final Integer departmentID : dealerRevenueFacts.keySet()) {
			final BigDecimal totalMargin = Lambda.sumFrom(
					dealerRevenueFacts.get(departmentID),
					DealerIncomeRevenueFact.class).getMargin();
			final ReportDataDetailAmount amount = new ReportDataDetailAmount();
			amount.setAmount(op == JournalOp.SUM ? totalMargin.doubleValue()
					: totalMargin.doubleValue() / (monthOfYear.some() * 1.0));

			if (dealerPreviousDetailOption.isSome()) {
				amount.setPercentage(calcPercentage(amount.getAmount(), dealerPreviousDetailOption.some().get(departmentID)
								.getMargin().getAmount()));
			}
			departmentDetails.get(departmentID).setMargin(amount);
		}
		return this;
	}
	
	/**
	 * Calculate the revenues.
	 * 
	 * @param dealerRevenueFacts
	 * @param op
	 * @return
	 */
	public DepartmentReportCalculator calcRevenues(
			final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFacts,
			final JournalOp op) {
		for (final Integer departmentID : dealerRevenueFacts.keySet()) {
			final BigDecimal totalAmount = Lambda.sumFrom(
					dealerRevenueFacts.get(departmentID),
					DealerIncomeRevenueFact.class).getAmount();
			final ReportDataDetailAmount amount = new ReportDataDetailAmount();
			amount.setAmount(op == JournalOp.SUM ? totalAmount.doubleValue()
					: totalAmount.doubleValue() / (monthOfYear.some() * 1.0));
			if (dealerPreviousDetailOption.isSome()) {
				amount.setPercentage(calcPercentage(amount.getAmount(), dealerPreviousDetailOption.some().get(departmentID)
								.getRevenue().getAmount()));
			} 
			departmentDetails.get(departmentID).setRevenue(amount);
		}
		return this;
	}
	
	public DepartmentReportCalculator calcOpProfit() {
		for (final Integer dealerID : departmentDetails.keySet()) {
			final ReportDataDetailAmount margin = departmentDetails.get(
					dealerID).getMargin();
			final ReportDataDetailAmount expense = departmentDetails.get(
					dealerID).getExpense();
			final ReportDataDetailAmount amount = new ReportDataDetailAmount();
			// Operational Profit = Margin - Expense
			amount.setAmount(margin.getAmount() - expense.getAmount());
			if (dealerPreviousDetailOption.isSome()) {
				amount.setPercentage(calcPercentage(amount.getAmount(), dealerPreviousDetailOption.some().get(dealerID)
								.getOpProfit().getAmount()));
			}
			departmentDetails.get(dealerID).setOpProfit(amount);
		}
		return this;
	}

}
