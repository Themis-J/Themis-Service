package com.jdc.themis.dealer.service;

import org.springframework.transaction.annotation.Transactional;

import com.jdc.themis.dealer.web.domain.QueryReportDataResponse;

import fj.data.Option;

public interface DealerIncomeReportService {

	@Transactional
	void importReportData(Integer year, Option<Integer> monthOfYear);
	
	@Transactional(readOnly=true)
	QueryReportDataResponse queryYearlyOverallIncomeReport(Integer year);
}
