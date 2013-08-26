package com.jdc.themis.dealer.service;

import org.springframework.transaction.annotation.Transactional;

import com.jdc.themis.dealer.web.domain.GetMenuResponse;
import com.jdc.themis.dealer.web.domain.GetSalesServiceRevenueItemResponse;
import com.jdc.themis.dealer.web.domain.GetVehicleResponse;

public interface RefDataQueryService {
	
	@Transactional(readOnly=true)
	GetMenuResponse getAllMenu();
	
	@Transactional(readOnly=true)
	GetVehicleResponse getAllVehicles();
	
	@Transactional(readOnly=true)
	GetSalesServiceRevenueItemResponse getAllSalesServiceRevenueItems();
}
