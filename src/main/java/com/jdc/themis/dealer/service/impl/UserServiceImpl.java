package com.jdc.themis.dealer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.jdc.themis.dealer.data.dao.UserDAO;
import com.jdc.themis.dealer.domain.UserInfo;
import com.jdc.themis.dealer.service.RefDataQueryService;
import com.jdc.themis.dealer.service.UserService;
import com.jdc.themis.dealer.utils.Performance;
import com.jdc.themis.dealer.web.domain.AddNewUserRequest;
import com.jdc.themis.dealer.web.domain.GetUserInfoResponse;

/**
 * Service layer to manage user information.
 * 
 * @author Kai Chen
 *
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDAL;
	@Autowired
	private RefDataQueryService refDataQueryDAL;
	
	@Override
	@Performance
	public void addNewUser(final AddNewUserRequest request) {
		Preconditions.checkNotNull(request.getUsername(), "user name can't be null");
		Preconditions.checkArgument(userDAL.getUser(request.getUsername()).isNone(), "user name already exists");
		Preconditions.checkNotNull(request.getPassword(), "password can't be null");
		Preconditions.checkNotNull(request.getUserRole(), "user role can't be null");
		
		final UserInfo user = new UserInfo();
		user.setUsername(request.getUsername());
		user.setPassword(request.getPassword());
		user.setActive(Boolean.TRUE);
		user.setDealerID(refDataQueryDAL.getDealer(request.getDealerID()).getId());
		userDAL.saveOrUpdateUser(user);
	}

	@Override
	@Performance
	public void disableUser(String username) {
		Preconditions.checkNotNull(username, "username can't be null");
		Preconditions.checkArgument(userDAL.getUser(username).isSome(), "unknown user name");

		final UserInfo user = userDAL.getUser(username).some();
		user.setActive(Boolean.FALSE);
		userDAL.saveOrUpdateUser(user);
	}

	@Override
	@Performance
	public GetUserInfoResponse getUser(String username) {
		Preconditions.checkNotNull(username, "username can't be null");
		Preconditions.checkArgument(userDAL.getUser(username).isSome(), "unknown user name");

		final GetUserInfoResponse response = new GetUserInfoResponse();
		response.setUsername(username);
		if ( userDAL.getUser(username).some().getDealerID() != null ) {
			response.setDealer(refDataQueryDAL.getDealer(userDAL.getUser(username).some().getDealerID()));
		} 
		response.setRole(userDAL.getUserRole(userDAL.getUser(username).some().getUserRoleID()).some().getName());
		return response;
	}

	@Override
	public void enableUser(String username) {
		Preconditions.checkNotNull(username, "username can't be null");
		Preconditions.checkNotNull(userDAL.getUser(username).isSome(), "unknown user name");

		final UserInfo user = userDAL.getUser(username).some();
		user.setActive(Boolean.TRUE);
		userDAL.saveOrUpdateUser(user);
	}

}
