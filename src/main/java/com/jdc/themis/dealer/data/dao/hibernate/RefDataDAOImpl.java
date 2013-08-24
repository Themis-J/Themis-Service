package com.jdc.themis.dealer.data.dao.hibernate;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.jdc.themis.dealer.data.dao.RefDataDAO;
import com.jdc.themis.dealer.domain.Dealer;
import com.jdc.themis.dealer.domain.Menu;
import com.jdc.themis.dealer.domain.MenuHierachy;
import com.jdc.themis.dealer.domain.SalesServiceJournalItem;
import com.jdc.themis.dealer.domain.TaxJournalItem;
import com.jdc.themis.dealer.domain.Vehicle;


/**
 * Hibernate implementation for reference data access layer. 
 * 
 * @author chen386_2000
 *
 */
@Service
public class RefDataDAOImpl implements RefDataDAO {
	final static Logger logger = LoggerFactory.getLogger(RefDataDAOImpl.class);
	
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
		logger.info("Fetching menu list");
		
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<Menu> list = session.createCriteria(Menu.class).list();
		return ImmutableList.copyOf(list);
	}

	@Override
	public List<MenuHierachy> getMenuHierachy() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<MenuHierachy> list = session.createCriteria(MenuHierachy.class).list();
		return ImmutableList.copyOf(list);
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
	
	/*
	 * child to parent menu mapping
	 */
	private Map<Integer, Integer> getChildMenuMapping() {
		final List<MenuHierachy> menuHierachy = getMenuHierachy();
		final Map<Integer, Integer> map = Maps.newHashMap();
		final ImmutableListMultimap<Integer, MenuHierachy> parentIDToMenuHierachy = Multimaps.index(menuHierachy, GetChildIDFunction.INSTANCE);
		
		for (final Integer key : parentIDToMenuHierachy.keySet()) {
			final Iterator<Integer> parents = Collections2.transform(parentIDToMenuHierachy.get(key), GetParentIDFunction.INSTANCE).iterator();
			if ( parents.hasNext() ) {
				map.put(key, parents.next()); // there must be one child mapped to one parent
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
		return getChildMenuMapping().get(id);
	}

	@Override
	public List<Vehicle> getVehicleList() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<Vehicle> list = session.createCriteria(Vehicle.class).list();
		return ImmutableList.copyOf(list);
	}

	@Override
	public List<Dealer> getDealerList() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<Dealer> list = session.createCriteria(Dealer.class).list();
		return ImmutableList.copyOf(list);
	}

	@Override
	public List<TaxJournalItem> getTaxJournalItemList() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<TaxJournalItem> list = session.createCriteria(TaxJournalItem.class).list();
		return ImmutableList.copyOf(list);
	}

	@Override
	public List<SalesServiceJournalItem> getSalesServiceJournalItemList() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<SalesServiceJournalItem> list = session.createCriteria(SalesServiceJournalItem.class).list();
		return ImmutableList.copyOf(list);
	}

	/*
	 * parent to child menu mapping
	 */
	private Collection<MenuHierachy> getParentMenuMapping(Integer id) {
		final List<MenuHierachy> menuHierachy = getMenuHierachy();
		final ImmutableListMultimap<Integer, MenuHierachy> parentIDToMenuHierachy = Multimaps.index(menuHierachy, GetParentIDFunction.INSTANCE);
		return parentIDToMenuHierachy.asMap().get(id);
	}
	
	@Override
	public Collection<MenuHierachy> getChildMenus(Integer id) {
		return getParentMenuMapping(id);
	}

}
