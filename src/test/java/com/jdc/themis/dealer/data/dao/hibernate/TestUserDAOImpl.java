package com.jdc.themis.dealer.data.dao.hibernate;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import com.jdc.themis.dealer.data.dao.UserDAO;

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
	public void getEntitlements() {
		Assert.assertEquals(2, userDAL.getEntitlements("Service", 2).size());
	}
	
}
