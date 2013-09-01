package com.jdc.themis.dealer.data.dao.hibernate;

import java.util.Collection;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.jdc.themis.dealer.data.dao.RefDataDAO;
import com.jdc.themis.dealer.data.dao.UserDAO;
import com.jdc.themis.dealer.domain.EntitlementResource;
import com.jdc.themis.dealer.domain.UserInfo;
import com.jdc.themis.dealer.domain.UserRole;
import com.jdc.themis.dealer.domain.UserRoleEntitlement;

@Service
public class UserDAOImpl implements UserDAO {
	private final static Logger logger = LoggerFactory
			.getLogger(UserDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private RefDataDAO refDataDAL;
	
	public RefDataDAO getRefDataDAL() {
		return refDataDAL;
	}

	public void setRefDataDAL(RefDataDAO refDataDAL) {
		this.refDataDAL = refDataDAL;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public List<UserRole> getUserRoles() {
		logger.info("Fetching all user roles");
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<UserRole> list = session.createCriteria(UserRole.class).list();
		return ImmutableList.copyOf(list);
	}

	private enum GetUserRoleIDFunction implements Function<UserRole, Integer> {
		INSTANCE;

		@Override
		public Integer apply(final UserRole item) {
			return item.getId();
		}
	}
	
	@Override
	public UserRole getUserRole(final Integer roleID) {
		return Maps.uniqueIndex(getUserRoles(), GetUserRoleIDFunction.INSTANCE).get(roleID);
	}
	
	private List<EntitlementResource> getEntitlements() {
		logger.info("Fetching all entitlements");
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<EntitlementResource> list = session.createCriteria(EntitlementResource.class).list();
		return ImmutableList.copyOf(list);
	}
	private Iterable<EntitlementResource> getEntitlements(final Collection<UserRoleEntitlement> roleResources) {
		return Iterables.filter(getEntitlements(), new Predicate<EntitlementResource>() {
			  		public boolean apply(final EntitlementResource resource) {
			  			for (UserRoleEntitlement item : roleResources) {
			  				if ( item.getResourceID().equals(resource.getId() )) {
			  					return true;
			  				}
			  			}
			  			return false;
			  		}
				});
	}

	private List<UserRoleEntitlement> getUserRoleEntitlements() {
		logger.info("Fetching all user role entitlements");
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<UserRoleEntitlement> list = session.createCriteria(UserRoleEntitlement.class).list();
		return ImmutableList.copyOf(list);
	}

	private enum GetUserRoleEntitlementIDFunction implements Function<UserRoleEntitlement, Integer> {
		INSTANCE;

		@Override
		public Integer apply(final UserRoleEntitlement item) {
			return item.getRoleID();
		}
	}
	
	@Override
	public Collection<EntitlementResource> getEntitlements(final String resourceType,
			final Integer userRoleID) {
		final Integer resourceTypeID = refDataDAL.getEnumValue("EntitlementResourceType", resourceType).getValue();
		final ImmutableListMultimap<Integer, UserRoleEntitlement> values = Multimaps
				.index(getUserRoleEntitlements(), GetUserRoleEntitlementIDFunction.INSTANCE);
		
		return Lists.newArrayList(Iterables.filter(getEntitlements(values.asMap().get(userRoleID)), new Predicate<EntitlementResource>() {
			  public boolean apply(EntitlementResource resource) {
				    return resource.getResourceType().equals(resourceTypeID);
				  }
				}));
	}

	private List<UserInfo> getUsers() {
		logger.info("Fetching all users");
		final Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		final List<UserInfo> list = session.createCriteria(UserInfo.class).list();
		return ImmutableList.copyOf(list);
	}
	private enum GetUserNameFunction implements Function<UserInfo, String> {
		INSTANCE;

		@Override
		public String apply(final UserInfo item) {
			return item.getUsername();
		}
	}
	@Override
	public UserInfo getUser(final String username) {
		return Maps.uniqueIndex(getUsers(), GetUserNameFunction.INSTANCE).get(username);
	}

	@Override
	public Integer getDealerID(final String username) {
		return getUser(username).getDealerID();
	}

	@Override
	public void saveOrUpdateUser(final UserInfo user) {
		final Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(user);
	}

}
