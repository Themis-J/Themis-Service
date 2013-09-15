package com.jdc.themis.dealer.service;

import org.springframework.transaction.annotation.Transactional;

import com.jdc.themis.dealer.web.domain.AddNewUserRequest;
import com.jdc.themis.dealer.web.domain.GetUserInfoResponse;

public interface UserService {

	@Transactional
	void addNewUser(AddNewUserRequest request);
	
	@Transactional
	void disableUser(String username);
	
	@Transactional
	void enableUser(String username);
	
	@Transactional(readOnly=true)
	GetUserInfoResponse getUser(String username);
	
}
