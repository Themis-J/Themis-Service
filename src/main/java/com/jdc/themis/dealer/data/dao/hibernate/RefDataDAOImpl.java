package com.jdc.themis.dealer.data.dao.hibernate;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
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
import com.jdc.themis.dealer.domain.GeneralJournalCategory;
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

import fj.P1;
import fj.data.Option;

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
	public List<Vehicle> getVehicles(Option<Integer> categoryID) {
		final Session session = sessionFactory.getCurrentSession();
		if ( categoryID.isSome() ) {
			session.enableFilter(Vehicle.FILTER).setParameter("categoryID", categoryID.some());
		}
		@SuppressWarnings("unchecked")
		final List<Vehicle> list = session.createCriteria(Vehicle.class).list();
		if ( categoryID.isSome() ) {
			session.disableFilter(Vehicle.FILTER);
		}
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
	public List<SalesServiceJournalItem> getSalesServiceJournalItems(Option<Integer> categoryID) {
		final Session session = sessionFactory.getCurrentSession();
		if ( categoryID.isSome() ) {
			session.enableFilter(SalesServiceJournalItem.FILTER).setParameter("categoryID", categoryID.some());
		}
		@SuppressWarnings("unchecked")
		final List<SalesServiceJournalItem> list = session.createCriteria(
				SalesServiceJournalItem.class).list();
		if ( categoryID.isSome() ) {
			session.disableFilter(SalesServiceJournalItem.FILTER);
		}
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
	public Option<EnumValue> getEnumValue(String enumType, Integer enumValue) {
		final Map<String, EnumType> enumTypes = Maps.uniqueIndex(
				getEnumTypes(), GetEnumTypeNameFunction.INSTANCE);
		final EnumType type = enumTypes.get(enumType);
		final ImmutableListMultimap<Integer, EnumValue> values = Multimaps
				.index(getEnumValues(), GetEnumTypeIDFunction.INSTANCE);
		for (final EnumValue ev : values.asMap().get(type.getId())) {
			if (ev.getValue().equals(enumValue)) {
				return Option.<EnumValue>some(ev);
			}
		}
		return Option.<EnumValue>none();
	}

	@Override
	@Performance
	public Option<EnumValue> getEnumValue(String enumType, String enumValue) {
		final Map<String, EnumType> enumTypes = Maps.uniqueIndex(
				getEnumTypes(), GetEnumTypeNameFunction.INSTANCE);
		final EnumType type = enumTypes.get(enumType);
		final ImmutableListMultimap<Integer, EnumValue> values = Multimaps
				.index(getEnumValues(), GetEnumTypeIDFunction.INSTANCE);
		for (final EnumValue ev : values.asMap().get(type.getId())) {
			if (ev.getName().equals(enumValue)) {
				return Option.<EnumValue>some(ev);
			}
		}
		return Option.<EnumValue>none();
	}

	@Override
	public List<GeneralJournalItem> getGeneralJournalItems(Option<Integer> categoryID) {
		final Session session = sessionFactory.getCurrentSession();
		
		if ( categoryID.isSome() ) {
			session.enableFilter(GeneralJournalItem.FILTER).setParameter("categoryID", categoryID.some());
		}
		@SuppressWarnings("unchecked")
		final List<GeneralJournalItem> list = session.createCriteria(
				GeneralJournalItem.class).list();
		if ( categoryID.isSome() ) {
			session.disableFilter(GeneralJournalItem.FILTER);
		}
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
	public Option<Vehicle> getVehicle(Integer id) {
		final Map<Integer, Vehicle> map = Maps.uniqueIndex(getVehicles(Option.<Integer>none()), GetVehicleIDFunction.INSTANCE);
		if ( !map.containsKey(id) ) {
			return Option.<Vehicle>none();
		}
		return Option.<Vehicle>some(map.get(id));
	}
	private enum GetSalesServiceIDFunction implements Function<SalesServiceJournalItem, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(SalesServiceJournalItem item) {
	        return item.getId();
	    }
	}
	
	@Override
	public Option<SalesServiceJournalItem> getSalesServiceJournalItem(Integer id) {
		final Map<Integer, SalesServiceJournalItem> map = Maps.uniqueIndex(getSalesServiceJournalItems(Option.<Integer>none()), GetSalesServiceIDFunction.INSTANCE);
		if ( !map.containsKey(id) ) {
			return Option.<SalesServiceJournalItem>none();
		}
		return Option.<SalesServiceJournalItem>some(map.get(id));
	}
	
	@Override
	public Option<SalesServiceJournalItem> getSalesServiceJournalItem(final String name, final Integer categoryID) {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<SalesServiceJournalItem> list = session.createCriteria(
				SalesServiceJournalItem.class)
				.add(Restrictions.eq("name", name))
				.add(Restrictions.eq("categoryID", categoryID))
				.list();

		return Option.<SalesServiceJournalItem>iif(!list.isEmpty(), new P1<SalesServiceJournalItem>() {

			@Override
			public SalesServiceJournalItem _1() {
				return list.get(0);
			}
			
		});
	}

	private enum GetCategoryIDFunction implements Function<SalesServiceJournalCategory, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(SalesServiceJournalCategory item) {
	        return item.getId();
	    }
	}
	
	@Override
	public Option<SalesServiceJournalCategory> getSalesServiceJournalCategory(final Integer id) {
		final Map<Integer, SalesServiceJournalCategory> map = Maps.uniqueIndex(getSalesServiceJournalCategorys(), GetCategoryIDFunction.INSTANCE);
		if ( !map.containsKey(id) ) {
			return Option.<SalesServiceJournalCategory>none();
		}
		return Option.<SalesServiceJournalCategory>some(map.get(id));
	}
	
	@Override
	public Option<SalesServiceJournalCategory> getSalesServiceJournalCategory(final String name) {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<SalesServiceJournalCategory> list = session.createCriteria(
				SalesServiceJournalCategory.class)
				.add(Restrictions.eq("name", name))
				.list();

		return Option.<SalesServiceJournalCategory>iif(!list.isEmpty(), new P1<SalesServiceJournalCategory>() {

			@Override
			public SalesServiceJournalCategory _1() {
				return list.get(0);
			}
			
		});
	}

	private enum GetDealerIDFunction implements Function<Dealer, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(Dealer item) {
	        return item.getId();
	    }
	}
	@Override
	public Option<Dealer> getDealer(final Integer dealerID) {
		final Dealer dealer = Maps.uniqueIndex(getDealers(), GetDealerIDFunction.INSTANCE).get(dealerID);
		return Option.<Dealer>fromNull(dealer);
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
	public Option<Department> getDepartment(Integer departmentID) {
		final Department department = Maps.uniqueIndex(getDepartments(), GetDepartmentIDFunction.INSTANCE).get(departmentID);
		return Option.<Department>fromNull(department);
	}

	private enum GetJobPositionIDFunction implements Function<JobPosition, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(JobPosition item) {
	        return item.getId();
	    }
	}
	@Override
	public Option<JobPosition> getJobPosition(Integer positionID) {
		final Map<Integer, JobPosition> map = Maps.uniqueIndex(getJobPositions(), GetJobPositionIDFunction.INSTANCE);
		return Option.<JobPosition>iif(map.containsKey(positionID), map.get(positionID));
	}

	private enum GetAccountReceivableDurationItemIDFunction implements Function<AccountReceivableDurationItem, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(AccountReceivableDurationItem item) {
	        return item.getId();
	    }
	}
	@Override
	public Option<AccountReceivableDurationItem> getAccountReceivableDurationItem(Integer itemID) {
		final Map<Integer, AccountReceivableDurationItem> map = 
				Maps.uniqueIndex(getAccountReceivableDurationItems(), GetAccountReceivableDurationItemIDFunction.INSTANCE);
		return Option.<AccountReceivableDurationItem>iif(map.containsKey(itemID), map.get(itemID));
	}

	private enum GetDurationIDFunction implements Function<Duration, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(Duration item) {
	        return item.getId();
	    }
	}
	@Override
	public Option<Duration> getDuration(Integer durationID) {
		final Map<Integer, Duration> map = Maps.uniqueIndex(getDurations(), GetDurationIDFunction.INSTANCE);
		return Option.<Duration>iif(map.containsKey(durationID), map.get(durationID));
	}
	private enum GetEmployeeFeeItemIDFunction implements Function<EmployeeFeeItem, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(EmployeeFeeItem item) {
	        return item.getId();
	    }
	}
	@Override
	public Option<EmployeeFeeItem> getEmployeeFeeItem(Integer itemID) {
		final Map<Integer, EmployeeFeeItem> map = Maps.uniqueIndex(getEmployeeFeeItems(), GetEmployeeFeeItemIDFunction.INSTANCE);
		return Option.<EmployeeFeeItem>iif(map.containsKey(itemID), map.get(itemID));
	}
	private enum GetEmployeeFeeSummaryItemIDFunction implements Function<EmployeeFeeSummaryItem, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(EmployeeFeeSummaryItem item) {
	        return item.getId();
	    }
	}
	@Override
	public Option<EmployeeFeeSummaryItem> getEmployeeFeeSummaryItem(Integer itemID) {
		final Map<Integer, EmployeeFeeSummaryItem> map = 
				Maps.uniqueIndex(getEmployeeFeeSummaryItems(), GetEmployeeFeeSummaryItemIDFunction.INSTANCE);
		return Option.<EmployeeFeeSummaryItem>iif(map.containsKey(itemID), map.get(itemID));
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
	public Option<InventoryDurationItem> getInventoryDurationItem(Integer itemID) {
		final Map<Integer, InventoryDurationItem> map = Maps.uniqueIndex(getInventoryDurationItems(), GetInventoryDurationItemIDFunction.INSTANCE);
		return Option.<InventoryDurationItem>iif(map.containsKey(itemID), map.get(itemID));
	}

	private enum GetGeneralJournalItemIDFunction implements Function<GeneralJournalItem, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(GeneralJournalItem item) {
	        return item.getId();
	    }
	}
	@Override
	public Option<GeneralJournalItem> getGeneralJournalItem(Integer itemID) {
		final Map<Integer, GeneralJournalItem> map = Maps.uniqueIndex(getGeneralJournalItems(Option.<Integer>none()), GetGeneralJournalItemIDFunction.INSTANCE);
		return Option.<GeneralJournalItem>iif(map.containsKey(itemID), map.get(itemID));
	}
	private enum GetGeneralJournalCategoryIDFunction implements Function<GeneralJournalCategory, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(GeneralJournalCategory item) {
	        return item.getId();
	    }
	}
	@Override
	public Option<GeneralJournalCategory> getGeneralJournalCategory(Integer itemID) {
		final Map<Integer, GeneralJournalCategory> map = Maps.uniqueIndex(getGeneralJournalCategorys(), GetGeneralJournalCategoryIDFunction.INSTANCE);
		return Option.<GeneralJournalCategory>iif(map.containsKey(itemID), map.get(itemID));
	}

	@Override
	public List<GeneralJournalCategory> getGeneralJournalCategorys() {
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<GeneralJournalCategory> list = session.createCriteria(
				GeneralJournalCategory.class).list();
		return ImmutableList.copyOf(list);
	}
	private enum GetTaxJournalItemIDFunction implements Function<TaxJournalItem, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(TaxJournalItem item) {
	        return item.getId();
	    }
	}
	@Override
	public Option<TaxJournalItem> getTaxJournalItem(Integer itemID) {
		final Map<Integer, TaxJournalItem> map = Maps.uniqueIndex(getTaxJournalItems(), GetTaxJournalItemIDFunction.INSTANCE);
		return Option.<TaxJournalItem>iif(map.containsKey(itemID), map.get(itemID));
	}

}
