package com.jdc.themis.dealer.service.impl;

import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collection;

import javax.time.Instant;
import javax.time.calendar.LocalDate;
import javax.time.calendar.LocalDateTime;
import javax.time.calendar.TimeZone;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.jdc.themis.dealer.data.dao.IncomeJournalDAO;
import com.jdc.themis.dealer.domain.AccountReceivableDuration;
import com.jdc.themis.dealer.domain.DealerEntryItemStatus;
import com.jdc.themis.dealer.domain.EmployeeFee;
import com.jdc.themis.dealer.domain.EmployeeFeeSummary;
import com.jdc.themis.dealer.domain.EnumValue;
import com.jdc.themis.dealer.domain.GeneralJournal;
import com.jdc.themis.dealer.domain.HumanResourceAllocation;
import com.jdc.themis.dealer.domain.InventoryDuration;
import com.jdc.themis.dealer.domain.SalesServiceJournal;
import com.jdc.themis.dealer.domain.VehicleSalesJournal;
import com.jdc.themis.dealer.service.RefDataQueryService;
import com.jdc.themis.dealer.web.domain.AccountReceivableDurationDetail;
import com.jdc.themis.dealer.web.domain.AccountReceivableDurationItemDetail;
import com.jdc.themis.dealer.web.domain.DealerDetail;
import com.jdc.themis.dealer.web.domain.DepartmentDetail;
import com.jdc.themis.dealer.web.domain.DurationDetail;
import com.jdc.themis.dealer.web.domain.EmployeeFeeDetail;
import com.jdc.themis.dealer.web.domain.EmployeeFeeItemDetail;
import com.jdc.themis.dealer.web.domain.EmployeeFeeSummaryDetail;
import com.jdc.themis.dealer.web.domain.EmployeeFeeSummaryItemDetail;
import com.jdc.themis.dealer.web.domain.GeneralJournalDetail;
import com.jdc.themis.dealer.web.domain.GeneralJournalItemDetail;
import com.jdc.themis.dealer.web.domain.GetAccountReceivableDurationResponse;
import com.jdc.themis.dealer.web.domain.GetDealerEntryItemStatusResponse;
import com.jdc.themis.dealer.web.domain.GetEmployeeFeeResponse;
import com.jdc.themis.dealer.web.domain.GetEmployeeFeeSummaryResponse;
import com.jdc.themis.dealer.web.domain.GetGeneralJournalResponse;
import com.jdc.themis.dealer.web.domain.GetHumanResourceAllocationResponse;
import com.jdc.themis.dealer.web.domain.GetInventoryDurationResponse;
import com.jdc.themis.dealer.web.domain.GetSalesServiceJournalResponse;
import com.jdc.themis.dealer.web.domain.GetVehicleSalesJournalResponse;
import com.jdc.themis.dealer.web.domain.HumanResourceAllocationDetail;
import com.jdc.themis.dealer.web.domain.HumanResourceAllocationItemDetail;
import com.jdc.themis.dealer.web.domain.InventoryDurationDetail;
import com.jdc.themis.dealer.web.domain.InventoryDurationItemDetail;
import com.jdc.themis.dealer.web.domain.MenuDetail;
import com.jdc.themis.dealer.web.domain.SalesServiceJournalDetail;
import com.jdc.themis.dealer.web.domain.SalesServiceJournalItemDetail;
import com.jdc.themis.dealer.web.domain.SaveAccountReceivableDurationRequest;
import com.jdc.themis.dealer.web.domain.SaveDealerEntryItemStatusRequest;
import com.jdc.themis.dealer.web.domain.SaveEmployeeFeeRequest;
import com.jdc.themis.dealer.web.domain.SaveEmployeeFeeSummaryRequest;
import com.jdc.themis.dealer.web.domain.SaveGeneralJournalRequest;
import com.jdc.themis.dealer.web.domain.SaveHumanResourceAllocationRequest;
import com.jdc.themis.dealer.web.domain.SaveInventoryDurationRequest;
import com.jdc.themis.dealer.web.domain.SaveSalesServiceRevenueRequest;
import com.jdc.themis.dealer.web.domain.SaveVehicleSalesJournalRequest;
import com.jdc.themis.dealer.web.domain.VehicleDetail;
import com.jdc.themis.dealer.web.domain.VehicleSalesJournalDetail;

import fj.data.Option;

public class TestDealerIncomeEntryServiceImpl {

	private DealerIncomeEntryServiceImpl service;
	@Mock
	private IncomeJournalDAO dal;
	@Mock
	private RefDataQueryService refDataDAL;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks( this );
		dal = mock(IncomeJournalDAO.class);
		refDataDAL = mock(RefDataQueryService.class);
		service = new DealerIncomeEntryServiceImpl();
		service.setIncomeJournalDAL(dal);
		service.setRefDataQueryService(refDataDAL);
	}
	
	@Test
	public void saveVehicleSalesRevenueSuccessfully() {
		final DealerDetail dealer = new DealerDetail();
		dealer.setId(1);
		dealer.setName("Dealer1");
		when(refDataDAL.getDealer(1)).thenReturn(dealer);
		
		final Instant timestamp = LocalDateTime.parse("2014-01-01T00:00:00.001").atZone(TimeZone.UTC).toInstant();
		when(dal.saveVehicleSalesJournal(eq(1), Mockito.anyInt(), anyCollectionOf(VehicleSalesJournal.class))).thenReturn(timestamp);
		
		final SaveVehicleSalesJournalRequest request = new SaveVehicleSalesJournalRequest();
		request.setDealerID(1);
		request.setDepartmentID(null);
		request.setValidDate(LocalDate.of(2013, 7, 1).toString());
		
		final VehicleSalesJournalDetail detail = new VehicleSalesJournalDetail();
		detail.setAmount(123.4);
		detail.setMargin(123.5);
		detail.setCount(12345);
		detail.setVehicleID(1);
		request.getDetail().add(detail);
		when(refDataDAL.getVehicle(1)).thenReturn(new VehicleDetail());
		final Instant result = service.saveVehicleSalesRevenue(request);
		
		Assert.assertEquals("2014-01-01T00:00:00.001Z", result.toString());
	}
	
	@Test
	public void saveDealerEntryItemStatusSuccessfully() {
		final DealerDetail dealer = new DealerDetail();
		dealer.setId(1);
		dealer.setName("Dealer1");
		when(refDataDAL.getDealer(1)).thenReturn(dealer);
		
		final Instant timestamp = LocalDateTime.parse("2014-01-01T00:00:00.001").atZone(TimeZone.UTC).toInstant();
		when(dal.saveDealerEntryItemStatus(eq(1), anyCollectionOf(DealerEntryItemStatus.class))).thenReturn(timestamp);
		
		final SaveDealerEntryItemStatusRequest request = new SaveDealerEntryItemStatusRequest();
		request.setDealerID(1);
		request.setValidDate(LocalDate.of(2013, 7, 1).toString());
		request.setDealerID(1);
		request.setItemID(1);
		when(refDataDAL.getMenu(1)).thenReturn(new MenuDetail());
		final Instant result = service.saveDealerEntryItemStatus(request);
		
		Assert.assertEquals("2014-01-01T00:00:00.001Z", result.toString());
	}
	
	@Test
	public void getDealerEntryItemStatusSuccessfully() {
		final DealerDetail dealer = new DealerDetail();
		dealer.setId(1);
		dealer.setName("Dealer1");
		when(refDataDAL.getDealer(1)).thenReturn(dealer);
		
		final DealerEntryItemStatus detail = new DealerEntryItemStatus();
		detail.setDealerID(1);
		detail.setEntryItemID(1);
		detail.setValidDate(LocalDate.of(2013, 7, 1));
		
		final MenuDetail menu = new MenuDetail();
		menu.setId(1);
		menu.setName("test");
		when(refDataDAL.getMenu(1)).thenReturn(menu);
		when(dal.getDealerEntryItemStatus(eq(1), eq(LocalDate.of(2013, 7, 1)))).thenReturn(Lists.newArrayList(detail));
		
		final GetDealerEntryItemStatusResponse result = service.getDealerEntryItemStatus(1, "2013-07-01");
		
		Assert.assertEquals(LocalDate.of(2013, 7, 1), result.getValidDate());
		Assert.assertEquals(1, result.getDetail().size());
	}
	
	@Test
	public void getVehicleSalesRevenueSuccessfully() {
		final DealerDetail dealer = new DealerDetail();
		dealer.setId(1);
		dealer.setName("Dealer1");
		when(refDataDAL.getDealer(1)).thenReturn(dealer);
		
		final VehicleSalesJournal detail = new VehicleSalesJournal();
		detail.setAmount(new BigDecimal("123.4"));
		detail.setMargin(new BigDecimal("123.5"));
		detail.setCount(12345);
		detail.setId(1);
		when(refDataDAL.getVehicle(1)).thenReturn(new VehicleDetail());
		when(dal.getVehicleSalesJournal(eq(1), Mockito.anyInt(), eq(LocalDate.of(2013, 7, 1)))).thenReturn(Lists.newArrayList(detail));
		
		final GetVehicleSalesJournalResponse result = service.getVehicleSalesRevenue(1, Option.<Integer>none(), "2013-07-01", Option.<Integer>none());
		
		Assert.assertEquals(LocalDate.of(2013, 7, 1), result.getValidDate());
		Assert.assertEquals(1, result.getDetail().size());
		Assert.assertEquals(new BigDecimal("123.4").doubleValue(), result.getDetail().get(0).getAmount());
	}
	
	@Test
	public void saveSalesServiceRevenueSuccessfully() {
		final DealerDetail dealer = new DealerDetail();
		dealer.setId(1);
		dealer.setName("Dealer1");
		when(refDataDAL.getDealer(1)).thenReturn(dealer);
		
		final Instant timestamp = LocalDateTime.parse("2014-01-01T00:00:00.001").atZone(TimeZone.UTC).toInstant();
		when(dal.saveSalesServiceJournal(eq(1), Mockito.anyInt(), anyCollectionOf(SalesServiceJournal.class))).thenReturn(timestamp);
		final DepartmentDetail department = new DepartmentDetail();
		department.setId(4);
		department.setName("Department4");
		when(refDataDAL.getDepartment(4)).thenReturn(department);
		
		when(refDataDAL.getSalesServiceRevenueItem(1)).thenReturn(new SalesServiceJournalItemDetail());
		
		final SaveSalesServiceRevenueRequest request = new SaveSalesServiceRevenueRequest();
		request.setDealerID(1);
		request.setDepartmentID(4);
		request.setValidDate(LocalDate.of(2013, 7, 1).toString());
		
		final SalesServiceJournalDetail detail = new SalesServiceJournalDetail();
		detail.setAmount(123.4);
		detail.setMargin(123.5);
		detail.setCount(12345);
		detail.setItemID(1);
		request.getDetail().add(detail);
		when(refDataDAL.getVehicle(1)).thenReturn(new VehicleDetail());
		final Instant result = service.saveSalesServiceRevenue(request);
		
		Assert.assertEquals("2014-01-01T00:00:00.001Z", result.toString());
	}
	
	@Test
	public void getSalesServiceRevenueSuccessfully() {
		final DealerDetail dealer = new DealerDetail();
		dealer.setId(1);
		dealer.setName("Dealer1");
		when(refDataDAL.getDealer(1)).thenReturn(dealer);
		
		final SalesServiceJournal detail = new SalesServiceJournal();
		detail.setAmount(new BigDecimal("123.4"));
		detail.setMargin(new BigDecimal("123.5"));
		detail.setCount(12345);
		detail.setId(1);
		when(refDataDAL.getVehicle(1)).thenReturn(new VehicleDetail());
		when(refDataDAL.getDepartment(1)).thenReturn(new DepartmentDetail());
		final SalesServiceJournalItemDetail item = new SalesServiceJournalItemDetail();
		item.setId(1);
		item.setName("item1");
		item.setCategoryID(1);
		item.setCategory("C1");
		when(refDataDAL.getSalesServiceRevenueItem(1)).thenReturn(item);
		when(dal.getSalesServiceJournal(eq(1), Mockito.anyInt(), eq(LocalDate.of(2013, 7, 1)))).thenReturn(Lists.newArrayList(detail));
		
		final GetSalesServiceJournalResponse result = service.getSalesServiceRevenue(1, 1, "2013-07-01", Option.<Integer>none());
		
		Assert.assertEquals(LocalDate.of(2013, 7, 1), result.getValidDate());
		Assert.assertEquals(1, result.getDetail().size());
		Assert.assertEquals(new BigDecimal("123.4").doubleValue(), result.getDetail().get(0).getAmount());
	}
	
	@Test(expected=RuntimeException.class)
	public void saveVehicleSalesRevenueFailMissingDealerID() {
		final DealerDetail dealer = new DealerDetail();
		dealer.setId(1);
		dealer.setName("Dealer1");
		when(refDataDAL.getDealer(1)).thenReturn(dealer);
		
		final Instant timestamp = LocalDateTime.parse("2014-01-01T00:00:00.001").atZone(TimeZone.UTC).toInstant();
		when(dal.saveVehicleSalesJournal(eq(1), Mockito.anyInt(), anyCollectionOf(VehicleSalesJournal.class))).thenReturn(timestamp);
		
		final SaveVehicleSalesJournalRequest request = new SaveVehicleSalesJournalRequest();
		// request.setDealerID(1);
		request.setDepartmentID(null);
		request.setValidDate(LocalDate.of(2013, 7, 1).toString());
		final VehicleSalesJournalDetail detail = new VehicleSalesJournalDetail();
		request.getDetail().add(detail);
		
		service.saveVehicleSalesRevenue(request);
		
		Assert.fail("it should not success");
	}
	
	@Test(expected=RuntimeException.class)
	public void saveVehicleSalesRevenueFailMissingValidDate() {
		final DealerDetail dealer = new DealerDetail();
		dealer.setId(1);
		dealer.setName("Dealer1");
		when(refDataDAL.getDealer(1)).thenReturn(dealer);
		
		final Instant timestamp = LocalDateTime.parse("2014-01-01T00:00:00.001").atZone(TimeZone.UTC).toInstant();
		when(dal.saveVehicleSalesJournal(eq(1), Mockito.anyInt(), anyCollectionOf(VehicleSalesJournal.class))).thenReturn(timestamp);
		
		final SaveVehicleSalesJournalRequest request = new SaveVehicleSalesJournalRequest();
		request.setDealerID(1);
		request.setDepartmentID(null);
		final VehicleSalesJournalDetail detail = new VehicleSalesJournalDetail();
		request.getDetail().add(detail);
		
		service.saveVehicleSalesRevenue(request);
		
		Assert.fail("it should not success");
	}
	
	@Test(expected=RuntimeException.class)
	public void saveVehicleSalesRevenueFailMissingDetail() {
		final DealerDetail dealer = new DealerDetail();
		dealer.setId(1);
		dealer.setName("Dealer1");
		when(refDataDAL.getDealer(1)).thenReturn(dealer);
		
		final Instant timestamp = LocalDateTime.parse("2014-01-01T00:00:00.001").atZone(TimeZone.UTC).toInstant();
		when(dal.saveVehicleSalesJournal(eq(1), Mockito.anyInt(), anyCollectionOf(VehicleSalesJournal.class))).thenReturn(timestamp);
		
		final SaveVehicleSalesJournalRequest request = new SaveVehicleSalesJournalRequest();
		request.setDealerID(1);
		request.setDepartmentID(null);
		final VehicleSalesJournalDetail detail = new VehicleSalesJournalDetail();
		request.getDetail().add(detail);
		
		service.saveVehicleSalesRevenue(request);
		
		Assert.fail("it should not success");
	}
	
	@Test
	public void saveGeneralJournalSuccessfully() {
		final DealerDetail dealer = new DealerDetail();
		dealer.setId(2);
		dealer.setName("Dealer2");
		when(refDataDAL.getDealer(2)).thenReturn(dealer);
		
		final DepartmentDetail department = new DepartmentDetail();
		department.setId(4);
		department.setName("Department4");
		when(refDataDAL.getDepartment(4)).thenReturn(department);
		
		final Instant timestamp = LocalDateTime.parse("2013-02-01T00:00:00.001").atZone(TimeZone.UTC).toInstant();
		when(dal.saveGeneralJournal(eq(2), eq(4), anyCollectionOf(GeneralJournal.class))).thenReturn(timestamp);
		when(refDataDAL.getGeneralIncomeItem(eq(3))).thenReturn(new GeneralJournalItemDetail());
		
		final SaveGeneralJournalRequest request = new SaveGeneralJournalRequest();
		request.setDealerID(2);
		request.setDepartmentID(4);
		request.setValidDate(LocalDate.of(2013, 6, 1).toString());
		
		final GeneralJournalDetail detail = new GeneralJournalDetail();
		detail.setAmount(123.4);
		detail.setItemID(3);
		request.getDetail().add(detail);
		
		final Instant result = service.saveGeneralIncome(request);
		
		Assert.assertEquals("2013-02-01T00:00:00.001Z", result.toString());
	}
	
	@Test
	public void getGeneralJournalSuccessfully() {
		final DealerDetail dealer = new DealerDetail();
		dealer.setId(2);
		dealer.setName("Dealer2");
		when(refDataDAL.getDealer(2)).thenReturn(dealer);
		
		final DepartmentDetail department = new DepartmentDetail();
		department.setId(4);
		department.setName("Department4");
		when(refDataDAL.getDepartment(4)).thenReturn(department);
		
		final Instant timestamp = LocalDateTime.parse("2013-02-01T00:00:00.001").atZone(TimeZone.UTC).toInstant();
		final Instant timeEnd = LocalDateTime.parse("9999-01-01T00:00:00.000").atZone(TimeZone.UTC).toInstant();
		final GeneralJournal journal1 = new GeneralJournal();
		journal1.setDealerID(2);
		journal1.setDepartmentID(4);
		journal1.setId(5);
		journal1.setAmount(new BigDecimal("21023.343"));
		journal1.setTimestamp(timestamp);
		journal1.setTimeEnd(timeEnd);
		
		final Collection<GeneralJournal> list = Lists.newArrayList();
		list.add(journal1);
		
		final GeneralJournalItemDetail item = new GeneralJournalItemDetail();
		item.setId(5);
		item.setName("GJ Item 5");
		when(refDataDAL.getGeneralIncomeItem(eq(5))).thenReturn(item);
		
		when(dal.getGeneralJournal(eq(2), eq(4), eq(LocalDate.of(2013, 6, 1)))).thenReturn(list);
		
		final GetGeneralJournalResponse result = service.getGeneralIncome(2, 4, LocalDate.of(2013, 6, 1).toString(), Option.<Integer>none());
		
		Assert.assertEquals("2013-02-01T00:00:00.001Z", result.getDetail().get(0).getTimestamp().toString());
		Assert.assertEquals(new BigDecimal("21023.343").doubleValue(), result.getDetail().get(0).getAmount());
	}
	
	@Test
	public void getGeneralJournalSuccessfully2() {
		final DealerDetail dealer = new DealerDetail();
		dealer.setId(2);
		dealer.setName("Dealer2");
		when(refDataDAL.getDealer(2)).thenReturn(dealer);
		
		final DepartmentDetail department = new DepartmentDetail();
		department.setId(4);
		department.setName("Department4");
		when(refDataDAL.getDepartment(4)).thenReturn(department);
		
		final Instant timestamp = LocalDateTime.parse("2013-02-01T00:00:00.001").atZone(TimeZone.UTC).toInstant();
		final Instant timeEnd = LocalDateTime.parse("9999-01-01T00:00:00.000").atZone(TimeZone.UTC).toInstant();
		final GeneralJournal journal1 = new GeneralJournal();
		journal1.setDealerID(2);
		journal1.setDepartmentID(4);
		journal1.setId(5);
		journal1.setAmount(new BigDecimal("21023.343"));
		journal1.setTimestamp(timestamp);
		journal1.setTimeEnd(timeEnd);
		
		final Collection<GeneralJournal> list = Lists.newArrayList();
		list.add(journal1);
		
		final GeneralJournalItemDetail item = new GeneralJournalItemDetail();
		item.setId(5);
		item.setName("GJ Item 5");
		item.setCategory("GC1");
		item.setCategoryID(1);
		when(refDataDAL.getGeneralIncomeItem(eq(5))).thenReturn(item);
		
		when(dal.getGeneralJournal(eq(2), eq(4), eq(LocalDate.of(2013, 6, 1)))).thenReturn(list);
		
		final GetGeneralJournalResponse result = service.getGeneralIncome(2, 4, LocalDate.of(2013, 6, 1).toString(), Option.<Integer>some(2));
		
		Assert.assertEquals(0, result.getDetail().size());
	}
	
	@Test
	public void getGeneralJournalSuccessfully3() {
		final DealerDetail dealer = new DealerDetail();
		dealer.setId(2);
		dealer.setName("Dealer2");
		when(refDataDAL.getDealer(2)).thenReturn(dealer);
		
		final DepartmentDetail department = new DepartmentDetail();
		department.setId(4);
		department.setName("Department4");
		when(refDataDAL.getDepartment(4)).thenReturn(department);
		
		final Instant timestamp = LocalDateTime.parse("2013-02-01T00:00:00.001").atZone(TimeZone.UTC).toInstant();
		final Instant timeEnd = LocalDateTime.parse("9999-01-01T00:00:00.000").atZone(TimeZone.UTC).toInstant();
		final GeneralJournal journal1 = new GeneralJournal();
		journal1.setDealerID(2);
		journal1.setDepartmentID(4);
		journal1.setId(5);
		journal1.setAmount(new BigDecimal("21023.343"));
		journal1.setTimestamp(timestamp);
		journal1.setTimeEnd(timeEnd);
		
		final Collection<GeneralJournal> list = Lists.newArrayList();
		list.add(journal1);
		
		final GeneralJournalItemDetail item = new GeneralJournalItemDetail();
		item.setId(5);
		item.setName("GJ Item 5");
		item.setCategory("GC1");
		item.setCategoryID(2);
		when(refDataDAL.getGeneralIncomeItem(eq(5))).thenReturn(item);
		
		when(dal.getGeneralJournal(eq(2), eq(4), eq(LocalDate.of(2013, 6, 1)))).thenReturn(list);
		
		final GetGeneralJournalResponse result = service.getGeneralIncome(2, 4, LocalDate.of(2013, 6, 1).toString(), Option.<Integer>some(2));
		
		Assert.assertEquals(1, result.getDetail().size());
	}
	
	@Test
	public void saveAcctReceivableDurationSuccessfully() {
		final DealerDetail dealer = new DealerDetail();
		dealer.setId(2);
		dealer.setName("Dealer2");
		when(refDataDAL.getDealer(2)).thenReturn(dealer);
		
		final DurationDetail duration = new DurationDetail();
		duration.setId(1);
		duration.setLowerBound(0);
		duration.setUnit(1);
		duration.setUpperBound(30);
		when(refDataDAL.getDuration(1)).thenReturn(duration);
		
		final Instant timestamp = LocalDateTime.parse("2013-02-01T00:00:00.001").atZone(TimeZone.UTC).toInstant();
		when(dal.saveAccountReceivableDuration(eq(2), anyCollectionOf(AccountReceivableDuration.class))).thenReturn(timestamp);
		final AccountReceivableDurationItemDetail item = new AccountReceivableDurationItemDetail();
		item.setId(3);
		item.setName("GJ Item 3");
		when(refDataDAL.getAccountReceivableDurationItem(eq(3))).thenReturn(item);
		
		final SaveAccountReceivableDurationRequest request = new SaveAccountReceivableDurationRequest();
		request.setDealerID(2);
		request.setValidDate(LocalDate.of(2013, 6, 1).toString());
		
		final AccountReceivableDurationDetail detail = new AccountReceivableDurationDetail();
		detail.setAmount(123.4);
		detail.setItemID(3);
		detail.setDurationID(1);
		detail.setName("AcctDesc");
		request.getDetail().add(detail);
		
		final Instant result = service.saveAccountReceivableDuration(request);
		
		Assert.assertEquals("2013-02-01T00:00:00.001Z", result.toString());
	}
	
	@Test
	public void getAcctReceivableDurationSuccessfully() {
		final DealerDetail dealer = new DealerDetail();
		dealer.setId(2);
		dealer.setName("Dealer2");
		when(refDataDAL.getDealer(2)).thenReturn(dealer);
		
		final DurationDetail duration = new DurationDetail();
		duration.setId(1);
		duration.setUnit(1);
		duration.setLowerBound(0);
		duration.setUpperBound(null);
		when(refDataDAL.getDuration(1)).thenReturn(duration);
		
		final Instant timestamp = LocalDateTime.parse("2013-02-01T00:00:00.001").atZone(TimeZone.UTC).toInstant();
		final Instant timeEnd = LocalDateTime.parse("9999-01-01T00:00:00.000").atZone(TimeZone.UTC).toInstant();
		final AccountReceivableDuration journal1 = new AccountReceivableDuration();
		journal1.setDealerID(2);
		journal1.setDurationID(1);
		journal1.setId(5);
		journal1.setAmount(new BigDecimal("21023.343"));
		journal1.setTimestamp(timestamp);
		journal1.setTimeEnd(timeEnd);
		
		final Collection<AccountReceivableDuration> list = Lists.newArrayList();
		list.add(journal1);
		
		final AccountReceivableDurationItemDetail item = new AccountReceivableDurationItemDetail();
		item.setId(5);
		item.setName("GJ Item 5");
		when(refDataDAL.getAccountReceivableDurationItem(eq(5))).thenReturn(item);
		
		when(dal.getAccountReceivableDuration(eq(2), eq(LocalDate.of(2013, 6, 1)))).thenReturn(list);
		final EnumValue enumValue = new EnumValue();
		enumValue.setTypeID(1);
		enumValue.setValue(1);
		enumValue.setName("Days");
		when(refDataDAL.getEnumValue("DurationUnit", 1)).thenReturn(enumValue);
		
		final GetAccountReceivableDurationResponse result = service.getAccountReceivableDuration(2, LocalDate.of(2013, 6, 1).toString());
		
		Assert.assertEquals("2013-02-01T00:00:00.001Z", result.getDetail().get(0).getTimestamp().toString());
		Assert.assertEquals(new BigDecimal("21023.343").doubleValue(), result.getDetail().get(0).getAmount());
	}
	
	@Test
	public void saveInventoryDurationSuccessfully() {
		final DealerDetail dealer = new DealerDetail();
		dealer.setId(2);
		dealer.setName("Dealer2");
		when(refDataDAL.getDealer(2)).thenReturn(dealer);
		
		final DurationDetail duration = new DurationDetail();
		duration.setId(1);
		duration.setLowerBound(0);
		duration.setUnit(1);
		duration.setUpperBound(30);
		when(refDataDAL.getDuration(1)).thenReturn(duration);
		
		final Instant timestamp = LocalDateTime.parse("2013-02-01T00:00:00.001").atZone(TimeZone.UTC).toInstant();
		when(dal.saveInventoryDuration(eq(2), eq(3), anyCollectionOf(InventoryDuration.class))).thenReturn(timestamp);
		when(refDataDAL.getDepartment(eq(3))).thenReturn(new DepartmentDetail());
		final InventoryDurationItemDetail item = new InventoryDurationItemDetail();
		item.setId(5);
		item.setName("GJ Item 5");
		when(refDataDAL.getInventoryDurationItem(eq(5))).thenReturn(item);
		
		final SaveInventoryDurationRequest request = new SaveInventoryDurationRequest();
		request.setDealerID(2);
		request.setDepartmentID(3);
		request.setValidDate(LocalDate.of(2013, 6, 1).toString());
		
		final InventoryDurationDetail detail = new InventoryDurationDetail();
		detail.setAmount(123.4);
		detail.setItemID(5);
		detail.setDurationID(1);
		detail.setName("AcctDesc");
		request.getDetail().add(detail);
		
		final Instant result = service.saveInventoryDuration(request);
		
		Assert.assertEquals("2013-02-01T00:00:00.001Z", result.toString());
	}
	
	@Test(expected=RuntimeException.class)
	public void saveInventoryDurationFailMissingDurationID() {
		final DealerDetail dealer = new DealerDetail();
		dealer.setId(2);
		dealer.setName("Dealer2");
		when(refDataDAL.getDealer(2)).thenReturn(dealer);
		
		final DurationDetail duration = new DurationDetail();
		duration.setId(1);
		duration.setLowerBound(0);
		duration.setUnit(1);
		duration.setUpperBound(30);
		when(refDataDAL.getDuration(1)).thenReturn(duration);
		
		final Instant timestamp = LocalDateTime.parse("2013-02-01T00:00:00.001").atZone(TimeZone.UTC).toInstant();
		when(dal.saveInventoryDuration(eq(2), eq(3), anyCollectionOf(InventoryDuration.class))).thenReturn(timestamp);
		when(refDataDAL.getDepartment(eq(3))).thenReturn(new DepartmentDetail());
		final InventoryDurationItemDetail item = new InventoryDurationItemDetail();
		item.setId(5);
		item.setName("GJ Item 5");
		when(refDataDAL.getInventoryDurationItem(eq(5))).thenReturn(item);
		
		final SaveInventoryDurationRequest request = new SaveInventoryDurationRequest();
		request.setDealerID(2);
		request.setDepartmentID(3);
		request.setValidDate(LocalDate.of(2013, 6, 1).toString());
		
		final InventoryDurationDetail detail = new InventoryDurationDetail();
		detail.setAmount(123.4);
		detail.setItemID(5);
		// detail.setDurationID(1);
		detail.setName("AcctDesc");
		request.getDetail().add(detail);
		
		final Instant result = service.saveInventoryDuration(request);
		
		Assert.assertEquals("2013-02-01T00:00:00.001Z", result.toString());
	}
	
	@Test
	public void getInventoryDurationSuccessfully() {
		final DealerDetail dealer = new DealerDetail();
		dealer.setId(2);
		dealer.setName("Dealer2");
		when(refDataDAL.getDealer(2)).thenReturn(dealer);
		
		final DurationDetail duration = new DurationDetail();
		duration.setId(1);
		duration.setUnit(1);
		duration.setLowerBound(0);
		duration.setUpperBound(null);
		when(refDataDAL.getDuration(1)).thenReturn(duration);
		
		final Instant timestamp = LocalDateTime.parse("2013-02-01T00:00:00.001").atZone(TimeZone.UTC).toInstant();
		final Instant timeEnd = LocalDateTime.parse("9999-01-01T00:00:00.000").atZone(TimeZone.UTC).toInstant();
		final InventoryDuration journal1 = new InventoryDuration();
		journal1.setDealerID(2);
		journal1.setDurationID(1);
		journal1.setId(5);
		journal1.setAmount(new BigDecimal("21023.343"));
		journal1.setTimestamp(timestamp);
		journal1.setTimeEnd(timeEnd);
		
		final Collection<InventoryDuration> list = Lists.newArrayList();
		list.add(journal1);
		
		final InventoryDurationItemDetail item = new InventoryDurationItemDetail();
		item.setId(5);
		item.setName("GJ Item 5");
		when(refDataDAL.getInventoryDurationItem(eq(5))).thenReturn(item);
		
		when(refDataDAL.getDepartment(eq(3))).thenReturn(new DepartmentDetail());
		
		when(dal.getInventoryDuration(eq(2), eq(3), eq(LocalDate.of(2013, 6, 1)))).thenReturn(list);
		final EnumValue enumValue = new EnumValue();
		enumValue.setTypeID(1);
		enumValue.setValue(1);
		enumValue.setName("Days");
		when(refDataDAL.getEnumValue("DurationUnit", 1)).thenReturn(enumValue);
		
		final GetInventoryDurationResponse result = service.getInventoryDuration(2, 3, LocalDate.of(2013, 6, 1).toString());
		
		Assert.assertEquals("2013-02-01T00:00:00.001Z", result.getDetail().get(0).getTimestamp().toString());
		Assert.assertEquals(new BigDecimal("21023.343").doubleValue(), result.getDetail().get(0).getAmount());
	}
	
	@Test
	public void saveEmployeeFeeSuccessfully() {
		final DealerDetail dealer = new DealerDetail();
		dealer.setId(2);
		dealer.setName("Dealer2");
		when(refDataDAL.getDealer(2)).thenReturn(dealer);
		
		final DepartmentDetail department = new DepartmentDetail();
		department.setId(4);
		department.setName("Department4");
		when(refDataDAL.getDepartment(4)).thenReturn(department);
		when(refDataDAL.getEmployeeFeeItem(3)).thenReturn(new EmployeeFeeItemDetail());
		when(refDataDAL.getEnumValue("FeeType", 1)).thenReturn(new EnumValue());
		
		final Instant timestamp = LocalDateTime.parse("2013-02-01T00:00:00.001").atZone(TimeZone.UTC).toInstant();
		when(dal.saveEmployeeFee(eq(2), eq(4), anyCollectionOf(EmployeeFee.class))).thenReturn(timestamp);
		
		final SaveEmployeeFeeRequest request = new SaveEmployeeFeeRequest();
		request.setDealerID(2);
		request.setDepartmentID(4);
		request.setValidDate(LocalDate.of(2013, 6, 1).toString());
		
		final EmployeeFeeDetail detail = new EmployeeFeeDetail();
		detail.setAmount(123.4);
		detail.setItemID(3);
		detail.setFeeTypeID(1);
		request.getDetail().add(detail);
		
		final Instant result = service.saveEmployeeFee(request);
		
		Assert.assertEquals("2013-02-01T00:00:00.001Z", result.toString());
	}
	
	@Test
	public void saveEmployeeFeeSuccessfullyWithZeroAmount() {
		final DealerDetail dealer = new DealerDetail();
		dealer.setId(2);
		dealer.setName("Dealer2");
		when(refDataDAL.getDealer(2)).thenReturn(dealer);
		
		final DepartmentDetail department = new DepartmentDetail();
		department.setId(4);
		department.setName("Department4");
		when(refDataDAL.getDepartment(4)).thenReturn(department);
		when(refDataDAL.getEmployeeFeeItem(3)).thenReturn(new EmployeeFeeItemDetail());
		when(refDataDAL.getEnumValue("FeeType", 1)).thenReturn(new EnumValue());
		
		final Instant timestamp = LocalDateTime.parse("2013-02-01T00:00:00.001").atZone(TimeZone.UTC).toInstant();
		when(dal.saveEmployeeFee(eq(2), eq(4), anyCollectionOf(EmployeeFee.class))).thenReturn(timestamp);
		
		final SaveEmployeeFeeRequest request = new SaveEmployeeFeeRequest();
		request.setDealerID(2);
		request.setDepartmentID(4);
		request.setValidDate(LocalDate.of(2013, 6, 1).toString());
		
		final EmployeeFeeDetail detail = new EmployeeFeeDetail();
		detail.setAmount(null);
		detail.setItemID(3);
		detail.setFeeTypeID(1);
		request.getDetail().add(detail);
		
		final Instant result = service.saveEmployeeFee(request);
		
		Assert.assertEquals("2013-02-01T00:00:00.001Z", result.toString());
	}
	
	@Test
	public void getEmployeeFeeSuccessfully() {
		final DealerDetail dealer = new DealerDetail();
		dealer.setId(2);
		dealer.setName("Dealer2");
		when(refDataDAL.getDealer(2)).thenReturn(dealer);
		
		final DepartmentDetail department = new DepartmentDetail();
		department.setId(4);
		department.setName("Department4");
		when(refDataDAL.getDepartment(4)).thenReturn(department);
		
		final Instant timestamp = LocalDateTime.parse("2013-02-01T00:00:00.001").atZone(TimeZone.UTC).toInstant();
		final Instant timeEnd = LocalDateTime.parse("9999-01-01T00:00:00.000").atZone(TimeZone.UTC).toInstant();
		final EmployeeFee journal1 = new EmployeeFee();
		journal1.setDealerID(2);
		journal1.setDepartmentID(4);
		journal1.setId(5);
		journal1.setFeeTypeID(1);
		journal1.setAmount(new BigDecimal("21023.343"));
		journal1.setTimestamp(timestamp);
		journal1.setTimeEnd(timeEnd);
		
		final Collection<EmployeeFee> list = Lists.newArrayList();
		list.add(journal1);
		
		final EmployeeFeeItemDetail item = new EmployeeFeeItemDetail();
		item.setId(5);
		item.setName("GJ Item 5");
		when(refDataDAL.getEmployeeFeeItem(eq(5))).thenReturn(item);
		
		when(dal.getEmployeeFee(eq(2), eq(4), eq(LocalDate.of(2013, 6, 1)))).thenReturn(list);
		when(refDataDAL.getEnumValue("FeeType", 1)).thenReturn(new EnumValue());
		
		final GetEmployeeFeeResponse result = service.getEmployeeFee(2, 4, LocalDate.of(2013, 6, 1).toString());
		
		Assert.assertEquals("2013-02-01T00:00:00.001Z", result.getDetail().get(0).getTimestamp().toString());
		Assert.assertEquals(new BigDecimal("21023.343").doubleValue(), result.getDetail().get(0).getAmount());
	}
	
	@Test
	public void saveEmployeeFeeSummarySuccessfully() {
		final DealerDetail dealer = new DealerDetail();
		dealer.setId(2);
		dealer.setName("Dealer2");
		when(refDataDAL.getDealer(2)).thenReturn(dealer);
		
		final DepartmentDetail department = new DepartmentDetail();
		department.setId(4);
		department.setName("Department4");
		when(refDataDAL.getDepartment(4)).thenReturn(department);
		when(refDataDAL.getEmployeeFeeSummaryItem(3)).thenReturn(new EmployeeFeeSummaryItemDetail());
		
		final Instant timestamp = LocalDateTime.parse("2013-02-01T00:00:00.001").atZone(TimeZone.UTC).toInstant();
		when(dal.saveEmployeeFeeSummary(eq(2), eq(4), anyCollectionOf(EmployeeFeeSummary.class))).thenReturn(timestamp);
		
		final SaveEmployeeFeeSummaryRequest request = new SaveEmployeeFeeSummaryRequest();
		request.setDealerID(2);
		request.setDepartmentID(4);
		request.setValidDate(LocalDate.of(2013, 6, 1).toString());
		
		final EmployeeFeeSummaryDetail detail = new EmployeeFeeSummaryDetail();
		detail.setAmount(123.4);
		detail.setItemID(3);
		request.getDetail().add(detail);
		
		final Instant result = service.saveEmployeeFeeSummary(request);
		
		Assert.assertEquals("2013-02-01T00:00:00.001Z", result.toString());
	}
	
	@Test
	public void getEmployeeFeeSummarySuccessfully() {
		final DealerDetail dealer = new DealerDetail();
		dealer.setId(2);
		dealer.setName("Dealer2");
		when(refDataDAL.getDealer(2)).thenReturn(dealer);
		
		final DepartmentDetail department = new DepartmentDetail();
		department.setId(4);
		department.setName("Department4");
		when(refDataDAL.getDepartment(4)).thenReturn(department);
		
		final Instant timestamp = LocalDateTime.parse("2013-02-01T00:00:00.001").atZone(TimeZone.UTC).toInstant();
		final Instant timeEnd = LocalDateTime.parse("9999-01-01T00:00:00.000").atZone(TimeZone.UTC).toInstant();
		final EmployeeFeeSummary journal1 = new EmployeeFeeSummary();
		journal1.setDealerID(2);
		journal1.setDepartmentID(4);
		journal1.setId(5);
		journal1.setAmount(new BigDecimal("21023.343"));
		journal1.setTimestamp(timestamp);
		journal1.setTimeEnd(timeEnd);
		
		final Collection<EmployeeFeeSummary> list = Lists.newArrayList();
		list.add(journal1);
		
		final EmployeeFeeSummaryItemDetail item = new EmployeeFeeSummaryItemDetail();
		item.setId(5);
		item.setName("GJ Item 5");
		when(refDataDAL.getEmployeeFeeSummaryItem(eq(5))).thenReturn(item);
		
		when(dal.getEmployeeFeeSummary(eq(2), eq(4), eq(LocalDate.of(2013, 6, 1)))).thenReturn(list);
		
		final GetEmployeeFeeSummaryResponse result = service.getEmployeeFeeSummary(2, 4, LocalDate.of(2013, 6, 1).toString());
		
		Assert.assertEquals("2013-02-01T00:00:00.001Z", result.getDetail().get(0).getTimestamp().toString());
		Assert.assertEquals(new BigDecimal("21023.343").doubleValue(), result.getDetail().get(0).getAmount());
	}
	
	@Test
	public void saveHRAllocationSuccessfully() {
		final DealerDetail dealer = new DealerDetail();
		dealer.setId(2);
		dealer.setName("Dealer2");
		when(refDataDAL.getDealer(2)).thenReturn(dealer);
		
		final DepartmentDetail department = new DepartmentDetail();
		department.setId(4);
		department.setName("Department4");
		when(refDataDAL.getDepartment(4)).thenReturn(department);
		when(refDataDAL.getHumanResourceAllocationItem(3)).thenReturn(new HumanResourceAllocationItemDetail());
		
		final Instant timestamp = LocalDateTime.parse("2013-02-01T00:00:00.001").atZone(TimeZone.UTC).toInstant();
		when(dal.saveHumanResourceAllocation(eq(2), eq(4), anyCollectionOf(HumanResourceAllocation.class))).thenReturn(timestamp);
		
		final SaveHumanResourceAllocationRequest request = new SaveHumanResourceAllocationRequest();
		request.setDealerID(2);
		request.setDepartmentID(4);
		request.setValidDate(LocalDate.of(2013, 6, 1).toString());
		
		final HumanResourceAllocationDetail detail = new HumanResourceAllocationDetail();
		detail.setAllocation(123.4);
		detail.setItemID(3);
		request.getDetail().add(detail);
		
		final Instant result = service.saveHumanResourceAllocation(request);
		
		Assert.assertEquals("2013-02-01T00:00:00.001Z", result.toString());
	}
	
	@Test(expected=RuntimeException.class)
	public void saveHRAllocationFailUnknownDealer() {
		when(refDataDAL.getDealer(2)).thenReturn(null);
		
		final DepartmentDetail department = new DepartmentDetail();
		department.setId(4);
		department.setName("Department4");
		when(refDataDAL.getDepartment(4)).thenReturn(department);
		when(refDataDAL.getHumanResourceAllocationItem(3)).thenReturn(new HumanResourceAllocationItemDetail());
		
		final Instant timestamp = LocalDateTime.parse("2013-02-01T00:00:00.001").atZone(TimeZone.UTC).toInstant();
		when(dal.saveHumanResourceAllocation(eq(2), eq(4), anyCollectionOf(HumanResourceAllocation.class))).thenReturn(timestamp);
		
		final SaveHumanResourceAllocationRequest request = new SaveHumanResourceAllocationRequest();
		request.setDealerID(2);
		request.setDepartmentID(4);
		request.setValidDate(LocalDate.of(2013, 6, 1).toString());
		
		final HumanResourceAllocationDetail detail = new HumanResourceAllocationDetail();
		detail.setAllocation(123.4);
		detail.setItemID(3);
		request.getDetail().add(detail);
		
		service.saveHumanResourceAllocation(request);
		
		Assert.fail();
	}
	
	@Test
	public void getHRAllocationSuccessfully() {
		final DealerDetail dealer = new DealerDetail();
		dealer.setId(2);
		dealer.setName("Dealer2");
		when(refDataDAL.getDealer(2)).thenReturn(dealer);
		
		final DepartmentDetail department = new DepartmentDetail();
		department.setId(4);
		department.setName("Department4");
		when(refDataDAL.getDepartment(4)).thenReturn(department);
		
		final Instant timestamp = LocalDateTime.parse("2013-02-01T00:00:00.001").atZone(TimeZone.UTC).toInstant();
		final Instant timeEnd = LocalDateTime.parse("9999-01-01T00:00:00.000").atZone(TimeZone.UTC).toInstant();
		final HumanResourceAllocation journal1 = new HumanResourceAllocation();
		journal1.setDealerID(2);
		journal1.setDepartmentID(4);
		journal1.setId(5);
		journal1.setAllocation(new BigDecimal("0.2"));
		journal1.setTimestamp(timestamp);
		journal1.setTimeEnd(timeEnd);
		
		final Collection<HumanResourceAllocation> list = Lists.newArrayList();
		list.add(journal1);
		
		final HumanResourceAllocationItemDetail item = new HumanResourceAllocationItemDetail();
		item.setId(5);
		item.setName("GJ Item 5");
		when(refDataDAL.getHumanResourceAllocationItem(eq(5))).thenReturn(item);
		
		when(dal.getHumanResourceAllocation(eq(2), eq(LocalDate.of(2013, 6, 1)))).thenReturn(list);
		
		final GetHumanResourceAllocationResponse result = service.getHumanResourceAllocation(2, LocalDate.of(2013, 6, 1).toString());
		
		Assert.assertEquals("2013-02-01T00:00:00.001Z", result.getDetail().get(0).getTimestamp().toString());
		Assert.assertEquals(new BigDecimal("0.2").doubleValue(), result.getDetail().get(0).getAllocation());
		Assert.assertEquals(4, result.getDetail().get(0).getDepartmentID().intValue());
	}
}
