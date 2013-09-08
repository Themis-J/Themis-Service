package com.jdc.themis.dealer.data.dao;

import java.util.List;

import com.jdc.themis.dealer.domain.UserInfo;
import com.jdc.themis.dealer.domain.UserRole;

import fj.data.Option;

/**
 * User information data access layer. 
 * 
 * @author Kai Chen
 *
 */
public interface UserDAO {

	List<UserRole> getUserRoles();
	
	Option<UserRole> getUserRole(Integer roleID);
	
	Option<UserInfo> getUser(String username);
	
	void saveOrUpdateUser(UserInfo user);
	
}
