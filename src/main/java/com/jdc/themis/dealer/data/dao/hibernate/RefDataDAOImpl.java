package com.jdc.themis.dealer.data.dao.hibernate;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.jdc.themis.dealer.data.dao.RefDataDAO;
import com.jdc.themis.dealer.domain.AccountReceivableDurationItem;
import com.jdc.themis.dealer.domain.Dealer;
import com.jdc.themis.dealer.domain.Department;
import com.jdc.themis.dealer.domain.Duration;
import com.jdc.themis.dealer.domain.EmployeeFeeItem;
import com.jdc.themis.dealer.domain.EmployeeFeeSummaryItem;
import com.jdc.themis.dealer.domain.EnumType;
import com.jdc.themis.dealer.domain.EnumValue;
import com.jdc.themis.dealer.domain.GeneralJournalItem;
import com.jdc.themis.dealer.domain.InventoryDurationItem;
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
	private final static Logger logger = LoggerFactory
			.getLogger(RefDataDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public List<Menu> getMenus() {
		logger.info("Fetching menu list");
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<Menu> list = session.createCriteria(Menu.class).list();
		return ImmutableList.copyOf(list);
	}

	@Override
	public List<MenuHierachy> getMenuHierachys() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<MenuHierachy> list = session.createCriteria(
				MenuHierachy.class).list();
		return ImmutableList.copyOf(list);
	}

	private enum GetParentIDFunction implements Function<MenuHierachy, Integer> {
		INSTANCE;

		@Override
		public Integer apply(MenuHierachy hierachy) {
			return hierachy.getMenuHierachyID().getParentID();
		}
	}

	@Override
	public Menu getMenu(Integer id) {
		final Session session = sessionFactory.getCurrentSession();
		final Menu menu = (Menu) session.load(Menu.class, id);
		return menu;
	}

	@Override
	public Integer getParentMenuID(final Integer id) {
		// Kai: try out functional java here...
		final Collection<MenuHierachy> list = fj.data.List.iterableList(getMenuHierachys()).filter(new fj.F<MenuHierachy, Boolean>() {

			@Override
			public Boolean f(MenuHierachy a) {
				return ((MenuHierachy) a).getMenuHierachyID().getChildID().equals(id);
			}
			
		}).toCollection();
		if ( list.size() == 0 ) {
			return null;
		}
		return list.iterator().next().getMenuHierachyID().getParentID();
	}

	@Override
	public List<Vehicle> getVehicles() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<Vehicle> list = session.createCriteria(Vehicle.class).list();
		return ImmutableList.copyOf(list);
	}

	@Override
	public List<Dealer> getDealers() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<Dealer> list = session.createCriteria(Dealer.class).list();
		return ImmutableList.copyOf(list);
	}

	@Override
	public List<TaxJournalItem> getTaxJournalItems() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<TaxJournalItem> list = session.createCriteria(
				TaxJournalItem.class).list();
		return ImmutableList.copyOf(list);
	}

	@Override
	public List<SalesServiceJournalItem> getSalesServiceJournalItems() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<SalesServiceJournalItem> list = session.createCriteria(
				SalesServiceJournalItem.class).list();
		return ImmutableList.copyOf(list);
	}

	@Override
	public List<SalesServiceJournalCategory> getSalesServiceJournalCategorys() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<SalesServiceJournalCategory> list = session.createCriteria(
				SalesServiceJournalCategory.class).list();
		return ImmutableList.copyOf(list);
	}

	/*
	 * parent to child menu mapping
	 */
	private Collection<MenuHierachy> getParentMenuMapping(Integer id) {
		final List<MenuHierachy> menuHierachy = getMenuHierachys();
		final ImmutableListMultimap<Integer, MenuHierachy> parentIDToMenuHierachy = Multimaps
				.index(menuHierachy, GetParentIDFunction.INSTANCE);
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
		final List<EnumType> list = session.createCriteria(EnumType.class)
				.list();
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
		final List<EnumValue> list = session.createCriteria(EnumValue.class)
				.list();
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
		final Map<String, EnumType> enumTypes = Maps.uniqueIndex(
				getEnumTypes(), GetEnumTypeNameFunction.INSTANCE);
		final EnumType type = enumTypes.get(enumType);
		final ImmutableListMultimap<Integer, EnumValue> values = Multimaps
				.index(getEnumValues(), GetEnumTypeIDFunction.INSTANCE);
		for (final EnumValue ev : values.asMap().get(type.getId())) {
			if (ev.getValue().equals(enumValue)) {
				return ev;
			}
		}
		return null;
	}

	@Override
	@Performance
	public EnumValue getEnumValue(String enumType, String enumValue) {
		final Map<String, EnumType> enumTypes = Maps.uniqueIndex(
				getEnumTypes(), GetEnumTypeNameFunction.INSTANCE);
		final EnumType type = enumTypes.get(enumType);
		final ImmutableListMultimap<Integer, EnumValue> values = Multimaps
				.index(getEnumValues(), GetEnumTypeIDFunction.INSTANCE);
		for (final EnumValue ev : values.asMap().get(type.getId())) {
			if (ev.getName().equals(enumValue)) {
				return ev;
			}
		}
		return null;
	}

	@Override
	public List<GeneralJournalItem> getGeneralJournalItems() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<GeneralJournalItem> list = session.createCriteria(
				GeneralJournalItem.class).list();
		return ImmutableList.copyOf(list);
	}

	@Override
	public List<JobPosition> getJobPositions() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<JobPosition> list = session
				.createCriteria(JobPosition.class).list();
		return ImmutableList.copyOf(list);
	}

	@Override
	public List<AccountReceivableDurationItem> getAccountReceivableDurationItems() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<AccountReceivableDurationItem> list = session
				.createCriteria(AccountReceivableDurationItem.class).list();
		return ImmutableList.copyOf(list);
	}

	@Override
	public List<Duration> getDurations() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<Duration> list = session.createCriteria(Duration.class)
				.list();
		return ImmutableList.copyOf(list);
	}

	@Override
	public List<EmployeeFeeItem> getEmployeeFeeItems() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<EmployeeFeeItem> list = session.createCriteria(
				EmployeeFeeItem.class).list();
		return ImmutableList.copyOf(list);
	}

	@Override
	public List<EmployeeFeeSummaryItem> getEmployeeFeeSummaryItems() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<EmployeeFeeSummaryItem> list = session.createCriteria(
				EmployeeFeeSummaryItem.class).list();
		return ImmutableList.copyOf(list);
	}
	private enum GetVehicleIDFunction implements Function<Vehicle, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(Vehicle item) {
	        return item.getId();
	    }
	}
	@Override
	public Vehicle getVehicle(Integer id) {
		return Maps.uniqueIndex(getVehicles(), GetVehicleIDFunction.INSTANCE).get(id);
	}
	private enum GetSalesServiceIDFunction implements Function<SalesServiceJournalItem, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(SalesServiceJournalItem item) {
	        return item.getId();
	    }
	}
	
	@Override
	public SalesServiceJournalItem getSalesServiceJournalItem(Integer id) {
		return Maps.uniqueIndex(getSalesServiceJournalItems(), GetSalesServiceIDFunction.INSTANCE).get(id);
	}

	private enum GetCategoryIDFunction implements Function<SalesServiceJournalCategory, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(SalesServiceJournalCategory item) {
	        return item.getId();
	    }
	}
	
	@Override
	public SalesServiceJournalCategory getSalesServiceJournalCategory(Integer id) {
		return Maps.uniqueIndex(getSalesServiceJournalCategorys(), GetCategoryIDFunction.INSTANCE).get(id);
	}

	private enum GetDealerIDFunction implements Function<Dealer, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(Dealer item) {
	        return item.getId();
	    }
	}
	@Override
	public Dealer getDealer(Integer dealerID) {
		return Maps.uniqueIndex(getDealers(), GetDealerIDFunction.INSTANCE).get(dealerID);
	}

	@Override
	public List<Department> getDepartments() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<Department> list = session.createCriteria(
				Department.class).list();
		return ImmutableList.copyOf(list);
	}

	private enum GetDepartmentIDFunction implements Function<Department, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(Department item) {
	        return item.getId();
	    }
	}
	@Override
	public Department getDepartment(Integer departmentID) {
		return Maps.uniqueIndex(getDepartments(), GetDepartmentIDFunction.INSTANCE).get(departmentID);
	}

	private enum GetJobPositionIDFunction implements Function<JobPosition, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(JobPosition item) {
	        return item.getId();
	    }
	}
	@Override
	public JobPosition getJobPosition(Integer positionID) {
		return Maps.uniqueIndex(getJobPositions(), GetJobPositionIDFunction.INSTANCE).get(positionID);
	}

	private enum GetAccountReceivableDurationItemIDFunction implements Function<AccountReceivableDurationItem, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(AccountReceivableDurationItem item) {
	        return item.getId();
	    }
	}
	@Override
	public AccountReceivableDurationItem getAccountReceivableDurationItem(Integer itemID) {
		return Maps.uniqueIndex(getAccountReceivableDurationItems(), GetAccountReceivableDurationItemIDFunction.INSTANCE).get(itemID);
	}

	private enum GetDurationIDFunction implements Function<Duration, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(Duration item) {
	        return item.getId();
	    }
	}
	@Override
	public Duration getDuration(Integer durationID) {
		return Maps.uniqueIndex(getDurations(), GetDurationIDFunction.INSTANCE).get(durationID);
	}
	private enum GetEmployeeFeeItemIDFunction implements Function<EmployeeFeeItem, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(EmployeeFeeItem item) {
	        return item.getId();
	    }
	}
	@Override
	public EmployeeFeeItem getEmployeeFeeItem(Integer itemID) {
		return Maps.uniqueIndex(getEmployeeFeeItems(), GetEmployeeFeeItemIDFunction.INSTANCE).get(itemID);
	}
	private enum GetEmployeeFeeSummaryItemIDFunction implements Function<EmployeeFeeSummaryItem, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(EmployeeFeeSummaryItem item) {
	        return item.getId();
	    }
	}
	@Override
	public EmployeeFeeSummaryItem getEmployeeFeeSummaryItem(Integer itemID) {
		return Maps.uniqueIndex(getEmployeeFeeSummaryItems(), GetEmployeeFeeSummaryItemIDFunction.INSTANCE).get(itemID);
	}

	@Override
	public List<InventoryDurationItem> getInventoryDurationItems() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<InventoryDurationItem> list = session.createCriteria(
				InventoryDurationItem.class).list();
		return ImmutableList.copyOf(list);
	}

	private enum GetInventoryDurationItemIDFunction implements Function<InventoryDurationItem, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(InventoryDurationItem item) {
	        return item.getId();
	    }
	}
	@Override
	public InventoryDurationItem getInventoryDurationItem(Integer itemID) {
		return Maps.uniqueIndex(getInventoryDurationItems(), GetInventoryDurationItemIDFunction.INSTANCE).get(itemID);
	}

	private enum GetGeneralJournalItemIDFunction implements Function<GeneralJournalItem, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(GeneralJournalItem item) {
	        return item.getId();
	    }
	}
	@Override
	public GeneralJournalItem getGeneralJournalItem(Integer id) {
		return Maps.uniqueIndex(getGeneralJournalItems(), GetGeneralJournalItemIDFunction.INSTANCE).get(id);
	}

}
