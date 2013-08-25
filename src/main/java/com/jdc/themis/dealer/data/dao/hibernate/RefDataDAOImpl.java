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
import com.jdc.themis.dealer.utils.Performance;


/**
 * Hibernate implementation for reference data access layer. 
 * 
 * @author Kai Chen
 *
 */
@Service
public class RefDataDAOImpl implements RefDataDAO {
	private final static Logger logger = LoggerFactory.getLogger(RefDataDAOImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private List<Menu> cachedMenuList;
	
	@Override
	public synchronized List<Menu> getMenuList() {
		logger.info("Fetching menu list");
		if ( cachedMenuList == null ) {
			final Session session = sessionFactory.getCurrentSession();
			@SuppressWarnings("unchecked")
			final List<Menu> list = session.createCriteria(Menu.class).list();
			cachedMenuList = ImmutableList.copyOf(list);
		} 
		return cachedMenuList;
	}

	private List<MenuHierachy> cachedMenuHierachyList;
	@Override
	public synchronized List<MenuHierachy> getMenuHierachy() {
		final Session session = sessionFactory.getCurrentSession();
		if ( cachedMenuHierachyList == null ) {
			@SuppressWarnings("unchecked")
			final List<MenuHierachy> list = session.createCriteria(MenuHierachy.class).list();
			cachedMenuHierachyList = ImmutableList.copyOf(list);
		}
		return cachedMenuHierachyList;
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
	
	@Override
	public List<SalesServiceJournalCategory> getSalesServiceJournalCategoryList() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<SalesServiceJournalCategory> list = session.createCriteria(SalesServiceJournalCategory.class).list();
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

	@Override
	public List<EnumType> getEnumTypes() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<EnumType> list = session.createCriteria(EnumType.class).list();
		return ImmutableList.copyOf(list);
	}

	private enum GetEnumTypeIDFunction implements Function<EnumValue, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(EnumValue value) {
	        return value.getTypeID();
	    }
	}
	@Override
	public List<EnumValue> getEnumValues() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<EnumValue> list = session.createCriteria(EnumValue.class).list();
		return ImmutableList.copyOf(list);
	}

	private enum GetEnumTypeNameFunction implements Function<EnumType, String> {
	    INSTANCE;

	    @Override
	    public String apply(EnumType type) {
	        return type.getName();
	    }
	}
	@Override
	@Performance
	public EnumValue getEnumValue(String enumType, Integer enumValue) {
		final Map<String, EnumType> enumTypes = Maps.uniqueIndex(getEnumTypes(), GetEnumTypeNameFunction.INSTANCE);
		final EnumType type = enumTypes.get(enumType);
		final ImmutableListMultimap<Integer, EnumValue> values = Multimaps.index(getEnumValues(), GetEnumTypeIDFunction.INSTANCE);
		for (final EnumValue ev : values.asMap().get(type.getId())) {
			if ( ev.getValue().equals(enumValue) ) {
				return ev;
			}
		}
		return null;
	}

	@Override
	public List<GeneralJournalItem> getGeneralJournalItemList() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<GeneralJournalItem> list = session.createCriteria(GeneralJournalItem.class).list();
		return ImmutableList.copyOf(list);
	}

	@Override
	public List<JobPosition> getJobPositionList() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<JobPosition> list = session.createCriteria(JobPosition.class).list();
		return ImmutableList.copyOf(list);
	}

	@Override
	public List<AccountReceivableDurationItem> getAccountReceivableItemList() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<AccountReceivableDurationItem> list = session.createCriteria(AccountReceivableDurationItem.class).list();
		return ImmutableList.copyOf(list);
	}

	@Override
	public List<Duration> getDurationList() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<Duration> list = session.createCriteria(Duration.class).list();
		return ImmutableList.copyOf(list);
	}

	@Override
	public List<EmployeeFeeItem> getEmployeeFeeItemList() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<EmployeeFeeItem> list = session.createCriteria(EmployeeFeeItem.class).list();
		return ImmutableList.copyOf(list);
	}

	@Override
	public List<EmployeeFeeSummaryItem> getEmployeeFeeSummaryItemList() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<EmployeeFeeSummaryItem> list = session.createCriteria(EmployeeFeeSummaryItem.class).list();
		return ImmutableList.copyOf(list);
	}

}
