package com.jdc.themis.dealer.data.dao.hibernate;

import static com.jdc.themis.dealer.domain.TemporalEntity.INFINITE_TIMEEND;

import java.util.Date;
import java.util.List;

import javax.time.Instant;

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
import com.jdc.themis.dealer.utils.Utils;

import fj.P1;
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
	
	@Override
	public Option<UserInfo> getUser(final String username) {
		final Session session = sessionFactory.getCurrentSession();
		final Instant currentTimestamp = Instant.millis(new Date().getTime());
		session.enableFilter(UserInfo.FILTER)
			.setParameter("username", username)
			.setParameter("referenceTime", currentTimestamp);
		@SuppressWarnings("unchecked")
		final List<UserInfo> list = session.createCriteria(UserInfo.class).list();
		session.disableFilter(UserInfo.FILTER);
		return Option.<UserInfo>iif(!list.isEmpty(), new P1<UserInfo>() {

			@Override
			public UserInfo _1() {
				return list.get(0);
			}
			
		});
	}
	
	@Override
	public Instant saveOrUpdateUser(final UserInfo user) {
		Preconditions.checkNotNull(user.getUsername(), "user name can't be null");
		Preconditions.checkNotNull(user.getPassword(), "password can't be null");
		Preconditions.checkNotNull(user.getUserRoleID(), "user role can't be null");
		
		final Session session = sessionFactory.getCurrentSession();
		final Instant currentTimestamp = Utils.currentTimestamp();
		
		// check whether this journal has been inserted before
		session.enableFilter(UserInfo.FILTER)
			.setParameter("username", user.getUsername())
			.setParameter("referenceTime", currentTimestamp);
		@SuppressWarnings("unchecked")
		final List<UserInfo> list = session.createCriteria(UserInfo.class).list();
		for ( final UserInfo oldJournal : list ) {
			if ( oldJournal.getTimeEnd().isBefore(INFINITE_TIMEEND) ) {
				logger.warn("TimeEnd of the one in database is closed already. {}, {}", oldJournal, currentTimestamp);
			} else {
				oldJournal.setTimeEnd(currentTimestamp);
				session.saveOrUpdate(oldJournal);
			} 	
		} 
		session.disableFilter(UserInfo.FILTER);
		user.setTimestamp(currentTimestamp);
		user.setTimeEnd(INFINITE_TIMEEND);
		session.save(user);
		session.flush();
		return currentTimestamp;
	}

}
