package com.jdc.themis.dealer.data.dao.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.jdc.themis.dealer.data.dao.UserDAO;
import com.jdc.themis.dealer.domain.UserInfo;
import com.jdc.themis.dealer.domain.UserRole;

import fj.data.Option;

@Service
public class UserDAOImpl implements UserDAO {
	private final static Logger logger = LoggerFactory
			.getLogger(UserDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	
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
	public Option<UserRole> getUserRole(final Integer roleID) {
		final UserRole userRole = Maps.uniqueIndex(getUserRoles(), GetUserRoleIDFunction.INSTANCE).get(roleID);
		if ( userRole == null ) {
			return Option.<UserRole>none();
		}
		return Option.<UserRole>some(userRole);
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
	public Option<UserInfo> getUser(final String username) {
		final UserInfo userInfo = Maps.uniqueIndex(getUsers(), GetUserNameFunction.INSTANCE).get(username);
		if ( userInfo == null ) {
			return Option.<UserInfo>none();
		}
		return Option.<UserInfo>some(userInfo);
	}

	@Override
	public void saveOrUpdateUser(final UserInfo user) {
		Preconditions.checkNotNull(user.getUsername(), "user name can't be null");
		Preconditions.checkNotNull(user.getPassword(), "password can't be null");
		Preconditions.checkNotNull(user.getUserRoleID(), "user role can't be null");
		final Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(user);
	}

}
