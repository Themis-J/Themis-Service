package com.jdc.themis.dealer.report;

import static com.jdc.themis.dealer.report.ReportUtils.calcPercentage;
import static com.jdc.themis.dealer.report.ReportUtils.calcReference;

import java.util.Collection;
import java.util.Map;

import ch.lambdaj.Lambda;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Maps;
import com.jdc.themis.dealer.domain.DealerIncomeRevenueFact;
import com.jdc.themis.dealer.web.domain.DealerDetail;
import com.jdc.themis.dealer.web.domain.ReportDataDealerSalesDetail;
import com.jdc.themis.dealer.web.domain.ReportDataDetailAmount;
import com.jdc.themis.dealer.web.domain.ReportDealerSalesDataList;

import fj.data.Option;

/**
 * Calculate the overall sales report numbers by dealer. 
 * 
 * The class is not thread-safe.
 * 
 * @author Kai Chen
 *
 */
public class DealerSalesReportCalculator {

	/**
	 * Mapping between dealer id and dealer report detail.
	 */
	private final Map<Integer, ReportDataDealerSalesDetail> dealerDetails = Maps.newHashMap();
	
	private Integer year;
	private Option<Integer> monthOfYear = Option.<Integer>none();
	private Option<Map<Integer, ReportDataDealerSalesDetail>> dealerPreviousDetailOption = Option
			.<Map<Integer, ReportDataDealerSalesDetail>> none();
	
	public DealerSalesReportCalculator(final Collection<DealerDetail> dealers, final Integer year) {
		for (final DealerDetail dealer : dealers) {
			// initialize dealer details map for all dealers
			dealerDetails.put(dealer.getId(), new ReportDataDealerSalesDetail());
			dealerDetails.get(dealer.getId()).setId(dealer.getId());
			dealerDetails.get(dealer.getId()).setName(dealer.getName());
			dealerDetails.get(dealer.getId()).setCode(dealer.getCode());
		}
		
		this.year = year;
		
	}
	private enum GetDealerIDFromReportDetailFunction implements
			Function<ReportDataDealerSalesDetail, Integer> {
		INSTANCE;

		@Override
		public Integer apply(final ReportDataDealerSalesDetail item) {
			return item.getId();
		}
	}

	/**
	 * Populate and return a report details with time and dealer report numbers. 
	 * 
	 * @return
	 */
	public ReportDealerSalesDataList getReportDetail() {
		final ReportDealerSalesDataList reportDetail = new ReportDealerSalesDataList();
		reportDetail.setYear(year);
		if ( monthOfYear.isSome() ) {
			reportDetail.setMonth(monthOfYear.some());
		} 
		reportDetail.getDetail().addAll(dealerDetails.values());
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
	public DealerSalesReportCalculator withMonth(final Option<Integer> monthOfYear) {
		this.monthOfYear = monthOfYear;
		return this;
	}
	/**
	 * Set previous dealer report details.
	 * 
	 * @param previousDetail
	 * @return
	 */
	public DealerSalesReportCalculator withPrevious(final Option<ReportDealerSalesDataList> previousDetail) {
		if ( previousDetail.isNone() ) {
			return this;
		}
		final Map<Integer, ReportDataDealerSalesDetail> dealerPreviousDetails = Maps
				.uniqueIndex(previousDetail.some().getDetail(),
						GetDealerIDFromReportDetailFunction.INSTANCE);
		dealerPreviousDetailOption = Option
				.<Map<Integer, ReportDataDealerSalesDetail>> some(dealerPreviousDetails);
		return this;
	}
	
	/**
	 * Calculate the overall. 
	 * 
	 * @param dealerRevenueFacts
	 * @param op
	 * @return
	 */
	public DealerSalesReportCalculator calcOverall(
			final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFacts,
			final JournalOp op) {
		for (final Integer dealerID : dealerRevenueFacts.keySet()) {
			final Integer totalSales = Lambda.sumFrom(
					dealerRevenueFacts.get(dealerID),
					DealerIncomeRevenueFact.class).getCount();
			final ReportDataDetailAmount amount = new ReportDataDetailAmount();
			amount.setAmount(op == JournalOp.SUM ? totalSales.doubleValue()
					: totalSales.doubleValue() / (monthOfYear.some() * 1.0));

			if (dealerPreviousDetailOption.isSome()) {
				amount.setPercentage(calcPercentage(amount.getAmount(), dealerPreviousDetailOption
						.some().get(dealerID).getOverall().getAmount()));
			}
			dealerDetails.get(dealerID).setOverall(amount);
		}
		final Double reference = calcReference(Lambda.extract(dealerDetails.values(), 
												Lambda.on(ReportDataDealerSalesDetail.class).getOverall().getAmount()));
		Lambda.forEach(dealerDetails.values()).getOverall().setReference(reference);
		return this;
	}
	
	/**
	 * Calculate the retail. 
	 * 
	 * @param dealerRevenueFacts
	 * @param op
	 * @return
	 */
	public DealerSalesReportCalculator calcRetail(
			final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFacts,
			final JournalOp op) {
		for (final Integer dealerID : dealerRevenueFacts.keySet()) {
			final Integer totalSales = Lambda.sumFrom(
					dealerRevenueFacts.get(dealerID),
					DealerIncomeRevenueFact.class).getCount();
			final ReportDataDetailAmount amount = new ReportDataDetailAmount();
			amount.setAmount(op == JournalOp.SUM ? totalSales.doubleValue()
					: totalSales.doubleValue() / (monthOfYear.some() * 1.0));

			if (dealerPreviousDetailOption.isSome()) {
				amount.setPercentage(calcPercentage(amount.getAmount(), dealerPreviousDetailOption
						.some().get(dealerID).getRetail().getAmount()));
			}
			dealerDetails.get(dealerID).setRetail(amount);
		}
		final Double reference = calcReference(Lambda.extract(dealerDetails.values(), 
												Lambda.on(ReportDataDealerSalesDetail.class).getRetail().getAmount()));
		Lambda.forEach(dealerDetails.values()).getRetail().setReference(reference);
		return this;
	}
	
	/**
	 * Calculate the wholesale. 
	 * 
	 * @param dealerRevenueFacts
	 * @param op
	 * @return
	 */
	public DealerSalesReportCalculator calcWholesale(
			final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFacts,
			final JournalOp op) {
		for (final Integer dealerID : dealerRevenueFacts.keySet()) {
			final Integer totalSales = Lambda.sumFrom(
					dealerRevenueFacts.get(dealerID),
					DealerIncomeRevenueFact.class).getCount();
			final ReportDataDetailAmount amount = new ReportDataDetailAmount();
			amount.setAmount(op == JournalOp.SUM ? totalSales.doubleValue()
					: totalSales.doubleValue() / (monthOfYear.some() * 1.0));

			if (dealerPreviousDetailOption.isSome()) {
				amount.setPercentage(calcPercentage(amount.getAmount(), dealerPreviousDetailOption
						.some().get(dealerID).getWholesale().getAmount()));
			}
			dealerDetails.get(dealerID).setWholesale(amount);
		}
		final Double reference = calcReference(Lambda.extract(dealerDetails.values(), 
												Lambda.on(ReportDataDealerSalesDetail.class).getWholesale().getAmount()));
		Lambda.forEach(dealerDetails.values()).getWholesale().setReference(reference);
		return this;
	}
	
	/**
	 * Calculate the other. 
	 * 
	 * @param dealerRevenueFacts
	 * @param op
	 * @return
	 */
	public DealerSalesReportCalculator calcOther(
			final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFacts,
			final JournalOp op) {
		for (final Integer dealerID : dealerRevenueFacts.keySet()) {
			final Integer totalSales = Lambda.sumFrom(
					dealerRevenueFacts.get(dealerID),
					DealerIncomeRevenueFact.class).getCount();
			final ReportDataDetailAmount amount = new ReportDataDetailAmount();
			amount.setAmount(op == JournalOp.SUM ? totalSales.doubleValue()
					: totalSales.doubleValue() / (monthOfYear.some() * 1.0));

			if (dealerPreviousDetailOption.isSome()) {
				amount.setPercentage(calcPercentage(amount.getAmount(), dealerPreviousDetailOption
						.some().get(dealerID).getOther().getAmount()));
			}
			dealerDetails.get(dealerID).setOther(amount);
		}
		final Double reference = calcReference(Lambda.extract(dealerDetails.values(), 
												Lambda.on(ReportDataDealerSalesDetail.class).getOther().getAmount()));
		Lambda.forEach(dealerDetails.values()).getOther().setReference(reference);
		return this;
	}
}
