package com.jdc.themis.dealer.service;

import org.springframework.transaction.annotation.Transactional;

public interface DealerIncomeReportService {

	@Transactional
	void importReportData(Integer year, Integer monthOfYear);
	
}
