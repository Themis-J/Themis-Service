package com.jdc.themis.dealer.service.rest;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import com.jdc.themis.dealer.web.domain.DepartmentDetail;

import java.io.IOException;
import java.util.*;

import junit.framework.Assert;

public class RestServiceTest {

	private final static String ROOT_URL = "http://localhost:8080/themis/dealer/";
	private GetMethod createGetMethod(final String url) throws HttpException,
			IOException {
		final HttpClient client = new HttpClient();
		final GetMethod mGet = new GetMethod(
				ROOT_URL + url);
		client.executeMethod(mGet);
		final Header mtHeader = new Header();
		mtHeader.setName("content-type");
		mtHeader.setValue("application/json");
		mtHeader.setName("accept");
		mtHeader.setValue("application/json");
		mGet.addRequestHeader(mtHeader);
		return mGet;
	}

	@Test
	public void getDepartments() throws Exception {
		final GetMethod mGet = createGetMethod("department");
		final String output = mGet.getResponseBodyAsString();
		mGet.releaseConnection();

		final ObjectMapper mapper = new ObjectMapper();
		final DepartmentItems items = mapper.readValue(output.getBytes(),
				DepartmentItems.class);
		System.out.println("departments : " + output);

		Assert.assertEquals(8, items.getItems().size());
		Assert.assertEquals("NA", items.getItems().get(0).getName());
	}

	public static class DepartmentItems {
		private List<DepartmentDetail> items;

		public List<DepartmentDetail> getItems() {
			return items;
		}

		public void setItems(List<DepartmentDetail> items) {
			this.items = items;
		}

	}
}
