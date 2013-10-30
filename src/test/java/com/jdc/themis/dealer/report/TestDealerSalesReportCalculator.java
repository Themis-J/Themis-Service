package com.jdc.themis.dealer.report;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;

import javax.time.calendar.LocalDate;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import com.jdc.themis.dealer.domain.DealerIncomeRevenueFact;
import com.jdc.themis.dealer.domain.ReportTime;
import com.jdc.themis.dealer.web.domain.DealerDetail;
import com.jdc.themis.dealer.web.domain.ReportDealerSalesDataList;

import fj.data.Option;

public class TestDealerSalesReportCalculator {

	private Collection<DealerDetail> dealers;

	@Before
	public void setUp() {
		final DealerDetail dealer1 = new DealerDetail();
		dealer1.setId(1);
		dealer1.setName("Dealer1");
		final DealerDetail dealer2 = new DealerDetail();
		dealer2.setId(2);
		dealer2.setName("Dealer2");
		final DealerDetail dealer3 = new DealerDetail();
		dealer3.setId(3);
		dealer3.setName("Dealer3");
		final DealerDetail dealer4 = new DealerDetail();
		dealer4.setId(4);
		dealer4.setName("Dealer4");
		final DealerDetail dealer5 = new DealerDetail();
		dealer5.setId(5);
		dealer5.setName("Dealer5");
		final DealerDetail dealer6 = new DealerDetail();
		dealer6.setId(6);
		dealer6.setName("Dealer6");
		final DealerDetail dealer7 = new DealerDetail();
		dealer7.setId(7);
		dealer7.setName("Dealer7");
		final DealerDetail dealer8 = new DealerDetail();
		dealer8.setId(8);
		dealer8.setName("Dealer8");
		final DealerDetail dealer9 = new DealerDetail();
		dealer9.setId(9);
		dealer9.setName("Dealer9");
		final DealerDetail dealer10 = new DealerDetail();
		dealer10.setId(10);
		dealer10.setName("Dealer10");
		final DealerDetail dealer11 = new DealerDetail();
		dealer11.setId(11);
		dealer11.setName("Dealer11");
		final DealerDetail dealer12 = new DealerDetail();
		dealer12.setId(12);
		dealer12.setName("Dealer12");
		dealers = Lists.newArrayList(dealer1, dealer2, dealer3, dealer4,
				dealer5, dealer6, dealer7, dealer8, dealer9, dealer10,
				dealer11, dealer12);
	}

	@Test
	public void calcOverallSalesForYearReport() {
		final DealerSalesReportCalculator calc = new DealerSalesReportCalculator(dealers, 2013);
		final ReportTime time201308 = new ReportTime();
		time201308.setId(1L);
		time201308.setMonthOfYear(8);
		time201308.setYear(2013);
		time201308.setValidDate(LocalDate.of(2013, 8, 1));

		final ReportTime time201307 = new ReportTime();
		time201307.setId(2L);
		time201307.setMonthOfYear(7);
		time201307.setYear(2013);
		time201307.setValidDate(LocalDate.of(2013, 7, 1));
		final DealerIncomeRevenueFact fact1 = new DealerIncomeRevenueFact();
		fact1.setTimeID(time201308.getId());
		fact1.setDealerID(1);
		fact1.setDepartmentID(1);
		fact1.setItemID(1L);
		fact1.setAmount(new BigDecimal("1000.0"));
		fact1.setMargin(new BigDecimal("2000.0"));
		fact1.setCount(20);
		// skip timestamp and time end
		final DealerIncomeRevenueFact fact2 = new DealerIncomeRevenueFact();
		fact2.setTimeID(time201308.getId());
		fact2.setDealerID(1);
		fact2.setDepartmentID(2);
		fact2.setItemID(1L);
		fact2.setAmount(new BigDecimal("2000.0"));
		fact2.setMargin(new BigDecimal("3000.0"));
		fact2.setCount(30);
		final DealerIncomeRevenueFact fact3 = new DealerIncomeRevenueFact();
		fact3.setTimeID(time201307.getId());
		fact3.setDealerID(2);
		fact3.setDepartmentID(2);
		fact3.setItemID(2L);
		fact3.setAmount(new BigDecimal("2020.0"));
		fact3.setMargin(new BigDecimal("3030.0"));
		fact3.setCount(40);

		final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFacts = Multimaps
				.index(Lists.newArrayList(fact1, fact2, fact3),
						GetDealerIDFromRevenueFunction.INSTANCE);

		final ReportDealerSalesDataList detail = calc.calcOverall(dealerRevenueFacts, JournalOp.SUM).getReportDetail();
		System.out.println(detail);
		Assert.assertNotNull(detail);
		Assert.assertEquals(new BigInteger((fact1.getCount() + fact2.getCount()) + "").doubleValue(), detail.getDetail().get(0).getOverall().getAmount());
		Assert.assertEquals((fact3.getCount().doubleValue()) / 9.0, detail.getDetail().get(0).getOverall().getReference());
	}
	
	@Test
	public void calcOverallSalesForYearReportWithPercentage() {
		final DealerSalesReportCalculator calc = new DealerSalesReportCalculator(dealers, 2013);
		final ReportTime time201308 = new ReportTime();
		time201308.setId(1L);
		time201308.setMonthOfYear(8);
		time201308.setYear(2013);
		time201308.setValidDate(LocalDate.of(2013, 8, 1));

		final ReportTime time201307 = new ReportTime();
		time201307.setId(2L);
		time201307.setMonthOfYear(7);
		time201307.setYear(2013);
		time201307.setValidDate(LocalDate.of(2013, 7, 1));
		
		final ReportTime time201207 = new ReportTime();
		time201207.setId(3L);
		time201207.setMonthOfYear(7);
		time201207.setYear(2012);
		time201207.setValidDate(LocalDate.of(2012, 7, 1));
		
		final DealerIncomeRevenueFact fact1 = new DealerIncomeRevenueFact();
		fact1.setTimeID(time201308.getId());
		fact1.setDealerID(1);
		fact1.setDepartmentID(1);
		fact1.setItemID(1L);
		fact1.setAmount(new BigDecimal("1000.0"));
		fact1.setMargin(new BigDecimal("2000.0"));
		fact1.setCount(20);
		// skip timestamp and time end
		final DealerIncomeRevenueFact fact2 = new DealerIncomeRevenueFact();
		fact2.setTimeID(time201308.getId());
		fact2.setDealerID(1);
		fact2.setDepartmentID(2);
		fact2.setItemID(1L);
		fact2.setAmount(new BigDecimal("2000.0"));
		fact2.setMargin(new BigDecimal("3000.0"));
		fact2.setCount(30);
		final DealerIncomeRevenueFact fact3 = new DealerIncomeRevenueFact();
		fact3.setTimeID(time201307.getId());
		fact3.setDealerID(2);
		fact3.setDepartmentID(2);
		fact3.setItemID(2L);
		fact3.setAmount(new BigDecimal("2020.0"));
		fact3.setMargin(new BigDecimal("3030.0"));
		fact3.setCount(40);
		
		final DealerIncomeRevenueFact fact4 = new DealerIncomeRevenueFact();
		fact4.setTimeID(time201207.getId());
		fact4.setDealerID(2);
		fact4.setDepartmentID(2);
		fact4.setItemID(2L);
		fact4.setAmount(new BigDecimal("1010.0"));
		fact4.setMargin(new BigDecimal("3030.0"));
		fact4.setCount(40);

		final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFactsPrevious = Multimaps
				.index(Lists.newArrayList(fact4),
						GetDealerIDFromRevenueFunction.INSTANCE);
		
		final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFacts = Multimaps
				.index(Lists.newArrayList(fact1, fact2, fact3),
						GetDealerIDFromRevenueFunction.INSTANCE);

		final ReportDealerSalesDataList detailPrevious = calc.calcOverall(dealerRevenueFactsPrevious, JournalOp.SUM).getReportDetail();
		final ReportDealerSalesDataList detail = calc.withPrevious(Option.<ReportDealerSalesDataList>some(detailPrevious)).calcOverall(dealerRevenueFacts, JournalOp.SUM).getReportDetail();
		System.out.println(detail);
		Assert.assertNotNull(detail);
		Assert.assertEquals(fact1.getCount().doubleValue() + fact2.getCount().doubleValue(), detail.getDetail().get(0).getOverall().getAmount());
		Assert.assertEquals((fact3.getCount().doubleValue()) / 9.0, detail.getDetail().get(0).getOverall().getReference());
		Assert.assertEquals((fact3.getCount().doubleValue() - fact4.getCount().doubleValue()) / fact4.getCount().doubleValue(), detail.getDetail().get(1).getOverall().getPercentage());
	}
	
	@Test
	public void calcRetailSalesForYearReport() {
		final DealerSalesReportCalculator calc = new DealerSalesReportCalculator(dealers, 2013);
		final ReportTime time201308 = new ReportTime();
		time201308.setId(1L);
		time201308.setMonthOfYear(8);
		time201308.setYear(2013);
		time201308.setValidDate(LocalDate.of(2013, 8, 1));

		final ReportTime time201307 = new ReportTime();
		time201307.setId(2L);
		time201307.setMonthOfYear(7);
		time201307.setYear(2013);
		time201307.setValidDate(LocalDate.of(2013, 7, 1));
		final DealerIncomeRevenueFact fact1 = new DealerIncomeRevenueFact();
		fact1.setTimeID(time201308.getId());
		fact1.setDealerID(1);
		fact1.setDepartmentID(1);
		fact1.setItemID(1L);
		fact1.setAmount(new BigDecimal("1000.0"));
		fact1.setMargin(new BigDecimal("2000.0"));
		fact1.setCount(20);
		// skip timestamp and time end
		final DealerIncomeRevenueFact fact2 = new DealerIncomeRevenueFact();
		fact2.setTimeID(time201308.getId());
		fact2.setDealerID(1);
		fact2.setDepartmentID(2);
		fact2.setItemID(1L);
		fact2.setAmount(new BigDecimal("2000.0"));
		fact2.setMargin(new BigDecimal("3000.0"));
		fact2.setCount(30);
		final DealerIncomeRevenueFact fact3 = new DealerIncomeRevenueFact();
		fact3.setTimeID(time201307.getId());
		fact3.setDealerID(2);
		fact3.setDepartmentID(2);
		fact3.setItemID(2L);
		fact3.setAmount(new BigDecimal("2020.0"));
		fact3.setMargin(new BigDecimal("3030.0"));
		fact3.setCount(40);

		final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFacts = Multimaps
				.index(Lists.newArrayList(fact1, fact2, fact3),
						GetDealerIDFromRevenueFunction.INSTANCE);

		final ReportDealerSalesDataList detail = calc.calcRetail(dealerRevenueFacts, JournalOp.SUM).getReportDetail();
		System.out.println(detail);
		Assert.assertNotNull(detail);
		Assert.assertEquals(new BigInteger((fact1.getCount() + fact2.getCount()) + "").doubleValue(), detail.getDetail().get(0).getRetail().getAmount());
		Assert.assertEquals((fact3.getCount().doubleValue()) / 9.0, detail.getDetail().get(0).getRetail().getReference());
	}
	
	@Test
	public void calcRetailSalesForYearReportWithPercentage() {
		final DealerSalesReportCalculator calc = new DealerSalesReportCalculator(dealers, 2013);
		final ReportTime time201308 = new ReportTime();
		time201308.setId(1L);
		time201308.setMonthOfYear(8);
		time201308.setYear(2013);
		time201308.setValidDate(LocalDate.of(2013, 8, 1));

		final ReportTime time201307 = new ReportTime();
		time201307.setId(2L);
		time201307.setMonthOfYear(7);
		time201307.setYear(2013);
		time201307.setValidDate(LocalDate.of(2013, 7, 1));
		
		final ReportTime time201207 = new ReportTime();
		time201207.setId(3L);
		time201207.setMonthOfYear(7);
		time201207.setYear(2012);
		time201207.setValidDate(LocalDate.of(2012, 7, 1));
		
		final DealerIncomeRevenueFact fact1 = new DealerIncomeRevenueFact();
		fact1.setTimeID(time201308.getId());
		fact1.setDealerID(1);
		fact1.setDepartmentID(1);
		fact1.setItemID(1L);
		fact1.setAmount(new BigDecimal("1000.0"));
		fact1.setMargin(new BigDecimal("2000.0"));
		fact1.setCount(20);
		// skip timestamp and time end
		final DealerIncomeRevenueFact fact2 = new DealerIncomeRevenueFact();
		fact2.setTimeID(time201308.getId());
		fact2.setDealerID(1);
		fact2.setDepartmentID(2);
		fact2.setItemID(1L);
		fact2.setAmount(new BigDecimal("2000.0"));
		fact2.setMargin(new BigDecimal("3000.0"));
		fact2.setCount(30);
		final DealerIncomeRevenueFact fact3 = new DealerIncomeRevenueFact();
		fact3.setTimeID(time201307.getId());
		fact3.setDealerID(2);
		fact3.setDepartmentID(2);
		fact3.setItemID(2L);
		fact3.setAmount(new BigDecimal("2020.0"));
		fact3.setMargin(new BigDecimal("3030.0"));
		fact3.setCount(40);
		
		final DealerIncomeRevenueFact fact4 = new DealerIncomeRevenueFact();
		fact4.setTimeID(time201207.getId());
		fact4.setDealerID(2);
		fact4.setDepartmentID(2);
		fact4.setItemID(2L);
		fact4.setAmount(new BigDecimal("1010.0"));
		fact4.setMargin(new BigDecimal("3030.0"));
		fact4.setCount(40);

		final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFactsPrevious = Multimaps
				.index(Lists.newArrayList(fact4),
						GetDealerIDFromRevenueFunction.INSTANCE);
		
		final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFacts = Multimaps
				.index(Lists.newArrayList(fact1, fact2, fact3),
						GetDealerIDFromRevenueFunction.INSTANCE);

		final ReportDealerSalesDataList detailPrevious = calc.calcRetail(dealerRevenueFactsPrevious, JournalOp.SUM).getReportDetail();
		final ReportDealerSalesDataList detail = calc.withPrevious(Option.<ReportDealerSalesDataList>some(detailPrevious)).calcRetail(dealerRevenueFacts, JournalOp.SUM).getReportDetail();
		System.out.println(detail);
		Assert.assertNotNull(detail);
		Assert.assertEquals(fact1.getCount().doubleValue() + fact2.getCount().doubleValue(), detail.getDetail().get(0).getRetail().getAmount());
		Assert.assertEquals((fact3.getCount().doubleValue()) / 9.0, detail.getDetail().get(0).getRetail().getReference());
		Assert.assertEquals((fact3.getCount().doubleValue() - fact4.getCount().doubleValue()) / fact4.getCount().doubleValue(), detail.getDetail().get(1).getRetail().getPercentage());
	}
	
	@Test
	public void calcWholesaleSalesForYearReport() {
		final DealerSalesReportCalculator calc = new DealerSalesReportCalculator(dealers, 2013);
		final ReportTime time201308 = new ReportTime();
		time201308.setId(1L);
		time201308.setMonthOfYear(8);
		time201308.setYear(2013);
		time201308.setValidDate(LocalDate.of(2013, 8, 1));

		final ReportTime time201307 = new ReportTime();
		time201307.setId(2L);
		time201307.setMonthOfYear(7);
		time201307.setYear(2013);
		time201307.setValidDate(LocalDate.of(2013, 7, 1));
		final DealerIncomeRevenueFact fact1 = new DealerIncomeRevenueFact();
		fact1.setTimeID(time201308.getId());
		fact1.setDealerID(1);
		fact1.setDepartmentID(1);
		fact1.setItemID(1L);
		fact1.setAmount(new BigDecimal("1000.0"));
		fact1.setMargin(new BigDecimal("2000.0"));
		fact1.setCount(20);
		// skip timestamp and time end
		final DealerIncomeRevenueFact fact2 = new DealerIncomeRevenueFact();
		fact2.setTimeID(time201308.getId());
		fact2.setDealerID(1);
		fact2.setDepartmentID(2);
		fact2.setItemID(1L);
		fact2.setAmount(new BigDecimal("2000.0"));
		fact2.setMargin(new BigDecimal("3000.0"));
		fact2.setCount(30);
		final DealerIncomeRevenueFact fact3 = new DealerIncomeRevenueFact();
		fact3.setTimeID(time201307.getId());
		fact3.setDealerID(2);
		fact3.setDepartmentID(2);
		fact3.setItemID(2L);
		fact3.setAmount(new BigDecimal("2020.0"));
		fact3.setMargin(new BigDecimal("3030.0"));
		fact3.setCount(40);

		final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFacts = Multimaps
				.index(Lists.newArrayList(fact1, fact2, fact3),
						GetDealerIDFromRevenueFunction.INSTANCE);

		final ReportDealerSalesDataList detail = calc.calcWholesale(dealerRevenueFacts, JournalOp.SUM).getReportDetail();
		System.out.println(detail);
		Assert.assertNotNull(detail);
		Assert.assertEquals(new BigInteger((fact1.getCount() + fact2.getCount()) + "").doubleValue(), detail.getDetail().get(0).getWholesale().getAmount());
		Assert.assertEquals((fact3.getCount().doubleValue()) / 9.0, detail.getDetail().get(0).getWholesale().getReference());
	}
	
	@Test
	public void calcWholesaleSalesForYearReportWithPercentage() {
		final DealerSalesReportCalculator calc = new DealerSalesReportCalculator(dealers, 2013);
		final ReportTime time201308 = new ReportTime();
		time201308.setId(1L);
		time201308.setMonthOfYear(8);
		time201308.setYear(2013);
		time201308.setValidDate(LocalDate.of(2013, 8, 1));

		final ReportTime time201307 = new ReportTime();
		time201307.setId(2L);
		time201307.setMonthOfYear(7);
		time201307.setYear(2013);
		time201307.setValidDate(LocalDate.of(2013, 7, 1));
		
		final ReportTime time201207 = new ReportTime();
		time201207.setId(3L);
		time201207.setMonthOfYear(7);
		time201207.setYear(2012);
		time201207.setValidDate(LocalDate.of(2012, 7, 1));
		
		final DealerIncomeRevenueFact fact1 = new DealerIncomeRevenueFact();
		fact1.setTimeID(time201308.getId());
		fact1.setDealerID(1);
		fact1.setDepartmentID(1);
		fact1.setItemID(1L);
		fact1.setAmount(new BigDecimal("1000.0"));
		fact1.setMargin(new BigDecimal("2000.0"));
		fact1.setCount(20);
		// skip timestamp and time end
		final DealerIncomeRevenueFact fact2 = new DealerIncomeRevenueFact();
		fact2.setTimeID(time201308.getId());
		fact2.setDealerID(1);
		fact2.setDepartmentID(2);
		fact2.setItemID(1L);
		fact2.setAmount(new BigDecimal("2000.0"));
		fact2.setMargin(new BigDecimal("3000.0"));
		fact2.setCount(30);
		final DealerIncomeRevenueFact fact3 = new DealerIncomeRevenueFact();
		fact3.setTimeID(time201307.getId());
		fact3.setDealerID(2);
		fact3.setDepartmentID(2);
		fact3.setItemID(2L);
		fact3.setAmount(new BigDecimal("2020.0"));
		fact3.setMargin(new BigDecimal("3030.0"));
		fact3.setCount(40);
		
		final DealerIncomeRevenueFact fact4 = new DealerIncomeRevenueFact();
		fact4.setTimeID(time201207.getId());
		fact4.setDealerID(2);
		fact4.setDepartmentID(2);
		fact4.setItemID(2L);
		fact4.setAmount(new BigDecimal("1010.0"));
		fact4.setMargin(new BigDecimal("3030.0"));
		fact4.setCount(40);

		final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFactsPrevious = Multimaps
				.index(Lists.newArrayList(fact4),
						GetDealerIDFromRevenueFunction.INSTANCE);
		
		final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFacts = Multimaps
				.index(Lists.newArrayList(fact1, fact2, fact3),
						GetDealerIDFromRevenueFunction.INSTANCE);

		final ReportDealerSalesDataList detailPrevious = calc.calcWholesale(dealerRevenueFactsPrevious, JournalOp.SUM).getReportDetail();
		final ReportDealerSalesDataList detail = calc.withPrevious(Option.<ReportDealerSalesDataList>some(detailPrevious)).calcWholesale(dealerRevenueFacts, JournalOp.SUM).getReportDetail();
		System.out.println(detail);
		Assert.assertNotNull(detail);
		Assert.assertEquals(fact1.getCount().doubleValue() + fact2.getCount().doubleValue(), detail.getDetail().get(0).getWholesale().getAmount());
		Assert.assertEquals((fact3.getCount().doubleValue()) / 9.0, detail.getDetail().get(0).getWholesale().getReference());
		Assert.assertEquals((fact3.getCount().doubleValue() - fact4.getCount().doubleValue()) / fact4.getCount().doubleValue(), detail.getDetail().get(1).getWholesale().getPercentage());
	}
	
	@Test
	public void calcOtherSalesForYearReport() {
		final DealerSalesReportCalculator calc = new DealerSalesReportCalculator(dealers, 2013);
		final ReportTime time201308 = new ReportTime();
		time201308.setId(1L);
		time201308.setMonthOfYear(8);
		time201308.setYear(2013);
		time201308.setValidDate(LocalDate.of(2013, 8, 1));

		final ReportTime time201307 = new ReportTime();
		time201307.setId(2L);
		time201307.setMonthOfYear(7);
		time201307.setYear(2013);
		time201307.setValidDate(LocalDate.of(2013, 7, 1));
		final DealerIncomeRevenueFact fact1 = new DealerIncomeRevenueFact();
		fact1.setTimeID(time201308.getId());
		fact1.setDealerID(1);
		fact1.setDepartmentID(1);
		fact1.setItemID(1L);
		fact1.setAmount(new BigDecimal("1000.0"));
		fact1.setMargin(new BigDecimal("2000.0"));
		fact1.setCount(20);
		// skip timestamp and time end
		final DealerIncomeRevenueFact fact2 = new DealerIncomeRevenueFact();
		fact2.setTimeID(time201308.getId());
		fact2.setDealerID(1);
		fact2.setDepartmentID(2);
		fact2.setItemID(1L);
		fact2.setAmount(new BigDecimal("2000.0"));
		fact2.setMargin(new BigDecimal("3000.0"));
		fact2.setCount(30);
		final DealerIncomeRevenueFact fact3 = new DealerIncomeRevenueFact();
		fact3.setTimeID(time201307.getId());
		fact3.setDealerID(2);
		fact3.setDepartmentID(2);
		fact3.setItemID(2L);
		fact3.setAmount(new BigDecimal("2020.0"));
		fact3.setMargin(new BigDecimal("3030.0"));
		fact3.setCount(40);

		final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFacts = Multimaps
				.index(Lists.newArrayList(fact1, fact2, fact3),
						GetDealerIDFromRevenueFunction.INSTANCE);

		final ReportDealerSalesDataList detail = calc.calcOther(dealerRevenueFacts, JournalOp.SUM).getReportDetail();
		System.out.println(detail);
		Assert.assertNotNull(detail);
		Assert.assertEquals(new BigInteger((fact1.getCount() + fact2.getCount()) + "").doubleValue(), detail.getDetail().get(0).getOther().getAmount());
		Assert.assertEquals((fact3.getCount().doubleValue()) / 9.0, detail.getDetail().get(0).getOther().getReference());
	}
	
	@Test
	public void calcOtherSalesForYearReportWithPercentage() {
		final DealerSalesReportCalculator calc = new DealerSalesReportCalculator(dealers, 2013);
		final ReportTime time201308 = new ReportTime();
		time201308.setId(1L);
		time201308.setMonthOfYear(8);
		time201308.setYear(2013);
		time201308.setValidDate(LocalDate.of(2013, 8, 1));

		final ReportTime time201307 = new ReportTime();
		time201307.setId(2L);
		time201307.setMonthOfYear(7);
		time201307.setYear(2013);
		time201307.setValidDate(LocalDate.of(2013, 7, 1));
		
		final ReportTime time201207 = new ReportTime();
		time201207.setId(3L);
		time201207.setMonthOfYear(7);
		time201207.setYear(2012);
		time201207.setValidDate(LocalDate.of(2012, 7, 1));
		
		final DealerIncomeRevenueFact fact1 = new DealerIncomeRevenueFact();
		fact1.setTimeID(time201308.getId());
		fact1.setDealerID(1);
		fact1.setDepartmentID(1);
		fact1.setItemID(1L);
		fact1.setAmount(new BigDecimal("1000.0"));
		fact1.setMargin(new BigDecimal("2000.0"));
		fact1.setCount(20);
		// skip timestamp and time end
		final DealerIncomeRevenueFact fact2 = new DealerIncomeRevenueFact();
		fact2.setTimeID(time201308.getId());
		fact2.setDealerID(1);
		fact2.setDepartmentID(2);
		fact2.setItemID(1L);
		fact2.setAmount(new BigDecimal("2000.0"));
		fact2.setMargin(new BigDecimal("3000.0"));
		fact2.setCount(30);
		final DealerIncomeRevenueFact fact3 = new DealerIncomeRevenueFact();
		fact3.setTimeID(time201307.getId());
		fact3.setDealerID(2);
		fact3.setDepartmentID(2);
		fact3.setItemID(2L);
		fact3.setAmount(new BigDecimal("2020.0"));
		fact3.setMargin(new BigDecimal("3030.0"));
		fact3.setCount(40);
		
		final DealerIncomeRevenueFact fact4 = new DealerIncomeRevenueFact();
		fact4.setTimeID(time201207.getId());
		fact4.setDealerID(2);
		fact4.setDepartmentID(2);
		fact4.setItemID(2L);
		fact4.setAmount(new BigDecimal("1010.0"));
		fact4.setMargin(new BigDecimal("3030.0"));
		fact4.setCount(40);

		final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFactsPrevious = Multimaps
				.index(Lists.newArrayList(fact4),
						GetDealerIDFromRevenueFunction.INSTANCE);
		
		final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFacts = Multimaps
				.index(Lists.newArrayList(fact1, fact2, fact3),
						GetDealerIDFromRevenueFunction.INSTANCE);

		final ReportDealerSalesDataList detailPrevious = calc.calcOther(dealerRevenueFactsPrevious, JournalOp.SUM).getReportDetail();
		final ReportDealerSalesDataList detail = calc.withPrevious(Option.<ReportDealerSalesDataList>some(detailPrevious)).calcOther(dealerRevenueFacts, JournalOp.SUM).getReportDetail();
		System.out.println(detail);
		Assert.assertNotNull(detail);
		Assert.assertEquals(fact1.getCount().doubleValue() + fact2.getCount().doubleValue(), detail.getDetail().get(0).getOther().getAmount());
		Assert.assertEquals((fact3.getCount().doubleValue()) / 9.0, detail.getDetail().get(0).getOther().getReference());
		Assert.assertEquals((fact3.getCount().doubleValue() - fact4.getCount().doubleValue()) / fact4.getCount().doubleValue(), detail.getDetail().get(1).getOther().getPercentage());
	}
	
	private enum GetDealerIDFromRevenueFunction implements
			Function<DealerIncomeRevenueFact, Integer> {
		INSTANCE;

		@Override
		public Integer apply(final DealerIncomeRevenueFact item) {
			return item.getDealerID();
		}
	}
}
