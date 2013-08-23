package com.jdc.themis.dealer.data.dao.hibernate;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.jdc.themis.dealer.data.dao.RefDataDAO;
import com.jdc.themis.dealer.domain.Menu;
import com.jdc.themis.dealer.domain.MenuHierachy;
import com.jdc.themis.dealer.domain.Vehicle;


/**
 * Hibernate implementation for reference data access layer. 
 * 
 * @author chen386_2000
 *
 * @TODO: add support for ordered menu items.
 */
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

	private enum GetParentIDFunction implements Function<MenuHierachy, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(MenuHierachy hierachy) {
	        return hierachy.getMenuHierachyID().getParentID();
	    }
	}
	private enum GetChildIDFunction implements Function<MenuHierachy, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(MenuHierachy hierachy) {
	        return hierachy.getMenuHierachyID().getChildID();
	    }
	}
	
	@Override
	public Map<Integer, Collection<Integer>> getMenuMapping() {
		final List<MenuHierachy> menuHierachy = getMenuHierachy();
		final Map<Integer, Collection<Integer>> map = Maps.newHashMap();
		final ImmutableListMultimap<Integer, MenuHierachy> parentIDToMenuHierachy = Multimaps.index(menuHierachy, GetParentIDFunction.INSTANCE);
		
		for (final Integer key : parentIDToMenuHierachy.keySet()) {
			map.put(key, Collections2.transform(parentIDToMenuHierachy.get(key), GetChildIDFunction.INSTANCE));
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
		final Map<Integer, Collection<Integer>> map = getMenuMapping();
		for (Integer i : map.keySet()) {
			if ( map.get(i).contains(id) ) {
				return i;
			}
		}
		return null;
	}

	@Override
	public List<Vehicle> getVehicleList() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Vehicle> list = session.createCriteria(Vehicle.class).list();
		return list;
	}

}
