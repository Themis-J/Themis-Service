package com.jdc.themis.dealer.data.dao;

import java.util.Collection;
import java.util.List;
import com.jdc.themis.dealer.domain.Dealer;
import com.jdc.themis.dealer.domain.Menu;
import com.jdc.themis.dealer.domain.MenuHierachy;
import com.jdc.themis.dealer.domain.SalesServiceJournalCategory;
import com.jdc.themis.dealer.domain.SalesServiceJournalItem;
import com.jdc.themis.dealer.domain.TaxJournalItem;
import com.jdc.themis.dealer.domain.Vehicle;

/**
 * Data access layer to query static reference data like menus. 
 * 
 * @author chen386_2000
 *
 */
public interface RefDataDAO {

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
}
