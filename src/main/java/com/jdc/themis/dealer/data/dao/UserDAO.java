package com.jdc.themis.dealer.data.dao;

import java.util.List;

import com.jdc.themis.dealer.domain.UserInfo;
import com.jdc.themis.dealer.domain.UserRole;
import com.jdc.themis.dealer.domain.UserRoleEntitlement;

public interface UserDAO {

	List<UserRole> getUserRoles();
	
	UserRole getUserRole(Integer roleID);
	
	List<UserRoleEntitlement> getEntitlements(String resourceType, Integer userRoleID);
	
	UserInfo getUser(String username);
	
	Integer getDealerID(UserInfo user);
	
}
