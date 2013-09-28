package com.jdc.themis.dealer.service.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.jdc.themis.dealer.data.dao.UserDAO;
import com.jdc.themis.dealer.domain.UserInfo;
import com.jdc.themis.dealer.service.RefDataQueryService;
import com.jdc.themis.dealer.web.domain.AddNewUserRequest;
import com.jdc.themis.dealer.web.domain.DealerDetail;

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
}
