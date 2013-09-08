package com.jdc.themis.dealer.report.job;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.time.calendar.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jdc.themis.dealer.service.DealerIncomeReportService;

@Service
public class DealerIncomeReportGeneratorJob { // extends QuartzJobBean {
	private final static Logger logger = LoggerFactory
			.getLogger(DealerIncomeReportGeneratorJob.class);

	@Autowired
	private DealerIncomeReportService service;

	public DealerIncomeReportService getService() {
		return service;
	}

	public void setService(DealerIncomeReportService service) {
		this.service = service;
	}

	public void execute() {
		final Calendar c = new GregorianCalendar();
		c.setTime(new Date());
		final LocalDate value = LocalDate.of(c.get(Calendar.YEAR),
				c.get(Calendar.MONTH) + 1, 1);
		logger.info("Importing report data for date {}", value);
		service.importReportData(value);
	}

}
