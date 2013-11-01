package com.jdc.themis.dealer.report;

import java.math.BigDecimal;
import java.util.Collection;

import javax.time.calendar.LocalDate;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.Before;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import com.jdc.themis.dealer.domain.DealerIncomeExpenseFact;
import com.jdc.themis.dealer.domain.DealerIncomeRevenueFact;
import com.jdc.themis.dealer.domain.ReportTime;
import com.jdc.themis.dealer.report.JournalOp;
import com.jdc.themis.dealer.web.domain.DealerDetail;
import com.jdc.themis.dealer.web.domain.ReportDataDetail;
import com.jdc.themis.dealer.web.domain.ReportDealerDataList;

import fj.data.Option;

public class TestDealerReportCalculator {

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
	public void calcMarginForYearReport() {
		final DealerReportCalculator calc = new DealerReportCalculator(dealers, 2013);
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

		final ReportDealerDataList detail = calc.calcMargins(dealerRevenueFacts, JournalOp.SUM).getReportDetail();
		System.out.println(detail);
		Assert.assertNotNull(detail);
		Assert.assertEquals(fact1.getMargin().add(fact2.getMargin()).doubleValue(), detail.getDetail().get(0).getMargin().getAmount());
		Assert.assertEquals((fact3.getMargin().doubleValue()) / 9.0, detail.getDetail().get(0).getMargin().getReference());
	}
	
	@Test
	public void calcMarginForYearReportWithPercentage() {
		final DealerReportCalculator calc = new DealerReportCalculator(dealers, 2013);
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

		final ReportDealerDataList detailPrevious = calc.calcMargins(dealerRevenueFactsPrevious, JournalOp.SUM).getReportDetail();
		final ReportDealerDataList detail = calc.withPrevious(Option.<ReportDealerDataList>some(detailPrevious)).calcMargins(dealerRevenueFacts, JournalOp.SUM).getReportDetail();
		System.out.println(detail);
		Assert.assertNotNull(detail);
		Assert.assertEquals(fact1.getMargin().add(fact2.getMargin()).doubleValue(), detail.getDetail().get(0).getMargin().getAmount());
		Assert.assertEquals((fact3.getMargin().doubleValue()) / 9.0, detail.getDetail().get(0).getMargin().getReference());
		Assert.assertEquals((fact3.getMargin().doubleValue() - fact4.getMargin().doubleValue()) / fact4.getMargin().doubleValue(), detail.getDetail().get(1).getMargin().getPercentage());
	}
	
	@Test
	public void calcRevenueForYearReport() {
		final DealerReportCalculator calc = new DealerReportCalculator(dealers, 2013);
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

		final ReportDealerDataList detail = calc.calcRevenues(dealerRevenueFacts, JournalOp.SUM).getReportDetail();
		System.out.println(detail);
		Assert.assertNotNull(detail);
		Assert.assertEquals(fact1.getAmount().add(fact2.getAmount()).doubleValue(), detail.getDetail().get(0).getRevenue().getAmount());
		Assert.assertEquals((fact3.getAmount().doubleValue()) / 9.0, detail.getDetail().get(0).getRevenue().getReference());
	}
	
	@Test
	public void calcRevenueForYearReportWithPercentage() {
		final DealerReportCalculator calc = new DealerReportCalculator(dealers, 2013);
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

		final ReportDealerDataList detailPrevious = calc.calcRevenues(dealerRevenueFactsPrevious, JournalOp.SUM).getReportDetail();
		final ReportDealerDataList detail = calc.withPrevious(Option.<ReportDealerDataList>some(detailPrevious)).calcRevenues(dealerRevenueFacts, JournalOp.SUM).getReportDetail();
		System.out.println(detail);
		Assert.assertNotNull(detail);
		Assert.assertEquals(fact1.getAmount().add(fact2.getAmount()).doubleValue(), detail.getDetail().get(0).getRevenue().getAmount());
		Assert.assertEquals((fact3.getAmount().doubleValue()) / 9.0, detail.getDetail().get(0).getRevenue().getReference());
		Assert.assertEquals((fact3.getAmount().doubleValue() - fact4.getAmount().doubleValue()) / fact4.getAmount().doubleValue(), detail.getDetail().get(1).getRevenue().getPercentage());
	}
	
	@Test
	public void calcExpenseForYearReport() {
		final DealerReportCalculator calc = new DealerReportCalculator(dealers, 2013);
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
		final DealerIncomeExpenseFact fact1 = new DealerIncomeExpenseFact();
		fact1.setTimeID(time201308.getId());
		fact1.setDealerID(1);
		fact1.setDepartmentID(1);
		fact1.setItemID(1L);
		fact1.setAmount(new BigDecimal("1000.0"));
		// skip timestamp and time end
		final DealerIncomeExpenseFact fact2 = new DealerIncomeExpenseFact();
		fact2.setTimeID(time201308.getId());
		fact2.setDealerID(1);
		fact2.setDepartmentID(2);
		fact2.setItemID(1L);
		fact2.setAmount(new BigDecimal("2000.0"));
		final DealerIncomeExpenseFact fact3 = new DealerIncomeExpenseFact();
		fact3.setTimeID(time201307.getId());
		fact3.setDealerID(2);
		fact3.setDepartmentID(2);
		fact3.setItemID(2L);
		fact3.setAmount(new BigDecimal("2020.0"));
		
		final ImmutableListMultimap<Integer, DealerIncomeExpenseFact> dealerExpenseFacts = Multimaps
				.index(Lists.newArrayList(fact1, fact2, fact3),
						GetDealerIDFromExpenseFunction.INSTANCE);

		final ReportDealerDataList detail = calc.calcExpenses(dealerExpenseFacts, JournalOp.SUM).getReportDetail();
		System.out.println(detail);
		Assert.assertNotNull(detail);
		Assert.assertEquals(fact1.getAmount().add(fact2.getAmount()).doubleValue(), detail.getDetail().get(0).getExpense().getAmount());
		Assert.assertEquals((fact3.getAmount().doubleValue()) / 9.0, detail.getDetail().get(0).getExpense().getReference());
	}
	
	@Test
	public void calcExpenseForYearReportWithPercentage() {
		final DealerReportCalculator calc = new DealerReportCalculator(dealers, 2013);
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
		
		final DealerIncomeExpenseFact fact1 = new DealerIncomeExpenseFact();
		fact1.setTimeID(time201308.getId());
		fact1.setDealerID(1);
		fact1.setDepartmentID(1);
		fact1.setItemID(1L);
		fact1.setAmount(new BigDecimal("1000.0"));
		// skip timestamp and time end
		final DealerIncomeExpenseFact fact2 = new DealerIncomeExpenseFact();
		fact2.setTimeID(time201308.getId());
		fact2.setDealerID(1);
		fact2.setDepartmentID(2);
		fact2.setItemID(1L);
		fact2.setAmount(new BigDecimal("2000.0"));
		final DealerIncomeExpenseFact fact3 = new DealerIncomeExpenseFact();
		fact3.setTimeID(time201307.getId());
		fact3.setDealerID(2);
		fact3.setDepartmentID(2);
		fact3.setItemID(2L);
		fact3.setAmount(new BigDecimal("2020.0"));
		
		final DealerIncomeExpenseFact fact4 = new DealerIncomeExpenseFact();
		fact4.setTimeID(time201207.getId());
		fact4.setDealerID(2);
		fact4.setDepartmentID(2);
		fact4.setItemID(2L);
		fact4.setAmount(new BigDecimal("1010.0"));
		
		final ImmutableListMultimap<Integer, DealerIncomeExpenseFact> dealerExpenseFactsPrevious = Multimaps
				.index(Lists.newArrayList(fact4),
						GetDealerIDFromExpenseFunction.INSTANCE);
		
		final ImmutableListMultimap<Integer, DealerIncomeExpenseFact> dealerExpenseFacts = Multimaps
				.index(Lists.newArrayList(fact1, fact2, fact3),
						GetDealerIDFromExpenseFunction.INSTANCE);

		final ReportDealerDataList detailPrevious = calc.calcExpenses(dealerExpenseFactsPrevious, JournalOp.SUM).getReportDetail();
		final ReportDealerDataList detail = calc.withPrevious(Option.<ReportDealerDataList>some(detailPrevious)).calcExpenses(dealerExpenseFacts, JournalOp.SUM).getReportDetail();
		System.out.println(detail);
		Assert.assertNotNull(detail);
		Assert.assertEquals(fact1.getAmount().add(fact2.getAmount()).doubleValue(), detail.getDetail().get(0).getExpense().getAmount());
		Assert.assertEquals((fact3.getAmount().doubleValue()) / 9.0, detail.getDetail().get(0).getExpense().getReference());
		Assert.assertEquals((fact3.getAmount().doubleValue() - fact4.getAmount().doubleValue()) / fact4.getAmount().doubleValue(), detail.getDetail().get(1).getExpense().getPercentage());
	}
	
	@Test
	public void calcExpenseForYearReportWithPercentage2() {
		final DealerReportCalculator calc = new DealerReportCalculator(dealers, 2013);
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
		
		final DealerIncomeExpenseFact fact1 = new DealerIncomeExpenseFact();
		fact1.setTimeID(time201308.getId());
		fact1.setDealerID(1);
		fact1.setDepartmentID(1);
		fact1.setItemID(1L);
		fact1.setAmount(new BigDecimal("-1000.0"));
		// skip timestamp and time end
		final DealerIncomeExpenseFact fact2 = new DealerIncomeExpenseFact();
		fact2.setTimeID(time201308.getId());
		fact2.setDealerID(1);
		fact2.setDepartmentID(2);
		fact2.setItemID(1L);
		fact2.setAmount(new BigDecimal("-2000.0"));
		final DealerIncomeExpenseFact fact3 = new DealerIncomeExpenseFact();
		fact3.setTimeID(time201307.getId());
		fact3.setDealerID(2);
		fact3.setDepartmentID(2);
		fact3.setItemID(2L);
		fact3.setAmount(new BigDecimal("-2020.0"));
		
		final DealerIncomeExpenseFact fact4 = new DealerIncomeExpenseFact();
		fact4.setTimeID(time201207.getId());
		fact4.setDealerID(2);
		fact4.setDepartmentID(2);
		fact4.setItemID(2L);
		fact4.setAmount(new BigDecimal("-1010.0"));
		
		final ImmutableListMultimap<Integer, DealerIncomeExpenseFact> dealerExpenseFactsPrevious = Multimaps
				.index(Lists.newArrayList(fact4),
						GetDealerIDFromExpenseFunction.INSTANCE);
		
		final ImmutableListMultimap<Integer, DealerIncomeExpenseFact> dealerExpenseFacts = Multimaps
				.index(Lists.newArrayList(fact1, fact2, fact3),
						GetDealerIDFromExpenseFunction.INSTANCE);

		final ReportDealerDataList detailPrevious = calc.calcExpenses(dealerExpenseFactsPrevious, JournalOp.SUM).getReportDetail();
		final ReportDealerDataList detail = calc.withPrevious(Option.<ReportDealerDataList>some(detailPrevious)).calcExpenses(dealerExpenseFacts, JournalOp.SUM).getReportDetail();
		System.out.println(detail);
		Assert.assertNotNull(detail);
		Assert.assertEquals(fact1.getAmount().add(fact2.getAmount()).doubleValue(), detail.getDetail().get(0).getExpense().getAmount());
		Assert.assertEquals(0.0, detail.getDetail().get(0).getExpense().getReference());
		Assert.assertEquals((fact3.getAmount().doubleValue() - fact4.getAmount().doubleValue()) / Math.abs(fact4.getAmount().doubleValue()), detail.getDetail().get(1).getExpense().getPercentage());
	}
	
	public void setRevenueForYearReport(final DealerReportCalculator calc) {
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

		final ReportDataDetail detail = calc.calcRevenues(dealerRevenueFacts, JournalOp.SUM).calcMargins(dealerRevenueFacts, JournalOp.SUM).getReportDetail();
		System.out.println(detail);
		Assert.assertNotNull(detail);
	}
	@Test
	public void calcExpenseForYearReportWithDenominator() {
		final DealerReportCalculator calc = new DealerReportCalculator(dealers, 2013);
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
		final DealerIncomeExpenseFact fact1 = new DealerIncomeExpenseFact();
		fact1.setTimeID(time201308.getId());
		fact1.setDealerID(1);
		fact1.setDepartmentID(1);
		fact1.setItemID(1L);
		fact1.setAmount(new BigDecimal("1000.0"));
		// skip timestamp and time end
		final DealerIncomeExpenseFact fact2 = new DealerIncomeExpenseFact();
		fact2.setTimeID(time201308.getId());
		fact2.setDealerID(1);
		fact2.setDepartmentID(2);
		fact2.setItemID(1L);
		fact2.setAmount(new BigDecimal("2000.0"));
		final DealerIncomeExpenseFact fact3 = new DealerIncomeExpenseFact();
		fact3.setTimeID(time201307.getId());
		fact3.setDealerID(2);
		fact3.setDepartmentID(2);
		fact3.setItemID(2L);
		fact3.setAmount(new BigDecimal("2020.0"));
		
		final ImmutableListMultimap<Integer, DealerIncomeExpenseFact> dealerExpenseFacts = Multimaps
				.index(Lists.newArrayList(fact1, fact2, fact3),
						GetDealerIDFromExpenseFunction.INSTANCE);

		setRevenueForYearReport(calc);
		calc.calcExpenses(dealerExpenseFacts, JournalOp.SUM);
		calc.withDenominator(Option.<Integer>some(1)).prepareDenominators();
		calc.adjustExpenseByDenominator();
		
		final ReportDealerDataList detail = calc.getReportDetail();
		System.out.println(detail);
		Assert.assertNotNull(detail);
		Assert.assertEquals(0.6666666666666666, detail.getDetail().get(1).getExpense().getAmount());
		Assert.assertEquals(0.06666666666666667, detail.getDetail().get(1).getExpense().getReference());
	}
	
	@Test
	public void calcOpProfitForYearReport() {
		final DealerReportCalculator calc = new DealerReportCalculator(dealers, 2013);
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
		final DealerIncomeExpenseFact fact1 = new DealerIncomeExpenseFact();
		fact1.setTimeID(time201308.getId());
		fact1.setDealerID(1);
		fact1.setDepartmentID(1);
		fact1.setItemID(1L);
		fact1.setAmount(new BigDecimal("1000.0"));
		// skip timestamp and time end
		final DealerIncomeExpenseFact fact2 = new DealerIncomeExpenseFact();
		fact2.setTimeID(time201308.getId());
		fact2.setDealerID(1);
		fact2.setDepartmentID(2);
		fact2.setItemID(1L);
		fact2.setAmount(new BigDecimal("2000.0"));
		final DealerIncomeExpenseFact fact3 = new DealerIncomeExpenseFact();
		fact3.setTimeID(time201307.getId());
		fact3.setDealerID(2);
		fact3.setDepartmentID(2);
		fact3.setItemID(2L);
		fact3.setAmount(new BigDecimal("2020.0"));
		
		final DealerIncomeRevenueFact fact11 = new DealerIncomeRevenueFact();
		fact11.setTimeID(time201308.getId());
		fact11.setDealerID(1);
		fact11.setDepartmentID(1);
		fact11.setItemID(1L);
		fact11.setAmount(new BigDecimal("1000.0"));
		fact11.setMargin(new BigDecimal("2000.0"));
		fact11.setCount(20);
		// skip timestamp and time end
		final DealerIncomeRevenueFact fact21 = new DealerIncomeRevenueFact();
		fact21.setTimeID(time201308.getId());
		fact21.setDealerID(1);
		fact21.setDepartmentID(2);
		fact21.setItemID(1L);
		fact21.setAmount(new BigDecimal("2000.0"));
		fact21.setMargin(new BigDecimal("3000.0"));
		fact21.setCount(30);
		final DealerIncomeRevenueFact fact31 = new DealerIncomeRevenueFact();
		fact31.setTimeID(time201307.getId());
		fact31.setDealerID(2);
		fact31.setDepartmentID(2);
		fact31.setItemID(2L);
		fact31.setAmount(new BigDecimal("2020.0"));
		fact31.setMargin(new BigDecimal("3030.0"));
		fact31.setCount(40);
		
		final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFacts = Multimaps
				.index(Lists.newArrayList(fact11, fact21, fact31),
						GetDealerIDFromRevenueFunction.INSTANCE);
		final ImmutableListMultimap<Integer, DealerIncomeExpenseFact> dealerExpenseFacts = Multimaps
				.index(Lists.newArrayList(fact1, fact2, fact3),
						GetDealerIDFromExpenseFunction.INSTANCE);

		final ReportDealerDataList detail = 
				calc.calcRevenues(dealerRevenueFacts, JournalOp.SUM)
					.calcExpenses(dealerExpenseFacts, JournalOp.SUM)
					.calcMargins(dealerRevenueFacts, JournalOp.SUM)
					.calcOpProfit().getReportDetail();
		System.out.println(detail);
		Assert.assertNotNull(detail);
		Assert.assertEquals(fact1.getAmount().add(fact2.getAmount()).doubleValue(), detail.getDetail().get(0).getExpense().getAmount());
		Assert.assertEquals((fact3.getAmount().doubleValue()) / 9.0, detail.getDetail().get(0).getExpense().getReference());
		
		Assert.assertEquals(fact11.getMargin().add(fact21.getMargin()).doubleValue() - fact1.getAmount().add(fact2.getAmount()).doubleValue(), 
				detail.getDetail().get(0).getOpProfit().getAmount());
	}
	
	@Test
	public void calcNetProfitForYearReport() {
		final DealerReportCalculator calc = new DealerReportCalculator(dealers, 2013);
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
		final DealerIncomeExpenseFact fact1 = new DealerIncomeExpenseFact();
		fact1.setTimeID(time201308.getId());
		fact1.setDealerID(1);
		fact1.setDepartmentID(1);
		fact1.setItemID(1L);
		fact1.setAmount(new BigDecimal("1000.0"));
		// skip timestamp and time end
		final DealerIncomeExpenseFact fact2 = new DealerIncomeExpenseFact();
		fact2.setTimeID(time201308.getId());
		fact2.setDealerID(1);
		fact2.setDepartmentID(2);
		fact2.setItemID(1L);
		fact2.setAmount(new BigDecimal("2000.0"));
		final DealerIncomeExpenseFact fact3 = new DealerIncomeExpenseFact();
		fact3.setTimeID(time201307.getId());
		fact3.setDealerID(2);
		fact3.setDepartmentID(2);
		fact3.setItemID(2L);
		fact3.setAmount(new BigDecimal("2020.0"));
		
		final DealerIncomeRevenueFact fact11 = new DealerIncomeRevenueFact();
		fact11.setTimeID(time201308.getId());
		fact11.setDealerID(1);
		fact11.setDepartmentID(1);
		fact11.setItemID(1L);
		fact11.setAmount(new BigDecimal("1000.0"));
		fact11.setMargin(new BigDecimal("2000.0"));
		fact11.setCount(20);
		// skip timestamp and time end
		final DealerIncomeRevenueFact fact21 = new DealerIncomeRevenueFact();
		fact21.setTimeID(time201308.getId());
		fact21.setDealerID(1);
		fact21.setDepartmentID(2);
		fact21.setItemID(1L);
		fact21.setAmount(new BigDecimal("2000.0"));
		fact21.setMargin(new BigDecimal("3000.0"));
		fact21.setCount(30);
		final DealerIncomeRevenueFact fact31 = new DealerIncomeRevenueFact();
		fact31.setTimeID(time201307.getId());
		fact31.setDealerID(2);
		fact31.setDepartmentID(2);
		fact31.setItemID(2L);
		fact31.setAmount(new BigDecimal("2020.0"));
		fact31.setMargin(new BigDecimal("3030.0"));
		fact31.setCount(40);
		
		final DealerIncomeRevenueFact fact12 = new DealerIncomeRevenueFact();
		fact12.setTimeID(time201308.getId());
		fact12.setDealerID(1);
		fact12.setDepartmentID(1);
		fact12.setItemID(1L);
		fact12.setAmount(new BigDecimal("1000.0"));
		fact12.setMargin(new BigDecimal("2000.0"));
		fact12.setCount(20);
		// skip timestamp and time end
		final DealerIncomeRevenueFact fact22 = new DealerIncomeRevenueFact();
		fact22.setTimeID(time201308.getId());
		fact22.setDealerID(1);
		fact22.setDepartmentID(2);
		fact22.setItemID(1L);
		fact22.setAmount(new BigDecimal("2000.0"));
		fact22.setMargin(new BigDecimal("3000.0"));
		fact22.setCount(30);
		
		final DealerIncomeExpenseFact fact13 = new DealerIncomeExpenseFact();
		fact13.setTimeID(time201308.getId());
		fact13.setDealerID(1);
		fact13.setDepartmentID(1);
		fact13.setItemID(1L);
		fact13.setAmount(new BigDecimal("1000.0"));
		// skip timestamp and time end
		final DealerIncomeExpenseFact fact23 = new DealerIncomeExpenseFact();
		fact23.setTimeID(time201308.getId());
		fact23.setDealerID(1);
		fact23.setDepartmentID(2);
		fact23.setItemID(1L);
		fact23.setAmount(new BigDecimal("4000.0"));
		
		final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFacts = Multimaps
				.index(Lists.newArrayList(fact11, fact21, fact31),
						GetDealerIDFromRevenueFunction.INSTANCE);
		final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> otherDealerRevenueFacts = Multimaps
				.index(Lists.newArrayList(fact12, fact22),
						GetDealerIDFromRevenueFunction.INSTANCE);
		final ImmutableListMultimap<Integer, DealerIncomeExpenseFact> dealerExpenseFacts = Multimaps
				.index(Lists.newArrayList(fact1, fact2, fact3),
						GetDealerIDFromExpenseFunction.INSTANCE);
		final ImmutableListMultimap<Integer, DealerIncomeExpenseFact> otherDealerExpenseFacts = Multimaps
				.index(Lists.newArrayList(fact13, fact23),
						GetDealerIDFromExpenseFunction.INSTANCE);

		final ReportDealerDataList detail = 
				calc.calcRevenues(dealerRevenueFacts, JournalOp.SUM)
					.calcExpenses(dealerExpenseFacts, JournalOp.SUM)
					.calcMargins(dealerRevenueFacts, JournalOp.SUM)
					.calcNetProfit(otherDealerRevenueFacts, otherDealerExpenseFacts, JournalOp.SUM).getReportDetail();
		System.out.println(detail);

		Assert.assertEquals(fact11.getMargin().add(fact21.getMargin()).doubleValue() - fact1.getAmount().add(fact2.getAmount()).doubleValue() - 2000.0, 
				detail.getDetail().get(0).getNetProfit().getAmount());
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
}
