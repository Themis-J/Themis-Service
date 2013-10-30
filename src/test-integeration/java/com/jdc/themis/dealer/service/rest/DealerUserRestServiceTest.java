package com.jdc.themis.dealer.service.rest;

import static com.jdc.themis.dealer.service.rest.RestClientUtils.createGetMethod;
import static com.jdc.themis.dealer.service.rest.RestClientUtils.createPostMethod;

import java.util.Random;

import junit.framework.Assert;

import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import com.jdc.themis.dealer.web.domain.GetUserInfoResponse;
import com.jdc.themis.dealer.web.domain.GetUserRoleResponse;

public class DealerUserRestServiceTest {

	private final static String ROOT_URL = "http://localhost:8080/themis/user/";
	//private final static String ROOT_URL = "http://115.28.15.122:8080/themis/user/";
	
	@Test
	public void getUserRoles() throws Exception {
		final GetMethod mGet = createGetMethod(ROOT_URL + "roles", new String[] {});
		final String output = mGet.getResponseBodyAsString();
		mGet.releaseConnection();

		final ObjectMapper mapper = new ObjectMapper();
		final GetUserRoleResponse items = mapper.readValue(output.getBytes(),
				GetUserRoleResponse.class);
		System.out.println("roles : " + new String(output.getBytes("ISO-8859-1")));

		Assert.assertEquals(4, items.getDetail().size());
		Assert.assertEquals("Admin", items.getDetail().get(1).getName());
	}
	
	@Test
	public void addDealerUser() throws Exception {
		final Random r = new Random();
		final String username = "test" + r.nextInt(10000);
		final StringRequestEntity requestEntity = new StringRequestEntity(
			      "{" +
			      "\"username\": \"" + username + "\"," +
			      "\"password\": \"testpwd\", " +
				  "\"userRole\": 2," +
				  "\"dealerID\": 10 " +
				  "}",
			    "application/json",
			    "UTF-8");
		
		final PostMethod mPost = createPostMethod(ROOT_URL + "add",
				requestEntity);
		final String output = mPost.getResponseBodyAsString();
		mPost.releaseConnection();
		System.out.println("response : " + output);
		final ObjectMapper mapper = new ObjectMapper();
		final GeneralSaveResponse response = mapper.readValue(output.getBytes(),
				GeneralSaveResponse.class);
		Assert.assertNotNull(response);
		Assert.assertEquals(Boolean.TRUE, response.getSuccess());
		
		// query employee fee journal and try to match the saved journal
		final GetMethod mGet = createGetMethod(ROOT_URL + "info",
				new String[] { "username:" + username });
		final String getOutput = mGet.getResponseBodyAsString();
		mGet.releaseConnection();
		System.out.println("response : " + new String(getOutput.getBytes("ISO-8859-1")));
		final ObjectMapper getMapper = new ObjectMapper();
		final GetUserInfoResponse getResponse = getMapper.readValue(getOutput.getBytes(),
				GetUserInfoResponse.class);
		Assert.assertNotNull(getResponse);
		
	}
	
	@Test
	public void addManagerUser() throws Exception {
		final Random r = new Random();
		final String username = "test" + r.nextInt(10000);
		final StringRequestEntity requestEntity = new StringRequestEntity(
			      "{" +
			      "\"username\": \"" + username + "\"," +
			      "\"password\": \"testpwd\", " +
				  "\"userRole\": 3" +
				  "}",
			    "application/json",
			    "UTF-8");
		
		final PostMethod mPost = createPostMethod(ROOT_URL + "add",
				requestEntity);
		final String output = mPost.getResponseBodyAsString();
		mPost.releaseConnection();
		System.out.println("response : " + output);
		final ObjectMapper mapper = new ObjectMapper();
		final GeneralSaveResponse response = mapper.readValue(output.getBytes(),
				GeneralSaveResponse.class);
		Assert.assertNotNull(response);
		Assert.assertEquals(Boolean.TRUE, response.getSuccess());
		
		// query employee fee journal and try to match the saved journal
		final GetMethod mGet = createGetMethod(ROOT_URL + "info",
				new String[] { "username:" + username });
		final String getOutput = mGet.getResponseBodyAsString();
		mGet.releaseConnection();
		System.out.println("response : " + new String(getOutput.getBytes("ISO-8859-1")));
		final ObjectMapper getMapper = new ObjectMapper();
		final GetUserInfoResponse getResponse = getMapper.readValue(getOutput.getBytes(),
				GetUserInfoResponse.class);
		Assert.assertNotNull(getResponse);
		
	}
	
	@Test
	public void resetPasswd() throws Exception {
		final Random r = new Random();
		final String username = "test" + r.nextInt(10000);
		final StringRequestEntity newUserRequestEntity = new StringRequestEntity(
			      "{" +
			      "\"username\": \"" + username + "\"," +
			      "\"password\": \"testpwd\", " +
				  "\"userRole\": 2," +
				  "\"dealerID\": 10 " +
				  "}",
			    "application/json",
			    "UTF-8");
		
		final PostMethod newUserMPost = createPostMethod(ROOT_URL + "add",
				newUserRequestEntity);
		final String newUserOutput = newUserMPost.getResponseBodyAsString();
		newUserMPost.releaseConnection();
		System.out.println("response : " + newUserOutput);
		
		final StringRequestEntity requestEntity = new StringRequestEntity(
			      "{" +
			      "\"username\": \"" + username + "\"," +
			      "\"oldPassword\": \"testpwd\", " +
			      "\"newPassword\": \"testpwd2\" " +
				  "}",
			    "application/json",
			    "UTF-8");
		
		final PostMethod mPost = createPostMethod(ROOT_URL + "resetpwd",
				requestEntity);
		final String output = mPost.getResponseBodyAsString();
		mPost.releaseConnection();
		System.out.println("response : " + output);
		final ObjectMapper mapper = new ObjectMapper();
		final GeneralSaveResponse response = mapper.readValue(output.getBytes(),
				GeneralSaveResponse.class);
		Assert.assertNotNull(response);
		Assert.assertEquals(Boolean.TRUE, response.getSuccess());
		
		// query employee fee journal and try to match the saved journal
		final GetMethod mGet = createGetMethod(ROOT_URL + "info",
				new String[] { "username:" + username });
		final String getOutput = mGet.getResponseBodyAsString();
		mGet.releaseConnection();
		System.out.println("response : " + new String(getOutput.getBytes("ISO-8859-1")));
		final ObjectMapper getMapper = new ObjectMapper();
		final GetUserInfoResponse getResponse = getMapper.readValue(getOutput.getBytes(),
				GetUserInfoResponse.class);
		Assert.assertNotNull(getResponse);
		
	}

}
