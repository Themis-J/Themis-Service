package com.jdc.themis.dealer.dao.hibernate;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jdc.themis.dealer.dao.RefDataDAO;
import com.jdc.themis.dealer.domain.Menu;
import com.jdc.themis.dealer.domain.MenuHierachy;


@Service
public class RefDataDAOImpl implements RefDataDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public List<Menu> getMenuList() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Menu> list = session.createCriteria(Menu.class).list();
		return list;
	}

	@Override
	public List<MenuHierachy> getMenuHierachy() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<MenuHierachy> list = session.createCriteria(MenuHierachy.class).list();
		return list;
	}

	@Override
	public Map<Integer, List<Integer>> getMenuMapping() {
		final List<MenuHierachy> menuHierachy = getMenuHierachy();
		final Map<Integer, List<Integer>> map = Maps.newHashMap();
		
		for (final MenuHierachy mh : menuHierachy) {
			if ( map.containsKey(mh.getMenuHierachyID().getParentID()) ) {
				map.get(mh.getMenuHierachyID().getParentID()).add(mh.getMenuHierachyID().getChildID());
			} else {
				final List<Integer> menus = Lists.newArrayList();
				menus.add(mh.getMenuHierachyID().getChildID());
				map.put(mh.getMenuHierachyID().getParentID(), menus);
			}
		}
		
		return map;
	}

	@Override
	public Menu getMenu(Integer id) {
		final Session session = sessionFactory.getCurrentSession();
		final Menu menu = (Menu) session.load(Menu.class, id);
		return menu;
	}

	@Override
	public Integer getParentMenuID(Integer id) {
		final Map<Integer, List<Integer>> map = getMenuMapping();
		for (Integer i : map.keySet()) {
			if ( map.get(i).contains(id) ) {
				return i;
			}
		}
		return null;
	}

}
