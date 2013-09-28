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
import com.jdc.themis.dealer.web.domain.DepartmentDetail;
import com.jdc.themis.dealer.web.domain.ReportDataDetail;

import fj.data.Option;

public class TestDepartmentReportCalculator {

	private Collection<DepartmentDetail> departments;

	@Before
	public void setUp() {
		final DepartmentDetail department1 = new DepartmentDetail();
		department1.setId(1);
		department1.setName("Department1");
		final DepartmentDetail department2 = new DepartmentDetail();
		department2.setId(2);
		department2.setName("Department2");
		final DepartmentDetail department3 = new DepartmentDetail();
		department3.setId(3);
		department3.setName("Department3");
		final DepartmentDetail department4 = new DepartmentDetail();
		department4.setId(4);
		department4.setName("Department4");
		final DepartmentDetail department5 = new DepartmentDetail();
		department5.setId(5);
		department5.setName("Department5");
		final DepartmentDetail department6 = new DepartmentDetail();
		department6.setId(6);
		department6.setName("Department6");
		final DepartmentDetail department7 = new DepartmentDetail();
		department7.setId(7);
		department7.setName("Department7");
		departments = Lists.newArrayList(department1, department2, department3, department4, department5, department6, department7);
	}

	@Test
	public void calcMarginForYearReport() {
		final DepartmentReportCalculator calc = new DepartmentReportCalculator(departments, 2013);
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
						GetDepartmentIDFromRevenueFunction.INSTANCE);

		final ReportDataDetail detail = calc.calcMargins(dealerRevenueFacts, JournalOp.SUM).getReportDetail();
		System.out.println(detail);
		Assert.assertNotNull(detail);
		Assert.assertEquals(fact1.getMargin().doubleValue(), detail.getDepartmentDetail().get(0).getMargin().getAmount());
	}
	
	@Test
	public void calcMarginForYearReportWithPercentage() {
		final DepartmentReportCalculator calc = new DepartmentReportCalculator(departments, 2013);
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
						GetDepartmentIDFromRevenueFunction.INSTANCE);
		
		final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFacts = Multimaps
				.index(Lists.newArrayList(fact1, fact2, fact3),
						GetDepartmentIDFromRevenueFunction.INSTANCE);

		final ReportDataDetail detailPrevious = calc.calcMargins(dealerRevenueFactsPrevious, JournalOp.SUM).getReportDetail();
		final ReportDataDetail detail = calc.withPrevious(Option.<ReportDataDetail>some(detailPrevious)).calcMargins(dealerRevenueFacts, JournalOp.SUM).getReportDetail();
		System.out.println(detailPrevious);
		System.out.println(detail);
		Assert.assertNotNull(detail);
		Assert.assertEquals(fact1.getMargin().doubleValue(), detail.getDepartmentDetail().get(0).getMargin().getAmount());
		Assert.assertEquals((fact2.getMargin().doubleValue() + fact3.getMargin().doubleValue() - fact4.getMargin().doubleValue()) / fact4.getMargin().doubleValue(), detail.getDepartmentDetail().get(1).getMargin().getPercentage());
	}
	
	@Test
	public void calcRevenueForYearReport() {
		final DepartmentReportCalculator calc = new DepartmentReportCalculator(departments, 2013);
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
						GetDepartmentIDFromRevenueFunction.INSTANCE);

		final ReportDataDetail detail = calc.calcRevenues(dealerRevenueFacts, JournalOp.SUM).getReportDetail();
		System.out.println(detail);
		Assert.assertNotNull(detail);
		Assert.assertEquals(fact1.getAmount().doubleValue(), detail.getDepartmentDetail().get(0).getRevenue().getAmount());
	}
	
	@Test
	public void calcRevenueForYearReportWithPercentage() {
		final DepartmentReportCalculator calc = new DepartmentReportCalculator(departments, 2013);
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
						GetDepartmentIDFromRevenueFunction.INSTANCE);
		
		final ImmutableListMultimap<Integer, DealerIncomeRevenueFact> dealerRevenueFacts = Multimaps
				.index(Lists.newArrayList(fact1, fact2, fact3),
						GetDepartmentIDFromRevenueFunction.INSTANCE);

		final ReportDataDetail detailPrevious = calc.calcRevenues(dealerRevenueFactsPrevious, JournalOp.SUM).getReportDetail();
		final ReportDataDetail detail = calc.withPrevious(Option.<ReportDataDetail>some(detailPrevious)).calcRevenues(dealerRevenueFacts, JournalOp.SUM).getReportDetail();
		System.out.println(detail);
		Assert.assertNotNull(detail);
		Assert.assertEquals(fact1.getAmount().doubleValue(), detail.getDepartmentDetail().get(0).getRevenue().getAmount());
		Assert.assertEquals((fact2.getAmount().doubleValue() + fact3.getAmount().doubleValue() - fact4.getAmount().doubleValue()) / fact4.getAmount().doubleValue(), detail.getDepartmentDetail().get(1).getRevenue().getPercentage());
	}
	
	@Test
	public void calcExpenseForYearReport() {
		final DepartmentReportCalculator calc = new DepartmentReportCalculator(departments, 2013);
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
						GetDepartmentIDFromExpenseFunction.INSTANCE);

		final ReportDataDetail detail = calc.calcExpenses(dealerExpenseFacts, JournalOp.SUM).getReportDetail();
		System.out.println(detail);
		Assert.assertNotNull(detail);
		Assert.assertEquals(fact1.getAmount().doubleValue(), detail.getDepartmentDetail().get(0).getExpense().getAmount());
	}
	
	@Test
	public void calcExpenseForYearReportWithPercentage() {
		final DepartmentReportCalculator calc = new DepartmentReportCalculator(departments, 2013);
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
						GetDepartmentIDFromExpenseFunction.INSTANCE);
		
		final ImmutableListMultimap<Integer, DealerIncomeExpenseFact> dealerExpenseFacts = Multimaps
				.index(Lists.newArrayList(fact1, fact2, fact3),
						GetDepartmentIDFromExpenseFunction.INSTANCE);

		final ReportDataDetail detailPrevious = calc.calcExpenses(dealerExpenseFactsPrevious, JournalOp.SUM).getReportDetail();
		final ReportDataDetail detail = calc.withPrevious(Option.<ReportDataDetail>some(detailPrevious)).calcExpenses(dealerExpenseFacts, JournalOp.SUM).getReportDetail();
		System.out.println(detail);
		Assert.assertNotNull(detail);
		Assert.assertEquals(fact1.getAmount().doubleValue(), detail.getDepartmentDetail().get(0).getExpense().getAmount());
		Assert.assertEquals((fact2.getAmount().doubleValue() + fact3.getAmount().doubleValue() - fact4.getAmount().doubleValue()) / fact4.getAmount().doubleValue(), detail.getDepartmentDetail().get(1).getExpense().getPercentage());
	}
	
	@Test
	public void calcExpenseForYearReportWithPercentage2() {
		final DepartmentReportCalculator calc = new DepartmentReportCalculator(departments, 2013);
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
						GetDepartmentIDFromExpenseFunction.INSTANCE);
		
		final ImmutableListMultimap<Integer, DealerIncomeExpenseFact> dealerExpenseFacts = Multimaps
				.index(Lists.newArrayList(fact1, fact2, fact3),
						GetDepartmentIDFromExpenseFunction.INSTANCE);

		final ReportDataDetail detailPrevious = calc.calcExpenses(dealerExpenseFactsPrevious, JournalOp.SUM).getReportDetail();
		final ReportDataDetail detail = calc.withPrevious(Option.<ReportDataDetail>some(detailPrevious)).calcExpenses(dealerExpenseFacts, JournalOp.SUM).getReportDetail();
		System.out.println(detail);
		Assert.assertNotNull(detail);
		Assert.assertEquals(fact1.getAmount().doubleValue(), detail.getDepartmentDetail().get(0).getExpense().getAmount());
		Assert.assertEquals((fact2.getAmount().doubleValue() + fact3.getAmount().doubleValue() - fact4.getAmount().doubleValue()) / Math.abs(fact4.getAmount().doubleValue()), detail.getDepartmentDetail().get(1).getExpense().getPercentage());
	}
	
	@Test
	public void calcOpProfitForYearReport() {
		final DepartmentReportCalculator calc = new DepartmentReportCalculator(departments, 2013);
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
		fact21.setDepartmentID(1);
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
						GetDepartmentIDFromRevenueFunction.INSTANCE);
		final ImmutableListMultimap<Integer, DealerIncomeExpenseFact> dealerExpenseFacts = Multimaps
				.index(Lists.newArrayList(fact1, fact2, fact3),
						GetDepartmentIDFromExpenseFunction.INSTANCE);

		final ReportDataDetail detail = 
				calc.calcRevenues(dealerRevenueFacts, JournalOp.SUM)
					.calcExpenses(dealerExpenseFacts, JournalOp.SUM)
					.calcMargins(dealerRevenueFacts, JournalOp.SUM)
					.calcOpProfit().getReportDetail();
		System.out.println(detail);
		Assert.assertNotNull(detail);
		Assert.assertEquals(fact1.getAmount().doubleValue(), detail.getDepartmentDetail().get(0).getExpense().getAmount());
		Assert.assertEquals(fact11.getMargin().add(fact21.getMargin()).doubleValue() - fact1.getAmount().doubleValue(), 
				detail.getDepartmentDetail().get(0).getOpProfit().getAmount());
	}
	
	private enum GetDepartmentIDFromRevenueFunction implements
			Function<DealerIncomeRevenueFact, Integer> {
		INSTANCE;

		@Override
		public Integer apply(final DealerIncomeRevenueFact item) {
			return item.getDepartmentID();
		}
	}
	

	private enum GetDepartmentIDFromExpenseFunction implements
			Function<DealerIncomeExpenseFact, Integer> {
		INSTANCE;

		@Override
		public Integer apply(final DealerIncomeExpenseFact item) {
			return item.getDepartmentID();
		}
	}
}
