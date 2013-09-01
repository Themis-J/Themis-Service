package com.jdc.themis.dealer.data.dao;

import java.util.Collection;
import java.util.List;
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

	EnumValue getEnumValue(String enumType, Integer enumValue);
	
	EnumValue getEnumValue(String enumType, String enumValue);
	
	List<EnumType> getEnumTypes();
	
	List<EnumValue> getEnumValues();
	
	List<Menu> getMenus();
	
	Menu getMenu(Integer id);
	
	Integer getParentMenuID(Integer id);
	
	Collection<MenuHierachy> getChildMenus(Integer id);
	
	List<MenuHierachy> getMenuHierachys();
	
	List<Vehicle> getVehicles();
	
	Vehicle getVehicle(Integer id);
	
	List<Dealer> getDealers();
	
	Dealer getDealer(Integer dealerID);
	
	List<Department> getDepartments();
	
	Department getDepartment(Integer departmentID);
	
	List<TaxJournalItem> getTaxJournalItems();

	List<SalesServiceJournalItem> getSalesServiceJournalItems();
	
	SalesServiceJournalItem getSalesServiceJournalItem(Integer id);
	
	List<SalesServiceJournalCategory> getSalesServiceJournalCategorys();
	
	SalesServiceJournalCategory getSalesServiceJournalCategory(Integer id);
	
	List<GeneralJournalItem> getGeneralJournalItems();
	
	GeneralJournalItem getGeneralJournalItem(Integer id);
	
	List<JobPosition> getJobPositions();
	
	JobPosition getJobPosition(Integer positionID);
	
	List<AccountReceivableDurationItem> getAccountReceivableDurationItems();
	
	InventoryDurationItem getInventoryDurationItem(Integer itemID);
	
	List<InventoryDurationItem> getInventoryDurationItems();
	
	AccountReceivableDurationItem getAccountReceivableDurationItem(Integer itemID);
	
	List<Duration> getDurations();
	
	Duration getDuration(Integer durationID);
	
	List<EmployeeFeeItem> getEmployeeFeeItems();
	
	EmployeeFeeItem getEmployeeFeeItem(Integer itemID);
	
	List<EmployeeFeeSummaryItem> getEmployeeFeeSummaryItems();
	
	EmployeeFeeSummaryItem getEmployeeFeeSummaryItem(Integer itemID);
	
}
