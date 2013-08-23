package com.jdc.themis.dealer.data.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jdc.themis.dealer.domain.Menu;
import com.jdc.themis.dealer.domain.MenuHierachy;
import com.jdc.themis.dealer.domain.Vehicle;

/**
 * Data access layer to query static reference data like menus. 
 * 
 * @author chen386_2000
 * 
 * @TODO: finish all other static reference data like journal items.
 *
 */
public interface RefDataDAO {

	List<Menu> getMenuList();
	
	Menu getMenu(Integer id);
	
	Integer getParentMenuID(Integer id);
	
	List<MenuHierachy> getMenuHierachy();
	
	Map<Integer, Collection<Integer>> getMenuMapping();
	
	List<Vehicle> getVehicleList();
	
}
