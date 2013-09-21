package com.jdc.themis.dealer.service;

import java.util.Collection;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import com.jdc.themis.dealer.domain.EnumValue;
import com.jdc.themis.dealer.web.domain.AccountReceivableDurationItemDetail;
import com.jdc.themis.dealer.web.domain.DealerDetail;
import com.jdc.themis.dealer.web.domain.DepartmentDetail;
import com.jdc.themis.dealer.web.domain.DurationDetail;
import com.jdc.themis.dealer.web.domain.EmployeeFeeItemDetail;
import com.jdc.themis.dealer.web.domain.EmployeeFeeSummaryItemDetail;
import com.jdc.themis.dealer.web.domain.GeneralJournalItemDetail;
import com.jdc.themis.dealer.web.domain.GetAccountReceivableDurationItemResponse;
import com.jdc.themis.dealer.web.domain.GetDepartmentResponse;
import com.jdc.themis.dealer.web.domain.GetEmployeeFeeItemResponse;
import com.jdc.themis.dealer.web.domain.GetEmployeeFeeSummaryItemResponse;
import com.jdc.themis.dealer.web.domain.GetGeneralJournalItemResponse;
import com.jdc.themis.dealer.web.domain.GetInventoryDurationItemResponse;
import com.jdc.themis.dealer.web.domain.GetHumanResourceAllocationItemResponse;
import com.jdc.themis.dealer.web.domain.GetMenuResponse;
import com.jdc.themis.dealer.web.domain.GetSalesServiceJournalItemResponse;
import com.jdc.themis.dealer.web.domain.GetVehicleResponse;
import com.jdc.themis.dealer.web.domain.HumanResourceAllocationItemDetail;
import com.jdc.themis.dealer.web.domain.InventoryDurationItemDetail;
import com.jdc.themis.dealer.web.domain.MenuDetail;
import com.jdc.themis.dealer.web.domain.SalesServiceJournalItemDetail;
import com.jdc.themis.dealer.web.domain.VehicleDetail;

import fj.data.Option;

public interface RefDataQueryService {
	
	@Transactional(readOnly=true)
	GetMenuResponse getMenu();
	
	@Transactional(readOnly=true)
	@Cacheable("menu")
	MenuDetail getMenu(Integer id);

	@Transactional(readOnly=true)
	@Cacheable("dealer")
	DealerDetail getDealer(Integer id);

	@Transactional(readOnly=true)
	Collection<DealerDetail> getDealers();
	
	@Transactional(readOnly=true)
	GetDepartmentResponse getDepartments();
	
	@Transactional(readOnly=true)
	@Cacheable("department")
	DepartmentDetail getDepartment(Integer id);
	
	@Transactional(readOnly=true)
	GetVehicleResponse getVehicles(Option<Integer> categoryID);
	
	@Transactional(readOnly=true)
	@Cacheable("vehicle")
	VehicleDetail getVehicle(Integer id);
	
	@Transactional(readOnly=true)
	GetSalesServiceJournalItemResponse getSalesServiceRevenueItems(Option<Integer> categoryID);
	
	@Transactional(readOnly=true)
	@Cacheable("salesServiceJournalItem")
	SalesServiceJournalItemDetail getSalesServiceRevenueItem(Integer id);
	
	@Transactional(readOnly=true)
	GetGeneralJournalItemResponse getGeneralIncomeItems(Option<Integer> categoryID);
	
	@Transactional(readOnly=true)
	@Cacheable("generalJournalItem")
	GeneralJournalItemDetail getGeneralIncomeItem(Integer id);
	
	@Transactional(readOnly=true)
	@Cacheable("inventoryDurationItem")
	InventoryDurationItemDetail getInventoryDurationItem(Integer id);
	
	@Transactional(readOnly=true)
	GetInventoryDurationItemResponse getInventoryDurationItems();
	
	@Transactional(readOnly=true)
	@Cacheable("accountReceivableDurationItem")
	AccountReceivableDurationItemDetail getAccountReceivableDurationItem(Integer id);
	
	@Transactional(readOnly=true)
	GetAccountReceivableDurationItemResponse getAccountReceivableDurationItems();
	
	@Transactional(readOnly=true)
	@Cacheable("employeeFeeItem")
	EmployeeFeeItemDetail getEmployeeFeeItem(Integer id);
	
	@Transactional(readOnly=true)
	GetEmployeeFeeItemResponse getEmployeeFeeItems();
	
	@Transactional(readOnly=true)
	@Cacheable("employeeFeeSummaryItem")
	EmployeeFeeSummaryItemDetail getEmployeeFeeSummaryItem(Integer id);
	
	@Transactional(readOnly=true)
	GetEmployeeFeeSummaryItemResponse getEmployeeFeeSummaryItems();
	
	@Transactional(readOnly=true)
	@Cacheable("jobPosition")
	HumanResourceAllocationItemDetail getHumanResourceAllocationItem(Integer id);
	
	@Transactional(readOnly=true)
	GetHumanResourceAllocationItemResponse getHumanResourceAllocationItems();
	
	@Transactional(readOnly=true)
	@Cacheable("duration")
	DurationDetail getDuration(Integer id);
	
	@Transactional(readOnly=true)
	@Cacheable("enumValueInt")
	Option<EnumValue> getEnumValue(String enumType, Integer enumValue);
	
	@Transactional(readOnly=true)
	@Cacheable("enumValueStr")
	Option<EnumValue> getEnumValue(String enumType, String enumValue);
}
