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
import com.jdc.themis.dealer.data.dao.RefDataDAO;
import com.jdc.themis.dealer.domain.Dealer;
import com.jdc.themis.dealer.domain.Department;
import com.jdc.themis.dealer.domain.GeneralJournal;
import com.jdc.themis.dealer.domain.GeneralJournalItem;
import com.jdc.themis.dealer.domain.VehicleSalesJournal;
import com.jdc.themis.dealer.web.domain.GeneralJournalDetail;
import com.jdc.themis.dealer.web.domain.GetGeneralJournalResponse;
import com.jdc.themis.dealer.web.domain.SaveGeneralJournalRequest;
import com.jdc.themis.dealer.web.domain.SaveVehicleSalesJournalRequest;
import com.jdc.themis.dealer.web.domain.VehicleSalesJournalDetail;

public class TestDealerIncomeEntryServiceImpl {

	private DealerIncomeEntryServiceImpl service;
	@Mock
	private IncomeJournalDAO dal;
	@Mock
	private RefDataDAO refDataDAL;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks( this );
		dal = mock(IncomeJournalDAO.class);
		refDataDAL = mock(RefDataDAO.class);
		service = new DealerIncomeEntryServiceImpl();
		service.setIncomeJournalDAL(dal);
		service.setRefDataDAL(refDataDAL);
	}
	
	@Test
	public void saveVehicleSalesRevenueSuccessfully() {
		final Dealer dealer = new Dealer();
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
		
		final Instant result = service.saveVehicleSalesRevenue(request);
		
		Assert.assertEquals("2014-01-01T00:00:00.001Z", result.toString());
	}
	
	@Test(expected=RuntimeException.class)
	public void saveVehicleSalesRevenueFailMissingDealerID() {
		final Dealer dealer = new Dealer();
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
		final Dealer dealer = new Dealer();
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
		final Dealer dealer = new Dealer();
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
		final Dealer dealer = new Dealer();
		dealer.setId(2);
		dealer.setName("Dealer2");
		when(refDataDAL.getDealer(2)).thenReturn(dealer);
		
		final Department department = new Department();
		department.setId(4);
		department.setName("Department4");
		when(refDataDAL.getDepartment(4)).thenReturn(department);
		
		final Instant timestamp = LocalDateTime.parse("2013-02-01T00:00:00.001").atZone(TimeZone.UTC).toInstant();
		when(dal.saveGeneralJournal(eq(2), eq(4), anyCollectionOf(GeneralJournal.class))).thenReturn(timestamp);
		
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
		final Dealer dealer = new Dealer();
		dealer.setId(2);
		dealer.setName("Dealer2");
		when(refDataDAL.getDealer(2)).thenReturn(dealer);
		
		final Department department = new Department();
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
		
		final GeneralJournalItem item = new GeneralJournalItem();
		item.setId(5);
		item.setName("GJ Item 5");
		when(refDataDAL.getGeneralJournalItem(eq(5))).thenReturn(item);
		
		when(dal.getGeneralJournal(eq(2), eq(4), eq(LocalDate.of(2013, 6, 1)))).thenReturn(list);
		
		final GetGeneralJournalResponse result = service.getGeneralIncome(2, 4, LocalDate.of(2013, 6, 1).toString());
		
		Assert.assertEquals("2013-02-01T00:00:00.001Z", result.getDetail().get(0).getTimestamp().toString());
		Assert.assertEquals(new BigDecimal("21023.343").doubleValue(), result.getDetail().get(0).getAmount());
	}
}
