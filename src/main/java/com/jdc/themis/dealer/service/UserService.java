package com.jdc.themis.dealer.service;

public interface UserService {

	void createNewUser();
	
	void deactivateUser(String username);
	
	void getUserEntitlements(String username);
	
	void getUser(String username);
	
}
