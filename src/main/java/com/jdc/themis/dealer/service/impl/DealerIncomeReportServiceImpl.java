package com.jdc.themis.dealer.service.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

import javax.time.calendar.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.lambdaj.Lambda;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.jdc.themis.dealer.data.dao.ReportDAO;
import com.jdc.themis.dealer.domain.DealerIncomeExpenseFact;
import com.jdc.themis.dealer.domain.DealerIncomeRevenueFact;
import com.jdc.themis.dealer.service.DealerIncomeReportService;
import com.jdc.themis.dealer.service.RefDataQueryService;
import com.jdc.themis.dealer.utils.Performance;
import com.jdc.themis.dealer.web.domain.DealerDetail;
import com.jdc.themis.dealer.web.domain.ImportReportDataRequest;
import com.jdc.themis.dealer.web.domain.QueryReportDataResponse;
import com.jdc.themis.dealer.web.domain.ReportDataDealerDetail;
import com.jdc.themis.dealer.web.domain.ReportDataDealerDetailAmount;
import com.jdc.themis.dealer.web.domain.ReportDataDetail;

import fj.data.Option;

@Service
public class DealerIncomeReportServiceImpl implements DealerIncomeReportService {

	@Autowired
	private ReportDAO reportDAL;
	@Autowired
	private RefDataQueryService refDataDAL;

	public RefDataQueryService getRefDataDAL() {
		return refDataDAL;
	}

	public void setRefDataDAL(RefDataQueryService refDataDAL) {
		this.refDataDAL = refDataDAL;
	}

	public ReportDAO getReportDAL() {
		return reportDAL;
	}

	public void setReportDAL(ReportDAO reportDAL) {
		this.reportDAL = reportDAL;
	}

	@Override
	public void importReportData(final ImportReportDataRequest request) {
		Preconditions.checkNotNull(request.getFromDate(), "from date can't be null");
		Preconditions.checkNotNull(request.getToDate(), "to date can't be null");
		
		reportDAL.importVehicleSalesJournal(LocalDate.parse(request.getFromDate()));
		reportDAL.importSalesServiceJournal(LocalDate.parse(request.getFromDate()));
		reportDAL.importGeneralJournal(LocalDate.parse(request.getFromDate()));
		reportDAL.importTaxJournal(LocalDate.parse(request.getFromDate()));
	}

	private enum GetDealerIDFromRevenueFunction implements Function<DealerIncomeRevenueFact, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(final DealerIncomeRevenueFact item) {
	        return item.getDealerID();
	    }
	}
	private enum GetDealerIDFromExpenseFunction implements Function<DealerIncomeExpenseFact, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(final DealerIncomeExpenseFact item) {
	        return item.getDealerID();
	    }
	}
	private enum GetDealerIDFromReportDetailFunction implements Function<ReportDataDealerDetail, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(final ReportDataDealerDetail item) {
	        return item.getId();
	    }
	}
	
	@Override
	@Performance
	public QueryReportDataResponse queryYearlyOverallIncomeReport(final Integer year, final Option<Integer> monthOfYear) {
		Preconditions.checkNotNull(year, "year can't be null");
		final QueryReportDataResponse response = new QueryReportDataResponse();
		response.setReportName("YearlyOverallIncomeReport");
		
		int previousYear = year - 1;
		final ReportDataDetail reportDetailPreviousYear = getReportDataDetail(previousYear, Option.<ReportDataDetail>none());
		reportDetailPreviousYear.setYear(previousYear);
		response.getDetail().add(reportDetailPreviousYear);
		
		final ReportDataDetail reportDetailCurrentYear = getReportDataDetail(year, Option.<ReportDataDetail>some(reportDetailPreviousYear));
		reportDetailCurrentYear.setYear(year);
		response.getDetail().add(reportDetailCurrentYear);
		
		return response;
	}
	
	private ReportDataDetail getReportDataDetail(final Integer year, final Option<ReportDataDetail> previousYearDetail) {
		final ReportDataDetail reportDetail = new ReportDataDetail();
		reportDetail.setYear(year);
		final Map<Integer, ReportDataDealerDetail> dealerDetails = Maps.newHashMap();
		for ( final DealerDetail dealer : refDataDAL.getDealers() ) {
			dealerDetails.put(dealer.getId(), new ReportDataDealerDetail());
			dealerDetails.get(dealer.getId()).setId(dealer.getId());
			dealerDetails.get(dealer.getId()).setName(dealer.getName());
			dealerDetails.get(dealer.getId()).setCode(dealer.getCode());
		}
		
	    Option<Map<Integer, ReportDataDealerDetail>> dealerPreviousYearDetailOption = Option.<Map<Integer, ReportDataDealerDetail>>none();
		if ( previousYearDetail.isSome() ) {
			final Map<Integer, ReportDataDealerDetail> dealerPreviousYearDetails = 
					Maps.uniqueIndex(previousYearDetail.some().getDetail(), GetDealerIDFromReportDetailFunction.INSTANCE);
			dealerPreviousYearDetailOption = Option.<Map<Integer, ReportDataDealerDetail>>some(dealerPreviousYearDetails);
		}
		
		// Get all revenues
		final Collection<DealerIncomeRevenueFact> revenueFacts = 
				reportDAL.getDealerIncomeRevenueFacts(year, 
						Lists.newArrayList(new Integer[]{}), 
						Lists.newArrayList(new Integer[]{}), 
						Lists.newArrayList(new Integer[]{}), 
						Lists.newArrayList(new String[]{"新轿车零售", "新货车零售", "附加产品业务", "二手车零售", "工时", "配件收入"}), 
						Lists.newArrayList(new Integer[]{}));
		
		final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFacts = Multimaps
				.index(revenueFacts, GetDealerIDFromRevenueFunction.INSTANCE);
		// Calculate total revenue
		for ( final Integer dealerID : dealerRevenueFacts.keySet() ) {
			final BigDecimal totalAmount = Lambda.sumFrom(dealerRevenueFacts.get(dealerID), DealerIncomeRevenueFact.class).getAmount();
			final ReportDataDealerDetailAmount amount = new ReportDataDealerDetailAmount();
			amount.setAmount(totalAmount.doubleValue());
			if ( dealerPreviousYearDetailOption.isSome() 
					&& dealerPreviousYearDetailOption.some().get(dealerID).getRevenue().getAmount() != 0.0) {
				// YOY Percentage = (this year amount - last year amount) / last year amount
				amount.setPercentage((totalAmount.doubleValue() - dealerPreviousYearDetailOption.some().get(dealerID).getRevenue().getAmount()) / 
						dealerPreviousYearDetailOption.some().get(dealerID).getRevenue().getAmount());
			}
			dealerDetails.get(dealerID).setRevenue(amount);
		}
		// Calculate total margin
		for ( final Integer dealerID : dealerRevenueFacts.keySet() ) {
			final BigDecimal totalMargin = Lambda.sumFrom(dealerRevenueFacts.get(dealerID), DealerIncomeRevenueFact.class).getMargin();
			final ReportDataDealerDetailAmount amount = new ReportDataDealerDetailAmount();
			amount.setAmount(totalMargin.doubleValue());
			if ( dealerPreviousYearDetailOption.isSome()
					&& dealerPreviousYearDetailOption.some().get(dealerID).getMargin().getAmount() != 0.0) {
				// YOY Percentage = (this year amount - last year amount) / last year amount
				amount.setPercentage((totalMargin.doubleValue() - dealerPreviousYearDetailOption.some().get(dealerID).getMargin().getAmount()) / 
						dealerPreviousYearDetailOption.some().get(dealerID).getMargin().getAmount());
			}
			dealerDetails.get(dealerID).setMargin(amount);
		}
		// Get all expenses
		final Collection<DealerIncomeExpenseFact> expenseFacts = 
				reportDAL.getDealerIncomeExpenseFacts(year, 
						Lists.newArrayList(new Integer[]{}), 
						Lists.newArrayList(new Integer[]{}), 
						Lists.newArrayList(new Integer[]{}), 
						Lists.newArrayList(new String[]{"变动费用", "销售费用", "人工费用", "半固定费用", "固定费用"}), 
						Lists.newArrayList(new Integer[]{}));
		
		final ImmutableListMultimap<Integer, DealerIncomeExpenseFact> dealerExpenseFacts = Multimaps
				.index(expenseFacts, GetDealerIDFromExpenseFunction.INSTANCE);
		// Get total expense
		for ( final Integer dealerID : dealerExpenseFacts.keySet() ) {
			final BigDecimal totalExpense = Lambda.sumFrom(dealerExpenseFacts.get(dealerID), DealerIncomeExpenseFact.class).getAmount();
			final ReportDataDealerDetailAmount amount = new ReportDataDealerDetailAmount();
			amount.setAmount(totalExpense.doubleValue());
			if ( dealerPreviousYearDetailOption.isSome() && 
					dealerPreviousYearDetailOption.some().get(dealerID).getExpense().getAmount() != 0.0) {
				// YOY Percentage = (this year amount - last year amount) / last year amount
				amount.setPercentage((totalExpense.doubleValue() - dealerPreviousYearDetailOption.some().get(dealerID).getExpense().getAmount()) / 
						dealerPreviousYearDetailOption.some().get(dealerID).getExpense().getAmount());
			}
			dealerDetails.get(dealerID).setExpense(amount);
		}
		
		reportDetail.getDetail().addAll(dealerDetails.values());
		
		return reportDetail;
	}

}
