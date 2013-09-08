package com.jdc.themis.dealer.data.dao;

import java.util.List;

import com.jdc.themis.dealer.domain.UserInfo;
import com.jdc.themis.dealer.domain.UserRole;

/**
 * User information data access layer. 
 * 
 * @author Kai Chen
 *
 */
public interface UserDAO {

	List<UserRole> getUserRoles();
	
	UserRole getUserRole(Integer roleID);
	
	UserInfo getUser(String username);
	
	void saveOrUpdateUser(UserInfo user);
	
	Integer getDealerID(String username);
	
}
