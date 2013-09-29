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
import com.jdc.themis.dealer.domain.GeneralJournalCategory;
import com.jdc.themis.dealer.domain.GeneralJournalItem;
import com.jdc.themis.dealer.domain.InventoryDurationItem;
import com.jdc.themis.dealer.domain.JobPosition;
import com.jdc.themis.dealer.domain.Menu;
import com.jdc.themis.dealer.domain.MenuHierachy;
import com.jdc.themis.dealer.domain.SalesServiceJournalCategory;
import com.jdc.themis.dealer.domain.SalesServiceJournalItem;
import com.jdc.themis.dealer.domain.TaxJournalItem;
import com.jdc.themis.dealer.domain.Vehicle;

import fj.data.Option;

/**
 * Data access layer to query static reference data like menus. 
 * 
 * @author Kai Chen
 *
 */
public interface RefDataDAO {

	Option<EnumValue> getEnumValue(String enumType, Integer enumValue);
	
	Option<EnumValue> getEnumValue(String enumType, String enumValue);
	
	List<EnumType> getEnumTypes();
	
	List<EnumValue> getEnumValues();
	
	List<Menu> getMenus();
	
	Menu getMenu(Integer id);
	
	Integer getParentMenuID(Integer id);
	
	Collection<MenuHierachy> getChildMenus(Integer id);
	
	List<MenuHierachy> getMenuHierachys();
	
	List<Vehicle> getVehicles(Option<Integer> categoryID);
	
	Option<Vehicle> getVehicle(Integer id);
	
	List<Dealer> getDealers();
	
	Option<Dealer> getDealer(Integer dealerID);
	
	List<Department> getDepartments();
	
	Option<Department> getDepartment(Integer departmentID);
	
	List<TaxJournalItem> getTaxJournalItems();

	List<SalesServiceJournalItem> getSalesServiceJournalItems(Option<Integer> categoryID);
	
	Option<SalesServiceJournalItem> getSalesServiceJournalItem(Integer id);
	
	Option<SalesServiceJournalItem> getSalesServiceJournalItem(String name, Integer categoryID);
	
	List<SalesServiceJournalCategory> getSalesServiceJournalCategorys();
	
	Option<SalesServiceJournalCategory> getSalesServiceJournalCategory(Integer id);
	
	Option<SalesServiceJournalCategory> getSalesServiceJournalCategory(String name);
	
	Option<GeneralJournalCategory> getGeneralJournalCategory(Integer id);
	
	List<GeneralJournalCategory> getGeneralJournalCategorys();
	
	List<GeneralJournalItem> getGeneralJournalItems(Option<Integer> categoryID);
	
	Option<GeneralJournalItem> getGeneralJournalItem(Integer id);
	
	List<JobPosition> getJobPositions();
	
	Option<JobPosition> getJobPosition(Integer positionID);
	
	List<AccountReceivableDurationItem> getAccountReceivableDurationItems();
	
	Option<InventoryDurationItem> getInventoryDurationItem(Integer itemID);
	
	List<InventoryDurationItem> getInventoryDurationItems();
	
	Option<AccountReceivableDurationItem> getAccountReceivableDurationItem(Integer itemID);
	
	List<Duration> getDurations();
	
	Option<Duration> getDuration(Integer durationID);
	
	List<EmployeeFeeItem> getEmployeeFeeItems();
	
	Option<EmployeeFeeItem> getEmployeeFeeItem(Integer itemID);
	
	Option<TaxJournalItem> getTaxJournalItem(Integer itemID);
	
	List<EmployeeFeeSummaryItem> getEmployeeFeeSummaryItems();
	
	Option<EmployeeFeeSummaryItem> getEmployeeFeeSummaryItem(Integer itemID);
	
}
