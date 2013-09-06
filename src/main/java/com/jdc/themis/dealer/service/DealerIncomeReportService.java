package com.jdc.themis.dealer.service;

import javax.time.calendar.LocalDate;

import org.springframework.transaction.annotation.Transactional;

import com.jdc.themis.dealer.web.domain.QueryReportDataResponse;

public interface DealerIncomeReportService {

	@Transactional
	void importReportData(LocalDate validDate);
	
	@Transactional(readOnly=true)
	QueryReportDataResponse queryYearlyOverallIncomeReport(Integer year);
}
