package com.jdc.themis.dealer.service.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.jdc.themis.dealer.data.dao.UserDAO;
import com.jdc.themis.dealer.domain.UserInfo;
import com.jdc.themis.dealer.domain.UserRole;
import com.jdc.themis.dealer.service.RefDataQueryService;
import com.jdc.themis.dealer.web.domain.AddNewUserRequest;
import com.jdc.themis.dealer.web.domain.DealerDetail;
import com.jdc.themis.dealer.web.domain.GetUserRoleResponse;

import fj.data.Option;

public class TestUserServiceImpl {
	private UserServiceImpl userService;
	@Mock
	private RefDataQueryService refDataQueryDAL;
	@Mock
	private UserDAO userDAL;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks( this );
		refDataQueryDAL = mock(RefDataQueryService.class);
		userDAL = mock(UserDAO.class);
		userService = new UserServiceImpl();
		userService.setRefDataQueryDAL(refDataQueryDAL);
		userService.setUserDAL(userDAL);
	} 
	
	@Test
	public void addNewUser() {
		final AddNewUserRequest request = new AddNewUserRequest();
		request.setDealerID(2);
		request.setPassword("pwd");
		request.setUsername("usr");
		request.setUserRole(0);
		when(userDAL.getUser("usr")).thenReturn(Option.<UserInfo>none());
		when(refDataQueryDAL.getDealer(2)).thenReturn(new DealerDetail());
		userService.addNewUser(request);
		verify(userDAL).saveOrUpdateUser(any(UserInfo.class));
	}
	
	@Test
	public void getUserRoles() {
		final List<UserRole> userRoles = Lists.newArrayList();
		final UserRole role1 = new UserRole();
		role1.setId(1);
		role1.setName("admin");
		final UserRole role2 = new UserRole();
		role2.setId(2);
		role2.setName("dealer");
		userRoles.add(role1);
		userRoles.add(role2);
		
		when(userDAL.getUserRoles()).thenReturn(userRoles);
		
		final GetUserRoleResponse response = userService.getUserRoles();
		
		Assert.assertEquals(2, response.getDetail().size());
	}
}
