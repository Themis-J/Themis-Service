package com.jdc.themis.dealer.service;

import org.springframework.transaction.annotation.Transactional;

import com.jdc.themis.dealer.web.domain.GetDepartmentResponse;
import com.jdc.themis.dealer.web.domain.GetGeneralJournalItemResponse;
import com.jdc.themis.dealer.web.domain.GetMenuResponse;
import com.jdc.themis.dealer.web.domain.GetSalesServiceJournalItemResponse;
import com.jdc.themis.dealer.web.domain.GetVehicleResponse;

public interface RefDataQueryService {
	
	@Transactional(readOnly=true)
	GetMenuResponse getAllMenu();
	
	@Transactional(readOnly=true)
	GetDepartmentResponse getAllDepartments();
	
	@Transactional(readOnly=true)
	GetVehicleResponse getAllVehicles();
	
	@Transactional(readOnly=true)
	GetSalesServiceJournalItemResponse getAllSalesServiceRevenueItems();
	
	@Transactional(readOnly=true)
	GetGeneralJournalItemResponse getAllGeneralIncomeItems();
}
