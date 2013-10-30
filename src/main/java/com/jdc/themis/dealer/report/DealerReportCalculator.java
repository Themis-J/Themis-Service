package com.jdc.themis.dealer.report;

import static com.jdc.themis.dealer.report.ReportUtils.calcPercentage;
import static com.jdc.themis.dealer.report.ReportUtils.calcReference;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

import ch.lambdaj.Lambda;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Maps;
import com.jdc.themis.dealer.domain.DealerIncomeExpenseFact;
import com.jdc.themis.dealer.domain.DealerIncomeRevenueFact;
import com.jdc.themis.dealer.web.domain.DealerDetail;
import com.jdc.themis.dealer.web.domain.ReportDataDealerDetail;
import com.jdc.themis.dealer.web.domain.ReportDataDetailAmount;
import com.jdc.themis.dealer.web.domain.ReportDealerDataList;

import fj.data.Option;

/**
 * Calculate the overall income report numbers by dealer. 
 * 
 * The class is not thread-safe.
 * 
 * @author Kai Chen
 *
 */
public class DealerReportCalculator {

	/**
	 * Mapping between dealer id and dealer report detail.
	 */
	private final Map<Integer, ReportDataDealerDetail> dealerDetails = Maps.newHashMap();
	
	private Integer year;
	private Option<Integer> monthOfYear = Option.<Integer>none();
	private Option<Map<Integer, ReportDataDealerDetail>> dealerPreviousDetailOption = Option
			.<Map<Integer, ReportDataDealerDetail>> none();
	private Option<Denominator> denominatorOption = Option.<Denominator>none();
	
	public DealerReportCalculator(final Collection<DealerDetail> dealers, final Integer year) {
		for (final DealerDetail dealer : dealers) {
			// initialize dealer details map for all dealers
			dealerDetails.put(dealer.getId(), new ReportDataDealerDetail());
			dealerDetails.get(dealer.getId()).setId(dealer.getId());
			dealerDetails.get(dealer.getId()).setName(dealer.getName());
			dealerDetails.get(dealer.getId()).setCode(dealer.getCode());
		}
		
		this.year = year;
		
	}
	
	public enum Denominator {
		REVENUE, MARGIN;
		
		public static Option<Denominator> valueOf(int value) {
			for ( final Denominator d : Denominator.values() ) {
				if ( d.ordinal() == value ) {
					return Option.<Denominator>some(d);
				}
			}
			return Option.<Denominator>none();
		}
		
	}
	
	private enum GetDealerIDFromReportDetailFunction implements
			Function<ReportDataDealerDetail, Integer> {
		INSTANCE;

		@Override
		public Integer apply(final ReportDataDealerDetail item) {
			return item.getId();
		}
	}

	/**
	 * Populate and return a report details with time and dealer report numbers. 
	 * 
	 * @return
	 */
	public ReportDealerDataList getReportDetail() {
		final ReportDealerDataList reportDetail = new ReportDealerDataList();
		reportDetail.setYear(year);
		if ( monthOfYear.isSome() ) {
			reportDetail.setMonth(monthOfYear.some());
		} 
		reportDetail.getDetail().addAll(dealerDetails.values());
		return reportDetail;
	}
	
	/**
	 * Set denominator for each report numbers.
	 * 
	 * @param denominatorOption
	 * @return
	 */
	public DealerReportCalculator withDenominator(final Option<Integer> denominatorOption) {
		if ( denominatorOption.isSome() ) {
			this.denominatorOption = Denominator.valueOf(denominatorOption.some());
		} 
		return this;
	}
	
	/**
	 * Set month of year. 
	 * 
	 * Note that it doesn't do NULL value check!
	 * 
	 * @param monthOfYear
	 * @return
	 */
	public DealerReportCalculator withMonth(final Option<Integer> monthOfYear) {
		this.monthOfYear = monthOfYear;
		return this;
	}
	/**
	 * Set previous dealer report details.
	 * 
	 * @param previousDetail
	 * @return
	 */
	public DealerReportCalculator withPrevious(final Option<ReportDealerDataList> previousDetail) {
		if ( previousDetail.isNone() ) {
			return this;
		}
		final Map<Integer, ReportDataDealerDetail> dealerPreviousDetails = Maps
				.uniqueIndex(previousDetail.some().getDetail(),
						GetDealerIDFromReportDetailFunction.INSTANCE);
		dealerPreviousDetailOption = Option
				.<Map<Integer, ReportDataDealerDetail>> some(dealerPreviousDetails);
		return this;
	}
	
	/**
	 * Adjust the expense by using denominator. 
	 * 
	 * This has to be called after "prepareDenominators" function is called. 
	 * 
	 * @return
	 */
	public DealerReportCalculator adjustExpenseByDenominator() {
		if (this.denominatorOption.isNone()) {
			return this;
		} 
		for (final Integer dealerID : dealerDetails.keySet()) {
			final Double amount = dealerDetails.get(dealerID).getExpense().getAmount() / denominators.get(dealerID);
			dealerDetails.get(dealerID).getExpense().setAmount(amount);
		}

		final Double reference = calcReference(Lambda.extract(dealerDetails.values(), 
				Lambda.on(ReportDataDealerDetail.class).getExpense().getAmount()));
		Lambda.forEach(dealerDetails.values()).getExpense().setReference(reference);
		return this;
	}
	
	/**
	 * Adjust the margin by using denominator. 
	 * 
	 * This has to be called after "prepareDenominators" function is called. 
	 * 
	 * @return
	 */
	public DealerReportCalculator adjustMarginByDenominator() {
		if (this.denominatorOption.isNone()) {
			return this;
		} 
		for (final Integer dealerID : dealerDetails.keySet()) {
			final Double amount = dealerDetails.get(dealerID).getMargin().getAmount() / denominators.get(dealerID);
			dealerDetails.get(dealerID).getMargin().setAmount(amount);
		}

		final Double reference = calcReference(Lambda.extract(dealerDetails.values(), 
				Lambda.on(ReportDataDealerDetail.class).getMargin().getAmount()));
		Lambda.forEach(dealerDetails.values()).getMargin().setReference(reference);
		return this;
	}
	
	/**
	 * Adjust the operational profit by using denominator. 
	 * 
	 * This has to be called after "prepareDenominators" function is called. 
	 * 
	 * @return
	 */
	public DealerReportCalculator adjustOpProfitByDenominator() {
		if (this.denominatorOption.isNone()) {
			return this;
		} 
		for (final Integer dealerID : dealerDetails.keySet()) {
			final Double amount = dealerDetails.get(dealerID).getOpProfit().getAmount() / denominators.get(dealerID);
			dealerDetails.get(dealerID).getOpProfit().setAmount(amount);
		}
		final Double reference = calcReference(Lambda.extract(dealerDetails.values(), 
				Lambda.on(ReportDataDealerDetail.class).getOpProfit().getAmount()));
		Lambda.forEach(dealerDetails.values()).getOpProfit().setReference(reference);
		return this;
	}
	
	/**
	 * Adjust the net profit by using denominator. 
	 * 
	 * This has to be called after "prepareDenominators" function is called. 
	 * 
	 * @return
	 */
	public DealerReportCalculator adjustNetProfitByDenominator() {
		if (this.denominatorOption.isNone()) {
			return this;
		} 
		for (final Integer dealerID : dealerDetails.keySet()) {
			final Double amount = dealerDetails.get(dealerID).getNetProfit().getAmount() / denominators.get(dealerID);
			dealerDetails.get(dealerID).getNetProfit().setAmount(amount);
		}
		final Double reference = calcReference(Lambda.extract(dealerDetails.values(), 
				Lambda.on(ReportDataDealerDetail.class).getNetProfit().getAmount()));
		Lambda.forEach(dealerDetails.values()).getNetProfit().setReference(reference);
		return this;
	}
	
	private Map<Integer, Double> denominators;
	public DealerReportCalculator prepareDenominators() {
		if (this.denominatorOption.isNone()) {
			return this;
		} 
		denominators = Maps.newHashMap();
		for (final Integer dealerID : dealerDetails.keySet()) {
			if ( this.denominatorOption.some() == Denominator.REVENUE ) {
				if ( dealerDetails.get(dealerID).getRevenue().getAmount() == BigDecimal.ZERO.doubleValue() ) {
					denominators.put(dealerID, 1.0);
				} else {
					denominators.put(dealerID, dealerDetails.get(dealerID).getRevenue().getAmount());
				}
			} else if ( this.denominatorOption.some() == Denominator.MARGIN ) {
				if ( dealerDetails.get(dealerID).getMargin().getAmount() == BigDecimal.ZERO.doubleValue() ) {
					denominators.put(dealerID, 1.0);
				} else {
					denominators.put(dealerID, dealerDetails.get(dealerID).getMargin().getAmount());
				}
			}
		} 
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
	public DealerReportCalculator calcExpenses(
			final ImmutableListMultimap<Integer, DealerIncomeExpenseFact> dealerExpenseFacts,
			final JournalOp op) {
		for (final Integer dealerID : dealerExpenseFacts.keySet()) {
			final BigDecimal totalExpense = Lambda.sumFrom(
					dealerExpenseFacts.get(dealerID),
					DealerIncomeExpenseFact.class).getAmount();
			final ReportDataDetailAmount amount = new ReportDataDetailAmount();
			amount.setAmount(op == JournalOp.SUM ? totalExpense.doubleValue()
					: totalExpense.doubleValue() / (monthOfYear.some() * 1.0));

			if (dealerPreviousDetailOption.isSome()) {
				// Percentage = (this year OR month amount - last year OR avg amount) / last year OR avg amount
				amount.setPercentage(calcPercentage(amount.getAmount(), dealerPreviousDetailOption
						.some().get(dealerID).getExpense().getAmount()));
			}
			dealerDetails.get(dealerID).setExpense(amount);
		}

		final Double reference = calcReference(Lambda.extract(dealerDetails.values(), 
				Lambda.on(ReportDataDealerDetail.class).getExpense().getAmount()));
		Lambda.forEach(dealerDetails.values()).getExpense().setReference(reference);
		return this;
	}
	
	/**
	 * Calculate the margin. 
	 * 
	 * @param dealerRevenueFacts
	 * @param op
	 * @return
	 */
	public DealerReportCalculator calcMargins(
			final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFacts,
			final JournalOp op) {
		for (final Integer dealerID : dealerRevenueFacts.keySet()) {
			final BigDecimal totalMargin = Lambda.sumFrom(
					dealerRevenueFacts.get(dealerID),
					DealerIncomeRevenueFact.class).getMargin();
			final ReportDataDetailAmount amount = new ReportDataDetailAmount();
			amount.setAmount(op == JournalOp.SUM ? totalMargin.doubleValue()
					: totalMargin.doubleValue() / (monthOfYear.some() * 1.0));

			if (dealerPreviousDetailOption.isSome()) {
				amount.setPercentage(calcPercentage(amount.getAmount(), dealerPreviousDetailOption
						.some().get(dealerID).getMargin().getAmount()));
			}
			dealerDetails.get(dealerID).setMargin(amount);
		}
		final Double reference = calcReference(Lambda.extract(dealerDetails.values(), 
												Lambda.on(ReportDataDealerDetail.class).getMargin().getAmount()));
		Lambda.forEach(dealerDetails.values()).getMargin().setReference(reference);
		return this;
	}
	
	/**
	 * Calculate the revenues.
	 * 
	 * @param dealerRevenueFacts
	 * @param op
	 * @return
	 */
	public DealerReportCalculator calcRevenues(
			final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFacts,
			final JournalOp op) {
		for (final Integer dealerID : dealerRevenueFacts.keySet()) {
			final BigDecimal totalAmount = Lambda.sumFrom(
					dealerRevenueFacts.get(dealerID),
					DealerIncomeRevenueFact.class).getAmount();
			final ReportDataDetailAmount amount = new ReportDataDetailAmount();
			amount.setAmount(op == JournalOp.SUM ? totalAmount.doubleValue()
					: totalAmount.doubleValue() / (monthOfYear.some() * 1.0));
			if (dealerPreviousDetailOption.isSome()) {
				amount.setPercentage(calcPercentage(amount.getAmount(), dealerPreviousDetailOption
						.some().get(dealerID).getRevenue().getAmount()));
			}
			dealerDetails.get(dealerID).setRevenue(amount);
		}
		final Double reference = calcReference(Lambda.extract(dealerDetails.values(), 
				Lambda.on(ReportDataDealerDetail.class).getRevenue().getAmount()));
		Lambda.forEach(dealerDetails.values()).getRevenue().setReference(reference);
		return this;
	}
	
	/**
	 * Calculate net profit. 
	 * 
	 * @param otherDealerRevenueFacts
	 * @param otherDealerExpenseFacts
	 * @param op
	 * @return
	 */
	public DealerReportCalculator calcNetProfit(
			final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> otherDealerRevenueFacts,
			final ImmutableListMultimap<Integer, DealerIncomeExpenseFact> otherDealerExpenseFacts,
			final JournalOp op) {
		for (final Integer dealerID : dealerDetails.keySet()) {
			// Net Profit = Margin - Expense + Other Revenue - Other Expense
			// NonRecurrentPNL is included in other...
			final Double margin = this.dealerDetails.get(dealerID).getMargin().getAmount();
			final Double expense = this.dealerDetails.get(dealerID).getExpense().getAmount();
			final BigDecimal otherRevenue = Lambda.sumFrom(
					otherDealerRevenueFacts.get(dealerID),
					DealerIncomeRevenueFact.class).getAmount();
			final BigDecimal otherExpense = Lambda.sumFrom(
					otherDealerExpenseFacts.get(dealerID),
					DealerIncomeExpenseFact.class).getAmount();
			
			final Double totalAmount = op == JournalOp.SUM ? 
						margin - expense + otherRevenue.doubleValue() - otherExpense.doubleValue():
							(margin - expense + otherRevenue.doubleValue() - otherExpense.doubleValue()) / (monthOfYear.some() * 1.0);
			
			final ReportDataDetailAmount amount = new ReportDataDetailAmount();
			amount.setAmount(totalAmount);

			if (dealerPreviousDetailOption.isSome()) {
				amount.setPercentage(calcPercentage(amount.getAmount(), dealerPreviousDetailOption
						.some().get(dealerID).getNetProfit().getAmount()));
			}
			dealerDetails.get(dealerID).setNetProfit(amount);
		}
		final Double reference = calcReference(Lambda.extract(dealerDetails.values(), 
				Lambda.on(ReportDataDealerDetail.class).getNetProfit().getAmount()));
		Lambda.forEach(dealerDetails.values()).getNetProfit().setReference(reference);
		return this;
	}

	/**
	 * Calculate operational profit
	 * @return
	 */
	public DealerReportCalculator calcOpProfit() {
		for (final Integer dealerID : dealerDetails.keySet()) {
			final ReportDataDetailAmount margin = dealerDetails.get(
					dealerID).getMargin();
			final ReportDataDetailAmount expense = dealerDetails.get(
					dealerID).getExpense();
			final ReportDataDetailAmount amount = new ReportDataDetailAmount();
			// Operational Profit = Margin - Expense
			amount.setAmount(margin.getAmount() - expense.getAmount());

			if (dealerPreviousDetailOption.isSome()) {
				amount.setPercentage(calcPercentage(amount.getAmount(), dealerPreviousDetailOption
						.some().get(dealerID).getOpProfit().getAmount()));
			}
			dealerDetails.get(dealerID).setOpProfit(amount);
		}
		final Double reference = calcReference(Lambda.extract(dealerDetails.values(), 
				Lambda.on(ReportDataDealerDetail.class).getOpProfit().getAmount()));
		Lambda.forEach(dealerDetails.values()).getOpProfit().setReference(reference);
		return this;
	}

}
