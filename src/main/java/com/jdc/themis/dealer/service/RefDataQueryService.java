package com.jdc.themis.dealer.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import com.jdc.themis.dealer.web.domain.AccountReceivableDurationItemDetail;
import com.jdc.themis.dealer.web.domain.DealerDetail;
import com.jdc.themis.dealer.web.domain.DepartmentDetail;
import com.jdc.themis.dealer.web.domain.DurationDetail;
import com.jdc.themis.dealer.web.domain.EmployeeFeeItemDetail;
import com.jdc.themis.dealer.web.domain.EmployeeFeeSummaryItemDetail;
import com.jdc.themis.dealer.web.domain.GeneralJournalItemDetail;
import com.jdc.themis.dealer.web.domain.GetDepartmentResponse;
import com.jdc.themis.dealer.web.domain.GetGeneralJournalItemResponse;
import com.jdc.themis.dealer.web.domain.GetMenuResponse;
import com.jdc.themis.dealer.web.domain.GetSalesServiceJournalItemResponse;
import com.jdc.themis.dealer.web.domain.GetVehicleResponse;
import com.jdc.themis.dealer.web.domain.HumanResourceAllocationItemDetail;
import com.jdc.themis.dealer.web.domain.InventoryDurationItemDetail;
import com.jdc.themis.dealer.web.domain.MenuDetail;
import com.jdc.themis.dealer.web.domain.SalesServiceJournalItemDetail;
import com.jdc.themis.dealer.web.domain.VehicleDetail;

public interface RefDataQueryService {
	
	@Transactional(readOnly=true)
	GetMenuResponse getAllMenu();
	
	@Transactional(readOnly=true)
	@Cacheable("menu")
	MenuDetail getMenu(Integer id);

	@Transactional(readOnly=true)
	@Cacheable("dealer")
	DealerDetail getDealer(Integer id);

	@Transactional(readOnly=true)
	GetDepartmentResponse getAllDepartments();
	
	@Transactional(readOnly=true)
	@Cacheable("department")
	DepartmentDetail getDepartment(Integer id);
	
	@Transactional(readOnly=true)
	GetVehicleResponse getAllVehicles();
	
	@Transactional(readOnly=true)
	@Cacheable("vehicle")
	VehicleDetail getVehicle(Integer id);
	
	@Transactional(readOnly=true)
	GetSalesServiceJournalItemResponse getAllSalesServiceRevenueItems();
	
	@Transactional(readOnly=true)
	@Cacheable("salesServiceJournalItem")
	SalesServiceJournalItemDetail getSalesServiceRevenueItem(Integer id);
	
	@Transactional(readOnly=true)
	GetGeneralJournalItemResponse getAllGeneralIncomeItems();
	
	@Transactional(readOnly=true)
	@Cacheable("generalJournalItem")
	GeneralJournalItemDetail getGeneralIncomeItem(Integer id);
	
	@Transactional(readOnly=true)
	@Cacheable("inventoryDurationItem")
	InventoryDurationItemDetail getInventoryDurationItem(Integer id);
	
	@Transactional(readOnly=true)
	@Cacheable("accountReceivableDurationItem")
	AccountReceivableDurationItemDetail getAccountReceivableDurationItem(Integer id);
	
	@Transactional(readOnly=true)
	@Cacheable("employeeFeeItem")
	EmployeeFeeItemDetail getEmployeeFeeItem(Integer id);
	
	@Transactional(readOnly=true)
	@Cacheable("employeeFeeSummaryItem")
	EmployeeFeeSummaryItemDetail getEmployeeFeeSummaryItem(Integer id);
	
	@Transactional(readOnly=true)
	@Cacheable("jobPosition")
	HumanResourceAllocationItemDetail getHumanResourceAllocationItem(Integer id);
	
	@Transactional(readOnly=true)
	@Cacheable("duration")
	DurationDetail getDuration(Integer id);
}
