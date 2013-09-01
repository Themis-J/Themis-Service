package com.jdc.themis.dealer.service;

import org.springframework.transaction.annotation.Transactional;

import com.jdc.themis.dealer.web.domain.DepartmentDetail;
import com.jdc.themis.dealer.web.domain.GeneralJournalItemDetail;
import com.jdc.themis.dealer.web.domain.GetDepartmentResponse;
import com.jdc.themis.dealer.web.domain.GetGeneralJournalItemResponse;
import com.jdc.themis.dealer.web.domain.GetMenuResponse;
import com.jdc.themis.dealer.web.domain.GetSalesServiceJournalItemResponse;
import com.jdc.themis.dealer.web.domain.GetVehicleResponse;
import com.jdc.themis.dealer.web.domain.MenuDetail;
import com.jdc.themis.dealer.web.domain.SalesServiceJournalItemDetail;
import com.jdc.themis.dealer.web.domain.VehicleDetail;

public interface RefDataQueryService {
	
	@Transactional(readOnly=true)
	GetMenuResponse getAllMenu();
	
	@Transactional(readOnly=true)
	MenuDetail getMenu(Integer id);

	@Transactional(readOnly=true)
	GetDepartmentResponse getAllDepartments();
	
	@Transactional(readOnly=true)
	DepartmentDetail getDepartment(Integer id);
	
	@Transactional(readOnly=true)
	GetVehicleResponse getAllVehicles();
	
	@Transactional(readOnly=true)
	VehicleDetail getVehicle(Integer id);
	
	@Transactional(readOnly=true)
	GetSalesServiceJournalItemResponse getAllSalesServiceRevenueItems();
	
	@Transactional(readOnly=true)
	SalesServiceJournalItemDetail getSalesServiceRevenueItem(Integer id);
	
	@Transactional(readOnly=true)
	GetGeneralJournalItemResponse getAllGeneralIncomeItems();
	
	@Transactional(readOnly=true)
	GeneralJournalItemDetail getGeneralIncomeItem(Integer id);
}
