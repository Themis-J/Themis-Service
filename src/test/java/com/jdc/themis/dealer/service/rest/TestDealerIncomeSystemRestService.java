package com.jdc.themis.dealer.service.rest;

import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import com.jdc.themis.dealer.service.RefDataQueryService;

import fj.data.Option;

public class TestDealerIncomeSystemRestService {

	@Mock
	private RefDataQueryService refDataQueryService;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks( this );
		refDataQueryService = mock(RefDataQueryService.class);
	} 
	
	@Test
	public void getAllVehicles() {
		final DealerIncomeSystemRestService service = new DealerIncomeSystemRestService();
		service.setRefDataQueryService(refDataQueryService);
		service.getVehicles(null);
		verify(this.refDataQueryService).getVehicles(Option.<Integer>none());
	}
	
	@Test
	public void getVehiclesByCategory() {
		final DealerIncomeSystemRestService service = new DealerIncomeSystemRestService();
		service.setRefDataQueryService(refDataQueryService);
		service.getVehicles(2);
		verify(this.refDataQueryService).getVehicles(Option.<Integer>some(2));
	}
}
