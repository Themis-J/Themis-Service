package com.jdc.themis.dealer.service.rest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.jdc.themis.dealer.service.DealerIncomeReportService;
import com.jdc.themis.dealer.web.domain.ImportReportDataRequest;

public class TestDealerReportRestService {

	@Mock
	private DealerIncomeReportService dealerIncomeReportService;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks( this );
		dealerIncomeReportService = mock(DealerIncomeReportService.class);
	} 
	
	@Test
	public void importReportData() {
		final DealerReportRestService service = new DealerReportRestService();
		service.setDealerIncomeReportService(dealerIncomeReportService);
		final ImportReportDataRequest request = new ImportReportDataRequest();
		service.importReportData(request);
		verify(this.dealerIncomeReportService).importReportData(request);
	}
	
}
