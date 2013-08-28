package com.jdc.themis.dealer.service.impl;

import javax.time.Instant;
import javax.time.calendar.LocalDate;
import javax.time.calendar.LocalDateTime;
import javax.time.calendar.TimeZone;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;
import com.jdc.themis.dealer.data.dao.IncomeJournalDAO;
import com.jdc.themis.dealer.data.dao.RefDataDAO;
import com.jdc.themis.dealer.domain.Dealer;
import com.jdc.themis.dealer.domain.VehicleSalesJournal;
import com.jdc.themis.dealer.web.domain.SaveVehicleSalesJournalRequest;
import com.jdc.themis.dealer.web.domain.VehicleSalesJournalDetail;

import static org.mockito.Mockito.*;

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
}
