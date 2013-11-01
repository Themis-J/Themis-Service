package com.jdc.themis.dealer.report;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

import ch.lambdaj.Lambda;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Maps;
import com.jdc.themis.dealer.domain.DealerIncomeExpenseFact;
import com.jdc.themis.dealer.domain.DealerIncomeRevenueFact;
import com.jdc.themis.dealer.web.domain.DealerDetail;
import com.jdc.themis.dealer.web.domain.ReportDataDealerExpensePercentageDetail;
import com.jdc.themis.dealer.web.domain.ReportDataDetailAmount;
import com.jdc.themis.dealer.web.domain.ReportDealerExpensePercentageDataList;

import fj.data.Option;

/**
 * Calculate the overall expense percentage report numbers by dealer. 
 * 
 * The class is not thread-safe.
 * 
 * @author Kai Chen
 *
 */
public class DealerExpensePercentageReportCalculator {

	/**
	 * Mapping between dealer id and dealer report detail.
	 */
	private final Map<Integer, ReportDataDealerExpensePercentageDetail> dealerDetails = Maps.newHashMap();
	
	private Integer year;
	private Integer monthOfYear;

	public DealerExpensePercentageReportCalculator(final Collection<DealerDetail> dealers, final Integer year, final Integer monthOfYear) {
		for (final DealerDetail dealer : dealers) {
			// initialize dealer details map for all dealers
			dealerDetails.put(dealer.getId(), new ReportDataDealerExpensePercentageDetail());
			dealerDetails.get(dealer.getId()).setId(dealer.getId());
			dealerDetails.get(dealer.getId()).setName(dealer.getName());
			dealerDetails.get(dealer.getId()).setCode(dealer.getCode());
		}
		
		this.year = year;
		this.monthOfYear = monthOfYear;
	}
	
	public static enum Denominator {
		SALES_MARGIN, MARGIN;
		
		public static Option<Denominator> valueOf(int value) {
			for ( final Denominator d : Denominator.values() ) {
				if ( d.ordinal() == value ) {
					return Option.<Denominator>some(d);
				}
			}
			return Option.<Denominator>none();
		}
		
	}
	
	/**
	 * Populate and return a report details with time and dealer report numbers. 
	 * 
	 * @return
	 */
	public ReportDealerExpensePercentageDataList getReportDetail() {
		final ReportDealerExpensePercentageDataList reportDetail = new ReportDealerExpensePercentageDataList();
		reportDetail.setYear(year);
		reportDetail.setMonth(monthOfYear);
		reportDetail.getDetail().addAll(dealerDetails.values());
		return reportDetail;
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
	public DealerExpensePercentageReportCalculator calcExpenses(
			final ImmutableListMultimap<Integer, DealerIncomeExpenseFact> dealerExpenseFacts,
			final JournalOp op) {
		for (final Integer dealerID : dealerExpenseFacts.keySet()) {
			final BigDecimal totalExpense = Lambda.sumFrom(
					dealerExpenseFacts.get(dealerID),
					DealerIncomeExpenseFact.class).getAmount();
			final ReportDataDetailAmount amount = new ReportDataDetailAmount();
			amount.setAmount(totalExpense.doubleValue() / (monthOfYear * 1.0));
			dealerDetails.get(dealerID).setAmount(amount);
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
	public DealerExpensePercentageReportCalculator calcCurrentMargins(
			final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFacts,
			final JournalOp op) {
		for (final Integer dealerID : dealerRevenueFacts.keySet()) {
			final BigDecimal totalMargin = Lambda.sumFrom(
					dealerRevenueFacts.get(dealerID),
					DealerIncomeRevenueFact.class).getMargin();
			final ReportDataDetailAmount amount = new ReportDataDetailAmount();
			amount.setAmount(totalMargin.doubleValue() / (monthOfYear * 1.0));
			amount.setPercentage(dealerDetails.get(dealerID).getAmount().getAmount() / amount.getAmount());
			dealerDetails.get(dealerID).setCurrentMargin(amount);
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
	public DealerExpensePercentageReportCalculator calcPreviousMargins(
			final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFacts,
			final JournalOp op) {
		for (final Integer dealerID : dealerRevenueFacts.keySet()) {
			final BigDecimal totalMargin = Lambda.sumFrom(
					dealerRevenueFacts.get(dealerID),
					DealerIncomeRevenueFact.class).getMargin();
			final ReportDataDetailAmount amount = new ReportDataDetailAmount();
			amount.setAmount(totalMargin.doubleValue() / 12.0);
			amount.setPercentage(dealerDetails.get(dealerID).getAmount().getAmount() / amount.getAmount());
			dealerDetails.get(dealerID).setPreviousMargin(amount);
		}
		return this;
	}
	
}
