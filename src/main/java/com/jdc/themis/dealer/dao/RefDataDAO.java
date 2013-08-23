package com.jdc.themis.dealer.dao;

import java.util.List;
import java.util.Map;

import com.jdc.themis.dealer.domain.Menu;
import com.jdc.themis.dealer.domain.MenuHierachy;

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
	
	List<MenuHierachy> getMenuHierachy();
	
	Map<Integer, List<Integer>> getMenuMapping();
	
}
