package com.jdc.themis.dealer.service.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
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
import com.jdc.themis.dealer.data.dao.RefDataDAO;
import com.jdc.themis.dealer.data.dao.ReportDAO;
import com.jdc.themis.dealer.domain.Dealer;
import com.jdc.themis.dealer.domain.DealerIncomeExpenseFact;
import com.jdc.themis.dealer.domain.DealerIncomeRevenueFact;
import com.jdc.themis.dealer.service.DealerIncomeReportService;
import com.jdc.themis.dealer.utils.Performance;
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
	private RefDataDAO refDataDAL;

	public RefDataDAO getRefDataDAL() {
		return refDataDAL;
	}

	public void setRefDataDAL(RefDataDAO refDataDAL) {
		this.refDataDAL = refDataDAL;
	}

	public ReportDAO getReportDAL() {
		return reportDAL;
	}

	public void setReportDAL(ReportDAO reportDAL) {
		this.reportDAL = reportDAL;
	}

	@Override
	public void importReportData(LocalDate validDate) {
		Preconditions.checkNotNull(validDate, "valid date can't be null");
		reportDAL.importVehicleSalesJournal(validDate);
		reportDAL.importSalesServiceJournal(validDate);
		reportDAL.importGeneralJournal(validDate);
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
	public QueryReportDataResponse queryYearlyOverallIncomeReport(final Integer year) {
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
		for ( final Dealer dealer : refDataDAL.getDealers() ) {
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
		final Collection<DealerIncomeRevenueFact> revenueFacts = reportDAL.getDealerIncomeRevenueFacts(year, Option.<Integer>none(), Option.<Integer>none());
		
		final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFacts = Multimaps
				.index(fj.data.List.iterableList(revenueFacts).filter(new fj.F<DealerIncomeRevenueFact, Boolean>() {
		
					private final List<String> excluding = Lists.newArrayList("非经营性损益进项", "其它进项");
					
					@Override
					public Boolean f(DealerIncomeRevenueFact a) {
						return !excluding.contains(reportDAL.getReportItem(((DealerIncomeRevenueFact) a).getItemID()).some());
					}
					
				}).toCollection(), GetDealerIDFromRevenueFunction.INSTANCE);
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
		final Collection<DealerIncomeExpenseFact> expenseFacts = reportDAL.getDealerIncomeExpenseFacts(year, Option.<Integer>none(), Option.<Integer>none());
		
		final ImmutableListMultimap<Integer, DealerIncomeExpenseFact> dealerExpenseFacts = Multimaps
				.index(fj.data.List.iterableList(expenseFacts).filter(new fj.F<DealerIncomeExpenseFact, Boolean>() {
		
					private final List<String> excluding = Lists.newArrayList("非经营性损益削项", "其它削项");
					
					@Override
					public Boolean f(DealerIncomeExpenseFact a) {
						return !excluding.contains(reportDAL.getReportItem(((DealerIncomeExpenseFact) a).getItemID()).some());
					}
					
				}).toCollection(), GetDealerIDFromExpenseFunction.INSTANCE);
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
		
		final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerNonRecurrentPnLRevenueFacts = Multimaps
				.index(fj.data.List.iterableList(revenueFacts).filter(new fj.F<DealerIncomeRevenueFact, Boolean>() {
		
					private final List<String> including = Lists.newArrayList("非经营性损益进项");
					
					@Override
					public Boolean f(DealerIncomeRevenueFact a) {
						return including.contains(reportDAL.getReportItem(((DealerIncomeRevenueFact) a).getItemID()).some());
					}
					
				}).toCollection(), GetDealerIDFromRevenueFunction.INSTANCE);
		for ( final Integer dealerID : dealerNonRecurrentPnLRevenueFacts.keySet() ) {
			final BigDecimal totalAmount = Lambda.sumFrom(dealerNonRecurrentPnLRevenueFacts.get(dealerID), DealerIncomeRevenueFact.class).getAmount();
			final ReportDataDealerDetailAmount amount = new ReportDataDealerDetailAmount();
			amount.setAmount(totalAmount.doubleValue());
			dealerDetails.get(dealerID).setNonRecurrentPnL(amount);
		}
		final ImmutableListMultimap<Integer, DealerIncomeExpenseFact> dealerNonRecurrentPnLExpenseFacts = Multimaps
				.index(fj.data.List.iterableList(expenseFacts).filter(new fj.F<DealerIncomeExpenseFact, Boolean>() {
		
					private final List<String> including = Lists.newArrayList("非经营性损益削项");
					
					@Override
					public Boolean f(DealerIncomeExpenseFact a) {
						return including.contains(reportDAL.getReportItem(((DealerIncomeExpenseFact) a).getItemID()).some());
					}
					
				}).toCollection(), GetDealerIDFromExpenseFunction.INSTANCE);
		// Get total expense
		for ( final Integer dealerID : dealerNonRecurrentPnLExpenseFacts.keySet() ) {
			final BigDecimal totalExpense = Lambda.sumFrom(dealerNonRecurrentPnLExpenseFacts.get(dealerID), DealerIncomeExpenseFact.class).getAmount();
			final ReportDataDealerDetailAmount amount = new ReportDataDealerDetailAmount();
			amount.setAmount(totalExpense.doubleValue());
			dealerDetails.get(dealerID).getNonRecurrentPnL().setAmount(dealerDetails.get(dealerID).getNonRecurrentPnL().getAmount() - totalExpense.doubleValue());
			
			if ( dealerPreviousYearDetailOption.isSome() &&
					dealerPreviousYearDetailOption.some().get(dealerID).getNonRecurrentPnL().getAmount() != 0.0) {
				// YOY Percentage = (this year amount - last year amount) / last year amount
				amount.setPercentage((dealerDetails.get(dealerID).getNonRecurrentPnL().getAmount().doubleValue() - dealerPreviousYearDetailOption.some().get(dealerID).getNonRecurrentPnL().getAmount()) / 
						dealerPreviousYearDetailOption.some().get(dealerID).getNonRecurrentPnL().getAmount());
			}
		}
		reportDetail.getDetail().addAll(dealerDetails.values());
		
		return reportDetail;
	}

}
