package com.jdc.themis.dealer.data.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.jdc.themis.dealer.domain.AccountReceivableDurationItem;
import com.jdc.themis.dealer.domain.Dealer;
import com.jdc.themis.dealer.domain.Department;
import com.jdc.themis.dealer.domain.Duration;
import com.jdc.themis.dealer.domain.EmployeeFeeItem;
import com.jdc.themis.dealer.domain.EmployeeFeeSummaryItem;
import com.jdc.themis.dealer.domain.EnumType;
import com.jdc.themis.dealer.domain.EnumValue;
import com.jdc.themis.dealer.domain.GeneralJournalItem;
import com.jdc.themis.dealer.domain.InventoryDurationItem;
import com.jdc.themis.dealer.domain.JobPosition;
import com.jdc.themis.dealer.domain.Menu;
import com.jdc.themis.dealer.domain.MenuHierachy;
import com.jdc.themis.dealer.domain.SalesServiceJournalCategory;
import com.jdc.themis.dealer.domain.SalesServiceJournalItem;
import com.jdc.themis.dealer.domain.TaxJournalItem;
import com.jdc.themis.dealer.domain.Vehicle;

/**
 * Data access layer to query static reference data like menus. 
 * 
 * @author Kai Chen
 *
 */
public interface RefDataDAO {

	@Cacheable(value="enumValue")
	EnumValue getEnumValue(String enumType, Integer enumValue);
	
	List<EnumType> getEnumTypes();
	
	List<EnumValue> getEnumValues();
	
	List<Menu> getMenus();
	
	@Cacheable(value="menu")
	Menu getMenu(Integer id);
	
	@Cacheable(value="parentMenu")
	Integer getParentMenuID(Integer id);
	
	@Cacheable(value="childMenus")
	Collection<MenuHierachy> getChildMenus(Integer id);
	
	List<MenuHierachy> getMenuHierachys();
	
	List<Vehicle> getVehicles();
	
	@Cacheable(value="vehicle")
	Vehicle getVehicle(Integer id);
	
	List<Dealer> getDealers();
	
	@Cacheable(value="dealer")
	Dealer getDealer(Integer dealerID);
	
	List<Department> getDepartments();
	
	@Cacheable(value="department")
	Department getDepartment(Integer departmentID);
	
	List<TaxJournalItem> getTaxJournalItems();

	List<SalesServiceJournalItem> getSalesServiceJournalItems();
	
	@Cacheable(value="salesServiceJournalItem")
	SalesServiceJournalItem getSalesServiceJournalItem(Integer id);
	
	List<SalesServiceJournalCategory> getSalesServiceJournalCategorys();
	
	@Cacheable(value="salesServiceJournalCategory")
	SalesServiceJournalCategory getSalesServiceJournalCategory(Integer id);
	
	List<GeneralJournalItem> getGeneralJournalItems();
	
	List<JobPosition> getJobPositions();
	
	@Cacheable(value="jobPosition")
	JobPosition getJobPosition(Integer positionID);
	
	List<AccountReceivableDurationItem> getAccountReceivableDurationItems();
	
	@Cacheable(value="inventoryDurationItem")
	InventoryDurationItem getInventoryDurationItem(Integer itemID);
	
	List<InventoryDurationItem> getInventoryDurationItems();
	
	@Cacheable(value="accountReceivableDurationItem")
	AccountReceivableDurationItem getAccountReceivableItem(Integer itemID);
	
	List<Duration> getDurations();
	
	@Cacheable(value="duration")
	Duration getDuration(Integer durationID);
	
	List<EmployeeFeeItem> getEmployeeFeeItems();
	
	@Cacheable(value="employeeFeeItem")
	EmployeeFeeItem getEmployeeFeeItem(Integer itemID);
	
	List<EmployeeFeeSummaryItem> getEmployeeFeeSummaryItems();
	
	@Cacheable(value="employeeFeeSummarysItem")
	EmployeeFeeSummaryItem getEmployeeFeeSummaryItem(Integer itemID);
}
