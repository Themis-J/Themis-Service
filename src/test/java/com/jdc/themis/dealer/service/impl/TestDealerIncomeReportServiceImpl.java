package com.jdc.themis.dealer.service.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.time.calendar.LocalDate;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.jdc.themis.dealer.data.dao.ReportDAO;
import com.jdc.themis.dealer.domain.DealerIncomeRevenueFact;
import com.jdc.themis.dealer.domain.ReportItem;
import com.jdc.themis.dealer.domain.ReportTime;
import com.jdc.themis.dealer.service.RefDataQueryService;
import com.jdc.themis.dealer.web.domain.DealerDetail;
import com.jdc.themis.dealer.web.domain.DepartmentDetail;
import com.jdc.themis.dealer.web.domain.GetDealerResponse;
import com.jdc.themis.dealer.web.domain.GetDepartmentResponse;
import com.jdc.themis.dealer.web.domain.ImportReportDataRequest;
import com.jdc.themis.dealer.web.domain.QueryDealerIncomeResponse;
import com.jdc.themis.dealer.web.domain.QueryDealerSalesResponse;
import com.jdc.themis.dealer.web.domain.QueryDepartmentIncomeResponse;
import com.jdc.themis.dealer.web.domain.SalesServiceJournalItemDetail;

import fj.data.Option;

public class TestDealerIncomeReportServiceImpl {
	private DealerIncomeReportServiceImpl service;
	@Mock
	private ReportDAO dal;
	@Mock
	private RefDataQueryService refDataDAL;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks( this );
		dal = mock(ReportDAO.class);
		refDataDAL = mock(RefDataQueryService.class);
		service = new DealerIncomeReportServiceImpl();
		service.setReportDAL(dal);
		service.setRefDataDAL(refDataDAL);
	}
	
	@Test
	public void importReportData() {
		final ImportReportDataRequest request = new ImportReportDataRequest();
		request.setFromDate(LocalDate.of(2013, 8, 1).toString());
		request.setToDate(LocalDate.of(2013, 8, 1).toString());
		
		service.importReportData(request);
		verify(dal).importGeneralJournal(LocalDate.of(2013, 8, 1));
		verify(dal).importTaxJournal(LocalDate.of(2013, 8, 1));
		verify(dal).importSalesServiceJournal(LocalDate.of(2013, 8, 1));
		verify(dal).importVehicleSalesJournal(LocalDate.of(2013, 8, 1));
	}
	
	@Test(expected=RuntimeException.class)
	public void importReportDataFailed() {
		final ImportReportDataRequest request = new ImportReportDataRequest();
		request.setFromDate(LocalDate.of(2013, 8, 1).toString());
		final Calendar c = new GregorianCalendar();
		c.setTime(new Date());
		final LocalDate today = LocalDate.of(c.get(Calendar.YEAR),
				c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
		
		request.setToDate(today.plusDays(1).toString());
		
		service.importReportData(request);
	}
	private final static String[] revenueCategories = new String[] {"新轿车零售", "新货车零售",
			"附加产品业务", "二手车零售", "维修收入", "配件收入", "钣喷收入", "新车其它收入", "二手车其它收入", "维修其它收入", "钣喷其它收入", "租恁收入"};
	
	@Test
	public void queryYearlyOverallIncomeReport() {
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
		
		final GetDealerResponse dealers = new GetDealerResponse();
		dealers.getItems().addAll(Lists.newArrayList(dealer1, dealer2, dealer3, dealer4, dealer5, dealer6, dealer7, dealer8, dealer9, dealer10, dealer11, dealer12));
		when(refDataDAL.getDealers()).thenReturn(dealers);
		
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
		
		final ReportTime time201208 = new ReportTime();
		time201208.setId(3L);
		time201208.setMonthOfYear(8);
		time201208.setYear(2012);
		time201208.setValidDate(LocalDate.of(2012, 8, 1));
		final ReportTime time201207 = new ReportTime();
		time201207.setId(4L);
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
		//skip timestamp and time end
		final DealerIncomeRevenueFact fact2 = new DealerIncomeRevenueFact();
		fact2.setTimeID(time201308.getId());
		fact2.setDealerID(1);
		fact2.setDepartmentID(2);
		fact2.setItemID(1L);
		fact2.setAmount(new BigDecimal("2000.0"));
		fact2.setMargin(new BigDecimal("3000.0"));
		fact2.setCount(30);
		final DealerIncomeRevenueFact fact3 = new DealerIncomeRevenueFact();
		fact3.setTimeID(time201308.getId());
		fact3.setDealerID(2);
		fact3.setDepartmentID(2);
		fact3.setItemID(2L);
		fact3.setAmount(new BigDecimal("2020.0"));
		fact3.setMargin(new BigDecimal("3030.0"));
		fact3.setCount(40);
		final DealerIncomeRevenueFact fact6 = new DealerIncomeRevenueFact();
		fact6.setTimeID(time201307.getId());
		fact6.setDealerID(1);
		fact6.setDepartmentID(2);
		fact6.setItemID(1L);
		fact6.setAmount(new BigDecimal("2000.0"));
		fact6.setMargin(new BigDecimal("3000.0"));
		fact6.setCount(30);
		
		final DealerIncomeRevenueFact fact4 = new DealerIncomeRevenueFact();
		fact4.setTimeID(time201208.getId());
		fact4.setDealerID(1);
		fact4.setDepartmentID(2);
		fact4.setItemID(1L);
		fact4.setAmount(new BigDecimal("500.0"));
		fact4.setMargin(new BigDecimal("300.0"));
		fact4.setCount(30);
		final DealerIncomeRevenueFact fact5 = new DealerIncomeRevenueFact();
		fact5.setTimeID(time201208.getId());
		fact5.setDealerID(2);
		fact5.setDepartmentID(2);
		fact5.setItemID(2L);
		fact5.setAmount(new BigDecimal("220.0"));
		fact5.setMargin(new BigDecimal("330.0"));
		fact5.setCount(40);
		//skip timestamp and time end
		when(dal.getDealerIncomeRevenueFacts(2013, Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(revenueCategories), 
				Lists.newArrayList(new Long[]{}), 
				Lists.newArrayList(new Integer[]{}))).thenReturn(Lists.newArrayList(fact1, fact2, fact3, fact6));
		when(dal.getDealerIncomeRevenueFacts(2013, Lists.newArrayList(new Integer[]{8}), 
				Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(revenueCategories), 
				Lists.newArrayList(new Long[]{}), 
				Lists.newArrayList(new Integer[]{}))).thenReturn(Lists.newArrayList(fact1, fact2));
		when(dal.getDealerIncomeRevenueFacts(2012, Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(revenueCategories), 
				Lists.newArrayList(new Long[]{}), 
				Lists.newArrayList(new Integer[]{}))).thenReturn(Lists.newArrayList(fact4, fact5));
		final ReportItem item1 = new ReportItem();
		item1.setId(1L);
		item1.setItemCategory("C1");
		final ReportItem item2 = new ReportItem();
		item2.setId(2L);
		item2.setItemCategory("C1");
		when(dal.getReportItem(1L)).thenReturn(Option.<ReportItem>some(item1));
		when(dal.getReportItem(2L)).thenReturn(Option.<ReportItem>some(item2));
		final QueryDealerIncomeResponse response = service.queryOverallIncomeReport(2013, Option.<Integer>none(), Option.<Integer>none(), Option.<Integer>none());
		Assert.assertNotNull(response);
		Assert.assertEquals(2, response.getDetail().size());
		Assert.assertEquals(2012, response.getDetail().get(0).getYear().intValue());
		Assert.assertEquals(2013, response.getDetail().get(1).getYear().intValue());
		System.err.println("Year 2013 Detail: " + response.getDetail().get(1).getDetail());
		Assert.assertEquals(5000.0, response.getDetail().get(1).getDetail().get(0).getRevenue().getAmount());
		Assert.assertEquals(8000.0, response.getDetail().get(1).getDetail().get(0).getMargin().getAmount());
		Assert.assertEquals(8000.0, response.getDetail().get(1).getDetail().get(0).getNetProfit().getAmount());
		System.err.println(response);
		
		final QueryDealerIncomeResponse response2 = service.queryOverallIncomeReport(2013, Option.<Integer>some(8), Option.<Integer>none(), Option.<Integer>none());
		Assert.assertNotNull(response2);
		Assert.assertEquals(2, response2.getDetail().size());
		Assert.assertEquals(2013, response2.getDetail().get(0).getYear().intValue());
		Assert.assertEquals(8, response2.getDetail().get(0).getMonth().intValue());
		Assert.assertEquals(2013, response2.getDetail().get(1).getYear().intValue());
		Assert.assertEquals(8, response2.getDetail().get(1).getMonth().intValue());
		Assert.assertEquals(3000.0 / 8, response2.getDetail().get(0).getDetail().get(0).getRevenue().getAmount());
		Assert.assertEquals(3000.0, response2.getDetail().get(1).getDetail().get(0).getRevenue().getAmount());
		System.out.println(response2);
	}
	
	@Test
	public void queryYearlyDepartmentIncomeReport() {
		final DepartmentDetail department1 = new DepartmentDetail();
		department1.setId(1);
		department1.setName("Department1");
		final DepartmentDetail department2 = new DepartmentDetail();
		department2.setId(2);
		department2.setName("Department2");
		final DepartmentDetail department3 = new DepartmentDetail();
		department3.setId(3);
		department3.setName("Department3");
		
		
		final GetDepartmentResponse departments = new GetDepartmentResponse();
		departments.getItems().addAll(Lists.newArrayList(department1, department2, department3));
		when(refDataDAL.getDepartments()).thenReturn(departments);
		
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
		
		final ReportTime time201208 = new ReportTime();
		time201208.setId(3L);
		time201208.setMonthOfYear(8);
		time201208.setYear(2012);
		time201208.setValidDate(LocalDate.of(2012, 8, 1));
		final ReportTime time201207 = new ReportTime();
		time201207.setId(4L);
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
		//skip timestamp and time end
		final DealerIncomeRevenueFact fact2 = new DealerIncomeRevenueFact();
		fact2.setTimeID(time201308.getId());
		fact2.setDealerID(1);
		fact2.setDepartmentID(2);
		fact2.setItemID(1L);
		fact2.setAmount(new BigDecimal("2000.0"));
		fact2.setMargin(new BigDecimal("3000.0"));
		fact2.setCount(30);
		final DealerIncomeRevenueFact fact3 = new DealerIncomeRevenueFact();
		fact3.setTimeID(time201308.getId());
		fact3.setDealerID(2);
		fact3.setDepartmentID(2);
		fact3.setItemID(2L);
		fact3.setAmount(new BigDecimal("2020.0"));
		fact3.setMargin(new BigDecimal("3030.0"));
		fact3.setCount(40);
		final DealerIncomeRevenueFact fact6 = new DealerIncomeRevenueFact();
		fact6.setTimeID(time201307.getId());
		fact6.setDealerID(1);
		fact6.setDepartmentID(2);
		fact6.setItemID(1L);
		fact6.setAmount(new BigDecimal("2000.0"));
		fact6.setMargin(new BigDecimal("3000.0"));
		fact6.setCount(30);
		
		final DealerIncomeRevenueFact fact4 = new DealerIncomeRevenueFact();
		fact4.setTimeID(time201208.getId());
		fact4.setDealerID(1);
		fact4.setDepartmentID(2);
		fact4.setItemID(1L);
		fact4.setAmount(new BigDecimal("500.0"));
		fact4.setMargin(new BigDecimal("300.0"));
		fact4.setCount(30);
		final DealerIncomeRevenueFact fact5 = new DealerIncomeRevenueFact();
		fact5.setTimeID(time201208.getId());
		fact5.setDealerID(2);
		fact5.setDepartmentID(2);
		fact5.setItemID(2L);
		fact5.setAmount(new BigDecimal("220.0"));
		fact5.setMargin(new BigDecimal("330.0"));
		fact5.setCount(40);
		//skip timestamp and time end
		when(dal.getDealerIncomeRevenueFacts(2013, Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(revenueCategories), 
				Lists.newArrayList(new Long[]{}), 
				Lists.newArrayList(new Integer[]{}))).thenReturn(Lists.newArrayList(fact1, fact2, fact3, fact6));
		when(dal.getDealerIncomeRevenueFacts(2013, Lists.newArrayList(new Integer[]{8}), 
				Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(revenueCategories), 
				Lists.newArrayList(new Long[]{}), 
				Lists.newArrayList(new Integer[]{}))).thenReturn(Lists.newArrayList(fact1, fact2));
		when(dal.getDealerIncomeRevenueFacts(2012, Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(revenueCategories), 
				Lists.newArrayList(new Long[]{}), 
				Lists.newArrayList(new Integer[]{}))).thenReturn(Lists.newArrayList(fact4, fact5));
		final ReportItem item1 = new ReportItem();
		item1.setId(1L);
		item1.setItemCategory("C1");
		final ReportItem item2 = new ReportItem();
		item2.setId(2L);
		item2.setItemCategory("C1");
		when(dal.getReportItem(1L)).thenReturn(Option.<ReportItem>some(item1));
		when(dal.getReportItem(2L)).thenReturn(Option.<ReportItem>some(item2));
		final QueryDepartmentIncomeResponse response = service.queryDepartmentIncomeReport(2013, Option.<Integer>none(), Option.<Integer>none(), Option.<Integer>none());
		Assert.assertNotNull(response);
		Assert.assertEquals(2, response.getDetail().size());
		Assert.assertEquals(2012, response.getDetail().get(0).getYear().intValue());
		Assert.assertEquals(2013, response.getDetail().get(1).getYear().intValue());
		Assert.assertEquals(1000.0, response.getDetail().get(1).getDetail().get(0).getRevenue().getAmount());
		Assert.assertEquals(2000.0, response.getDetail().get(1).getDetail().get(0).getMargin().getAmount());
		System.err.println(response);
		
		final QueryDepartmentIncomeResponse response2 = service.queryDepartmentIncomeReport(2013, Option.<Integer>some(8), Option.<Integer>none(), Option.<Integer>none());
		Assert.assertNotNull(response2);
		Assert.assertEquals(2, response2.getDetail().size());
		Assert.assertEquals(2013, response2.getDetail().get(0).getYear().intValue());
		Assert.assertEquals(8, response2.getDetail().get(0).getMonth().intValue());
		Assert.assertEquals(2013, response2.getDetail().get(1).getYear().intValue());
		Assert.assertEquals(8, response2.getDetail().get(1).getMonth().intValue());
		Assert.assertEquals(1000.0 / 8, response2.getDetail().get(0).getDetail().get(0).getRevenue().getAmount());
		Assert.assertEquals(1000.0, response2.getDetail().get(1).getDetail().get(0).getRevenue().getAmount());
		System.out.println(response2);
	}
	
	@Test
	public void queryYearlyOverallIncomeReportWithDenominator() {
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
		
		final GetDealerResponse dealers = new GetDealerResponse();
		dealers.getItems().addAll(Lists.newArrayList(dealer1, dealer2, dealer3, dealer4, dealer5, dealer6, dealer7, dealer8, dealer9, dealer10, dealer11, dealer12));
		when(refDataDAL.getDealers()).thenReturn(dealers);
		
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
		
		final ReportTime time201208 = new ReportTime();
		time201208.setId(3L);
		time201208.setMonthOfYear(8);
		time201208.setYear(2012);
		time201208.setValidDate(LocalDate.of(2012, 8, 1));
		final ReportTime time201207 = new ReportTime();
		time201207.setId(4L);
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
		//skip timestamp and time end
		final DealerIncomeRevenueFact fact2 = new DealerIncomeRevenueFact();
		fact2.setTimeID(time201308.getId());
		fact2.setDealerID(1);
		fact2.setDepartmentID(2);
		fact2.setItemID(1L);
		fact2.setAmount(new BigDecimal("2000.0"));
		fact2.setMargin(new BigDecimal("3000.0"));
		fact2.setCount(30);
		final DealerIncomeRevenueFact fact3 = new DealerIncomeRevenueFact();
		fact3.setTimeID(time201308.getId());
		fact3.setDealerID(2);
		fact3.setDepartmentID(2);
		fact3.setItemID(2L);
		fact3.setAmount(new BigDecimal("2020.0"));
		fact3.setMargin(new BigDecimal("3030.0"));
		fact3.setCount(40);
		final DealerIncomeRevenueFact fact6 = new DealerIncomeRevenueFact();
		fact6.setTimeID(time201307.getId());
		fact6.setDealerID(1);
		fact6.setDepartmentID(2);
		fact6.setItemID(1L);
		fact6.setAmount(new BigDecimal("2000.0"));
		fact6.setMargin(new BigDecimal("3000.0"));
		fact6.setCount(30);
		
		final DealerIncomeRevenueFact fact4 = new DealerIncomeRevenueFact();
		fact4.setTimeID(time201208.getId());
		fact4.setDealerID(1);
		fact4.setDepartmentID(2);
		fact4.setItemID(1L);
		fact4.setAmount(new BigDecimal("500.0"));
		fact4.setMargin(new BigDecimal("300.0"));
		fact4.setCount(30);
		final DealerIncomeRevenueFact fact5 = new DealerIncomeRevenueFact();
		fact5.setTimeID(time201208.getId());
		fact5.setDealerID(2);
		fact5.setDepartmentID(2);
		fact5.setItemID(2L);
		fact5.setAmount(new BigDecimal("220.0"));
		fact5.setMargin(new BigDecimal("330.0"));
		fact5.setCount(40);
		
		//skip timestamp and time end
		when(dal.getDealerIncomeRevenueFacts(2013, Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(revenueCategories), 
				Lists.newArrayList(new Long[]{}), 
				Lists.newArrayList(new Integer[]{}))).thenReturn(Lists.newArrayList(fact1, fact2, fact3, fact6));
		when(dal.getDealerIncomeRevenueFacts(2013, Lists.newArrayList(new Integer[]{8}), 
				Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(revenueCategories), 
				Lists.newArrayList(new Long[]{}), 
				Lists.newArrayList(new Integer[]{}))).thenReturn(Lists.newArrayList(fact1, fact2));
		when(dal.getDealerIncomeRevenueFacts(2012, Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(revenueCategories), 
				Lists.newArrayList(new Long[]{}), 
				Lists.newArrayList(new Integer[]{}))).thenReturn(Lists.newArrayList(fact4, fact5));
		final ReportItem item1 = new ReportItem();
		item1.setId(1L);
		item1.setItemCategory("C1");
		final ReportItem item2 = new ReportItem();
		item2.setId(2L);
		item2.setItemCategory("C1");
		when(dal.getReportItem(1L)).thenReturn(Option.<ReportItem>some(item1));
		when(dal.getReportItem(2L)).thenReturn(Option.<ReportItem>some(item2));
		final QueryDealerIncomeResponse response = service.queryOverallIncomeReport(2013, Option.<Integer>none(), Option.<Integer>none(), Option.<Integer>some(0));
		Assert.assertNotNull(response);
		Assert.assertEquals(2, response.getDetail().size());
		Assert.assertEquals(2012, response.getDetail().get(0).getYear().intValue());
		Assert.assertEquals(2013, response.getDetail().get(1).getYear().intValue());
		Assert.assertEquals(5000.0, response.getDetail().get(1).getDetail().get(0).getRevenue().getAmount());
		Assert.assertEquals(1.6, response.getDetail().get(1).getDetail().get(0).getMargin().getAmount());
		System.err.println(response);
		
		final QueryDealerIncomeResponse response21 = service.queryOverallIncomeReport(2013, Option.<Integer>some(8), Option.<Integer>none(), Option.<Integer>none());
		Assert.assertNotNull(response21);
		Assert.assertEquals(2, response21.getDetail().size());
		Assert.assertEquals(2013, response21.getDetail().get(0).getYear().intValue());
		Assert.assertEquals(8, response21.getDetail().get(0).getMonth().intValue());
		Assert.assertEquals(2013, response21.getDetail().get(1).getYear().intValue());
		Assert.assertEquals(8, response21.getDetail().get(1).getMonth().intValue());
		Assert.assertEquals(3000.0, response21.getDetail().get(1).getDetail().get(0).getRevenue().getAmount());
		Assert.assertEquals(5000.0, response21.getDetail().get(1).getDetail().get(0).getMargin().getAmount());
		Assert.assertEquals(5000.0, response21.getDetail().get(1).getDetail().get(0).getOpProfit().getAmount());
		Assert.assertEquals(5000.0, response21.getDetail().get(1).getDetail().get(0).getNetProfit().getAmount());
		System.out.println(response21);
		
		final QueryDealerIncomeResponse response2 = service.queryOverallIncomeReport(2013, Option.<Integer>some(8), Option.<Integer>none(), Option.<Integer>some(1));
		Assert.assertNotNull(response2);
		Assert.assertEquals(2, response2.getDetail().size());
		Assert.assertEquals(2013, response2.getDetail().get(0).getYear().intValue());
		Assert.assertEquals(8, response2.getDetail().get(0).getMonth().intValue());
		Assert.assertEquals(2013, response2.getDetail().get(1).getYear().intValue());
		Assert.assertEquals(8, response2.getDetail().get(1).getMonth().intValue());
		Assert.assertEquals(3000.0, response2.getDetail().get(1).getDetail().get(0).getRevenue().getAmount());
		Assert.assertEquals(1.0, response2.getDetail().get(1).getDetail().get(0).getMargin().getAmount());
		Assert.assertEquals(1.0, response2.getDetail().get(1).getDetail().get(0).getOpProfit().getAmount());
		Assert.assertEquals(1.0, response2.getDetail().get(1).getDetail().get(0).getNetProfit().getAmount());
		System.out.println(response2);
	}
	
	private final static String[] salesRevenueCategories = new String[] {"新轿车零售", "新货车零售",
		"附加产品业务", "二手车零售", "新车其它收入", "二手车其它收入"};

	@Test
	public void queryYearlySalesReport() {
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
		
		final GetDealerResponse dealers = new GetDealerResponse();
		dealers.getItems().addAll(Lists.newArrayList(dealer1, dealer2, dealer3, dealer4, dealer5, dealer6, dealer7, dealer8, dealer9, dealer10, dealer11, dealer12));
		when(refDataDAL.getDealers()).thenReturn(dealers);
		
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
		
		final ReportTime time201208 = new ReportTime();
		time201208.setId(3L);
		time201208.setMonthOfYear(8);
		time201208.setYear(2012);
		time201208.setValidDate(LocalDate.of(2012, 8, 1));
		final ReportTime time201207 = new ReportTime();
		time201207.setId(4L);
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
		//skip timestamp and time end
		final DealerIncomeRevenueFact fact2 = new DealerIncomeRevenueFact();
		fact2.setTimeID(time201308.getId());
		fact2.setDealerID(1);
		fact2.setDepartmentID(2);
		fact2.setItemID(1L);
		fact2.setAmount(new BigDecimal("2000.0"));
		fact2.setMargin(new BigDecimal("3000.0"));
		fact2.setCount(30);
		final DealerIncomeRevenueFact fact3 = new DealerIncomeRevenueFact();
		fact3.setTimeID(time201308.getId());
		fact3.setDealerID(2);
		fact3.setDepartmentID(2);
		fact3.setItemID(2L);
		fact3.setAmount(new BigDecimal("2020.0"));
		fact3.setMargin(new BigDecimal("3030.0"));
		fact3.setCount(40);
		final DealerIncomeRevenueFact fact6 = new DealerIncomeRevenueFact();
		fact6.setTimeID(time201307.getId());
		fact6.setDealerID(1);
		fact6.setDepartmentID(2);
		fact6.setItemID(1L);
		fact6.setAmount(new BigDecimal("2000.0"));
		fact6.setMargin(new BigDecimal("3000.0"));
		fact6.setCount(30);
		
		final DealerIncomeRevenueFact fact4 = new DealerIncomeRevenueFact();
		fact4.setTimeID(time201208.getId());
		fact4.setDealerID(1);
		fact4.setDepartmentID(2);
		fact4.setItemID(1L);
		fact4.setAmount(new BigDecimal("500.0"));
		fact4.setMargin(new BigDecimal("300.0"));
		fact4.setCount(30);
		final DealerIncomeRevenueFact fact5 = new DealerIncomeRevenueFact();
		fact5.setTimeID(time201208.getId());
		fact5.setDealerID(2);
		fact5.setDepartmentID(2);
		fact5.setItemID(2L);
		fact5.setAmount(new BigDecimal("220.0"));
		fact5.setMargin(new BigDecimal("330.0"));
		fact5.setCount(40);
		//skip timestamp and time end
		when(dal.getDealerIncomeRevenueFacts(2013, Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(salesRevenueCategories), 
				Lists.newArrayList(new Long[]{}), 
				Lists.newArrayList(new Integer[]{}))).thenReturn(Lists.newArrayList(fact1, fact2, fact3, fact6));
		when(dal.getDealerIncomeRevenueFacts(2013, Lists.newArrayList(new Integer[]{8}), 
				Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(salesRevenueCategories), 
				Lists.newArrayList(new Long[]{}), 
				Lists.newArrayList(new Integer[]{}))).thenReturn(Lists.newArrayList(fact1, fact2));
		when(dal.getDealerIncomeRevenueFacts(2012, Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(new Integer[]{}), 
				Lists.newArrayList(salesRevenueCategories), 
				Lists.newArrayList(new Long[]{}), 
				Lists.newArrayList(new Integer[]{}))).thenReturn(Lists.newArrayList(fact4, fact5));
		final ReportItem item1 = new ReportItem();
		item1.setId(1L);
		item1.setItemCategory("C1");
		final ReportItem item2 = new ReportItem();
		item2.setId(2L);
		item2.setItemCategory("C1");
		when(dal.getReportItem(1L)).thenReturn(Option.<ReportItem>some(item1));
		when(dal.getReportItem(2L)).thenReturn(Option.<ReportItem>some(item2));
		
		final SalesServiceJournalItemDetail v1 = new SalesServiceJournalItemDetail();
		
		v1.setId(1);
		v1.setName("s1");
		v1.setCategoryID(1);
		when(refDataDAL.getSalesServiceRevenueItem("大客户采购（租车公司，政府机关）", "新车其它收入")).thenReturn(v1);
		when(refDataDAL.getSalesServiceRevenueItem("他店调车", "新车其它收入")).thenReturn(v1);
		when(refDataDAL.getSalesServiceRevenueItem("批发销售", "二手车其它收入")).thenReturn(v1);
		
		final QueryDealerSalesResponse response = service.queryDealerSalesReport(2013, Option.<Integer>none(), Option.<Integer>none());
		Assert.assertNotNull(response);
		Assert.assertEquals(2, response.getDetail().size());
		Assert.assertEquals(2012, response.getDetail().get(0).getYear().intValue());
		Assert.assertEquals(2013, response.getDetail().get(1).getYear().intValue());
		Assert.assertEquals(80.0, response.getDetail().get(1).getDetail().get(0).getOverall().getAmount());
		Assert.assertEquals(0.0, response.getDetail().get(1).getDetail().get(0).getRetail().getAmount());
		System.err.println(response);
		
		final QueryDealerSalesResponse response2 = service.queryDealerSalesReport(2013, Option.<Integer>some(8), Option.<Integer>none());
		Assert.assertNotNull(response2);
		Assert.assertEquals(2, response2.getDetail().size());
		Assert.assertEquals(2013, response2.getDetail().get(0).getYear().intValue());
		Assert.assertEquals(8, response2.getDetail().get(0).getMonth().intValue());
		Assert.assertEquals(2013, response2.getDetail().get(1).getYear().intValue());
		Assert.assertEquals(8, response2.getDetail().get(1).getMonth().intValue());
		Assert.assertEquals(6.25, response2.getDetail().get(0).getDetail().get(0).getOverall().getAmount());
		Assert.assertEquals(0.0, response2.getDetail().get(1).getDetail().get(0).getWholesale().getAmount());
		System.out.println(response2);
	}
}
