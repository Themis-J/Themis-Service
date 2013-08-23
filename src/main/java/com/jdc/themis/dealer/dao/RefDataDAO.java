package com.jdc.themis.dealer.dao;

import java.util.List;
import java.util.Map;

import com.jdc.themis.dealer.domain.Menu;
import com.jdc.themis.dealer.domain.MenuHierachy;

public interface RefDataDAO {

	List<Menu> getMenuList();
	
	Menu getMenu(Integer id);
	
	Integer getParentMenuID(Integer id);
	
	List<MenuHierachy> getMenuHierachy();
	
	Map<Integer, List<Integer>> getMenuMapping();
	
}
