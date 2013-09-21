package com.jdc.themis.dealer.data.dao.hibernate;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import com.jdc.themis.dealer.data.dao.UserDAO;
import com.jdc.themis.dealer.domain.UserInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
    "classpath:test-database-config.xml" })
@Transactional
public class TestUserDAOImpl {
	@Autowired
	private UserDAO userDAL;
	
	@Test
	public void getUserRolesSize() {
		Assert.assertEquals(4, userDAL.getUserRoles().size());
	}
	
	@Test
	public void getUserRole() {
		Assert.assertEquals("Admin", userDAL.getUserRole(1).some().getName());
	}
	
	@Test
	public void getUserRoleFail() {
		Assert.assertEquals(true, userDAL.getUserRole(10).isNone());
	}
	
	@Test
	public void getUserInfo() {
		Assert.assertEquals("admin", userDAL.getUser("admin").some().getPassword());
	}
	
	@Test
	public void getUserInfoNotFound() {
		Assert.assertEquals(true, userDAL.getUser("adminxx").isNone());
	}
	
	@Test
	public void addNewUser() {
		final UserInfo userInfo = new UserInfo();
		userInfo.setActive(true);
		userInfo.setDealerID(null);
		userInfo.setPassword("xxx");
		userInfo.setUsername("test");
		userInfo.setUserRoleID(2);
		userDAL.saveOrUpdateUser(userInfo);
		Assert.assertEquals("xxx", userDAL.getUser("test").some().getPassword());
	}
	
	@Test
	public void disableUser() {
		final UserInfo userInfo = new UserInfo();
		userInfo.setActive(true);
		userInfo.setDealerID(null);
		userInfo.setPassword("xxx");
		userInfo.setUsername("test");
		userInfo.setUserRoleID(2);
		userDAL.saveOrUpdateUser(userInfo);
		Assert.assertEquals("xxx", userDAL.getUser("test").some().getPassword());
		
		final UserInfo existingUserInfo = userDAL.getUser("test").some();
		existingUserInfo.setActive(false);
		userDAL.saveOrUpdateUser(existingUserInfo);
		
		Assert.assertEquals(false, userDAL.getUser("test").some().getActive().booleanValue());
	}

}
