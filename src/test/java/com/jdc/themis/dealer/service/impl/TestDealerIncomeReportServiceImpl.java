package com.jdc.themis.dealer.service.impl;

import static org.mockito.Mockito.mock;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import javax.time.calendar.LocalDate;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.jdc.themis.dealer.data.dao.RefDataDAO;
import com.jdc.themis.dealer.data.dao.ReportDAO;
import com.jdc.themis.dealer.domain.Dealer;
import com.jdc.themis.dealer.domain.DealerIncomeRevenueFact;
import com.jdc.themis.dealer.domain.ReportItem;
import com.jdc.themis.dealer.domain.ReportTime;
import com.jdc.themis.dealer.web.domain.QueryReportDataResponse;

import fj.data.Option;

public class TestDealerIncomeReportServiceImpl {
	private DealerIncomeReportServiceImpl service;
	@Mock
	private ReportDAO dal;
	@Mock
	private RefDataDAO refDataDAL;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks( this );
		dal = mock(ReportDAO.class);
		refDataDAL = mock(RefDataDAO.class);
		service = new DealerIncomeReportServiceImpl();
		service.setReportDAL(dal);
		service.setRefDataDAL(refDataDAL);
	}
	
	@Test
	public void queryYearlyOverallIncomeReport() {
		final Dealer dealer1 = new Dealer();
		dealer1.setId(1);
		dealer1.setName("Dealer1");
		final Dealer dealer2 = new Dealer();
		dealer2.setId(2);
		dealer2.setName("Dealer2");
		
		when(refDataDAL.getDealers()).thenReturn(Lists.newArrayList(dealer1, dealer2));
		
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
		when(dal.getDealerIncomeRevenueFacts(2013, Option.<Integer>none(), Option.<Integer>none())).thenReturn(Lists.newArrayList(fact1, fact2, fact3));
		when(dal.getDealerIncomeRevenueFacts(2012, Option.<Integer>none(), Option.<Integer>none())).thenReturn(Lists.newArrayList(fact4, fact5));
		final ReportItem item1 = new ReportItem();
		item1.setId(1L);
		item1.setItemCategory("C1");
		final ReportItem item2 = new ReportItem();
		item2.setId(2L);
		item2.setItemCategory("C1");
		when(dal.getReportItem(1L)).thenReturn(Option.<ReportItem>some(item1));
		when(dal.getReportItem(2L)).thenReturn(Option.<ReportItem>some(item2));
		final QueryReportDataResponse response = service.queryYearlyOverallIncomeReport(2013);
		Assert.assertNotNull(response);
		Assert.assertEquals(2, response.getDetail().size());
		Assert.assertEquals(2012, response.getDetail().get(0).getYear().intValue());
		Assert.assertEquals(2013, response.getDetail().get(1).getYear().intValue());
		Assert.assertEquals(3000.0, response.getDetail().get(1).getDetail().get(0).getRevenue().getAmount());
		Assert.assertEquals(5000.0, response.getDetail().get(1).getDetail().get(0).getMargin().getAmount());
		System.err.println(response);
	}
}
