package com.jdc.themis.dealer.report.job;

import static org.mockito.Mockito.mock;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.time.calendar.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import com.jdc.themis.dealer.service.DealerIncomeReportService;
import com.jdc.themis.dealer.web.domain.ImportReportDataRequest;

public class TestDealerIncomeReportGeneratorJob {

	@Mock
	private DealerIncomeReportService service;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks( this );
		service = mock(DealerIncomeReportService.class);
	}
	
	@Test
	public void executeSuccessfully() {
		final DealerIncomeReportGeneratorJob job = new DealerIncomeReportGeneratorJob();
		job.setService(service);
		job.execute();
		final Calendar c = new GregorianCalendar();
		c.setTime(new Date());
		final LocalDate value = LocalDate.of(c.get(Calendar.YEAR),
				c.get(Calendar.MONTH) + 1, 1);
		final ImportReportDataRequest request = new ImportReportDataRequest();
		request.setFromDate(value.toString());
		request.setToDate(value.toString());
		verify(service).importReportData(request);
	}
	
}
