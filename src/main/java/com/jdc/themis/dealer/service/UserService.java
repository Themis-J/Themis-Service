package com.jdc.themis.dealer.service;

import org.springframework.transaction.annotation.Transactional;

import com.jdc.themis.dealer.web.domain.GetUserInfoResponse;

public interface UserService {

	void createNewUser();
	
	void deactivateUser(String username);
	
	@Transactional(readOnly=true)
	void getUserEntitlements(String username);
	
	@Transactional(readOnly=true)
	GetUserInfoResponse getUser(String username);
	
}
