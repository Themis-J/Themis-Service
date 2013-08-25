package com.jdc.themis.dealer.data.dao;

import java.util.Collection;
import java.util.List;

import com.jdc.themis.dealer.domain.AccountReceivableDurationItem;
import com.jdc.themis.dealer.domain.Dealer;
import com.jdc.themis.dealer.domain.Duration;
import com.jdc.themis.dealer.domain.EmployeeFeeItem;
import com.jdc.themis.dealer.domain.EmployeeFeeSummaryItem;
import com.jdc.themis.dealer.domain.EnumType;
import com.jdc.themis.dealer.domain.EnumValue;
import com.jdc.themis.dealer.domain.GeneralJournalItem;
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
	
	List<EnumType> getEnumTypes();
	
	List<EnumValue> getEnumValues();
	
	List<Menu> getMenuList();
	
	Menu getMenu(Integer id);
	
	Integer getParentMenuID(Integer id);
	
	Collection<MenuHierachy> getChildMenus(Integer id);
	
	List<MenuHierachy> getMenuHierachy();
	
	List<Vehicle> getVehicleList();
	
	List<Dealer> getDealerList();
	
	List<TaxJournalItem> getTaxJournalItemList();

	List<SalesServiceJournalItem> getSalesServiceJournalItemList();
	
	List<SalesServiceJournalCategory> getSalesServiceJournalCategoryList();
	
	List<GeneralJournalItem> getGeneralJournalItemList();
	
	List<JobPosition> getJobPositionList();
	
	List<AccountReceivableDurationItem> getAccountReceivableItemList();
	
	List<Duration> getDurationList();
	
	List<EmployeeFeeItem> getEmployeeFeeItemList();
	
	List<EmployeeFeeSummaryItem> getEmployeeFeeSummaryItemList();
}
