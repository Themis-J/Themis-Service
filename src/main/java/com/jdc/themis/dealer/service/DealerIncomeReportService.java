package com.jdc.themis.dealer.service;


import org.springframework.transaction.annotation.Transactional;

import com.jdc.themis.dealer.web.domain.ImportReportDataRequest;
import com.jdc.themis.dealer.web.domain.QueryReportDataResponse;

import fj.data.Option;

public interface DealerIncomeReportService {

	@Transactional
	void importReportData(ImportReportDataRequest request);
	
	@Transactional(readOnly=true)
	QueryReportDataResponse queryOverallIncomeReport(
			Integer year, Option<Integer> monthOfYear, Option<Integer> departmentID, Option<Integer> denominator);
	
	@Transactional(readOnly=true)
	QueryReportDataResponse queryDepartmentIncomeReport(
			Integer year, Option<Integer> monthOfYear, Option<Integer> dealerID, Option<Integer> departmentID);
	
	@Transactional(readOnly=true)
	QueryReportDataResponse querySalesReport(
			Integer year, Option<Integer> monthOfYear, Option<Integer> departmentID);
	
}
