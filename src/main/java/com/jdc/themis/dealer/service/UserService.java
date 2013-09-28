package com.jdc.themis.dealer.service;

import javax.time.Instant;

import org.springframework.transaction.annotation.Transactional;
import com.jdc.themis.dealer.web.domain.AddNewUserRequest;
import com.jdc.themis.dealer.web.domain.ModifyUserRequest;
import com.jdc.themis.dealer.web.domain.ResetPasswordRequest;
import com.jdc.themis.dealer.web.domain.GetUserInfoResponse;

public interface UserService {

	@Transactional
	Instant addNewUser(AddNewUserRequest request);
	
	@Transactional
	Instant modifyUser(ModifyUserRequest request);
	
	@Transactional
	Instant resetPassword(ResetPasswordRequest request);
	
	@Transactional
	Instant disableUser(String username);
	
	@Transactional
	Instant enableUser(String username);
	
	@Transactional(readOnly=true)
	GetUserInfoResponse getUser(String username);
	
}
