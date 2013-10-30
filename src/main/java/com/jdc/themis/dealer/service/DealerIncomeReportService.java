package com.jdc.themis.dealer.service;


import org.springframework.transaction.annotation.Transactional;

import com.jdc.themis.dealer.web.domain.ImportReportDataRequest;
import com.jdc.themis.dealer.web.domain.QueryDealerExpensePercentageResponse;
import com.jdc.themis.dealer.web.domain.QueryDealerIncomeResponse;
import com.jdc.themis.dealer.web.domain.QueryDealerSalesResponse;
import com.jdc.themis.dealer.web.domain.QueryDepartmentIncomeResponse;

import fj.data.Option;

public interface DealerIncomeReportService {

	@Transactional
	void importReportData(ImportReportDataRequest request);
	
	@Transactional(readOnly=true)
	QueryDealerIncomeResponse queryOverallIncomeReport(
			Integer year, Option<Integer> monthOfYear, Option<Integer> departmentID, Option<Integer> denominator);
	
	@Transactional(readOnly=true)
	QueryDealerExpensePercentageResponse queryOverallExpensePercentageReport(
			Integer year, Integer monthOfYear, Option<Integer> denominator, Option<Integer> categoryID, Option<Integer> itemID);
	
	@Transactional(readOnly=true)
	QueryDepartmentIncomeResponse queryDepartmentIncomeReport(
			Integer year, Option<Integer> monthOfYear, Option<Integer> dealerID, Option<Integer> departmentID);
	
	@Transactional(readOnly=true)
	QueryDealerSalesResponse queryDealerSalesReport(
			Integer year, Option<Integer> monthOfYear, Option<Integer> departmentID);
	
}
