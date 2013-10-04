package com.jdc.themis.dealer.service.rest;

import static com.jdc.themis.dealer.service.rest.RestClientUtils.createGetMethod;
import static com.jdc.themis.dealer.service.rest.RestClientUtils.createPostMethod;

import java.util.List;

import junit.framework.Assert;

import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import com.jdc.themis.dealer.web.domain.DepartmentDetail;
import com.jdc.themis.dealer.web.domain.GetDealerResponse;
import com.jdc.themis.dealer.web.domain.GetMenuResponse;
import com.jdc.themis.dealer.web.domain.GetSalesServiceJournalItemResponse;
import com.jdc.themis.dealer.web.domain.VehicleDetail;

public class DealerEntryRestServiceTest {

	private final static String ROOT_URL = "http://localhost:8080/themis/dealer/";
	//private final static String ROOT_URL = "http://115.28.15.122:8080/themis/dealer/";
	
	@Test
	public void getDepartments() throws Exception {
		final GetMethod mGet = createGetMethod(ROOT_URL + "department", new String[] {});
		final String output = mGet.getResponseBodyAsString();
		mGet.releaseConnection();

		final ObjectMapper mapper = new ObjectMapper();
		final DepartmentItems items = mapper.readValue(output.getBytes(),
				DepartmentItems.class);
		System.out.println("departments : " + new String(output.getBytes("ISO-8859-1")));

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
	
	@Test
	public void getMenus() throws Exception {
		final GetMethod mGet = createGetMethod(ROOT_URL + "menu", new String[] {});
		final String output = mGet.getResponseBodyAsString();
		mGet.releaseConnection();

		final ObjectMapper mapper = new ObjectMapper();
		final GetMenuResponse response = mapper.readValue(output.getBytes(),
				GetMenuResponse.class);
		System.out.println("menus : " + new String(output.getBytes("ISO-8859-1")));

		Assert.assertEquals(29, response.getItems().size());
		Assert.assertEquals("EmployeeDividend", response.getItems().get(5).getName());
		Assert.assertEquals("NonRecurrentPNL", response.getItems().get(6).getName());
	}
	
	@Test
	public void getDealers() throws Exception {
		final GetMethod mGet = createGetMethod(ROOT_URL + "list", new String[] {});
		final String output = mGet.getResponseBodyAsString();
		mGet.releaseConnection();

		final ObjectMapper mapper = new ObjectMapper();
		final GetDealerResponse items = mapper.readValue(output.getBytes(),
				GetDealerResponse.class);
		System.out.println("dealers : " + new String(output.getBytes("ISO-8859-1")));

		Assert.assertEquals(20, items.getItems().size());
		Assert.assertEquals("CN-01", items.getItems().get(0).getCode());
	}

	@Test
	public void getVehiclesByCategoryID() throws Exception {
		final GetMethod mGet = createGetMethod(ROOT_URL + "vehicle",
				new String[] { "categoryID:1" });
		final String output = mGet.getResponseBodyAsString();
		mGet.releaseConnection();
		System.out.println("vehicles : " + new String(output.getBytes("ISO-8859-1")));
		final ObjectMapper mapper = new ObjectMapper();
		final VechicleItems items = mapper.readValue(output.getBytes(),
				VechicleItems.class);

		Assert.assertEquals(22, items.getItems().size());
		Assert.assertEquals(0, items.getItems().get(0).getTypeID().intValue());
	}

	public static class VechicleItems {
		private List<VehicleDetail> items;

		public List<VehicleDetail> getItems() {
			return items;
		}

		public void setItems(List<VehicleDetail> items) {
			this.items = items;
		}

	}
	
	@Test
	public void getSalesServiceJournalItemsByCategoryID() throws Exception {
		final GetMethod mGet = createGetMethod(ROOT_URL + "salesServiceRevenue/items",
				new String[] { "categoryID:3" });
		final String output = mGet.getResponseBodyAsString();
		mGet.releaseConnection();
		System.out.println("items : " + new String(output.getBytes("ISO-8859-1")));
		final ObjectMapper mapper = new ObjectMapper();
		final GetSalesServiceJournalItemResponse items = mapper.readValue(output.getBytes(),
				GetSalesServiceJournalItemResponse.class);

		Assert.assertEquals(5, items.getItems().size());
	}
	
	@Test
	public void verifyDealerEntryItemStatus() throws Exception {
		final StringRequestEntity requestEntity = new StringRequestEntity(
			      "{" +
			      "\"dealerID\": 9," +
			      "\"itemID\": 6, " +
				  "\"validDate\": \"2010-08-01\"," +
				  "\"updateBy\": \"chenkai\" " +
				  "}",
			    "application/json",
			    "UTF-8");
		
		final PostMethod mPost = createPostMethod(ROOT_URL + "menu/entrystatus",
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
		final GetMethod mGet = createGetMethod(ROOT_URL + "menu/entrystatus",
				new String[] { "dealerID:9", "validDate:2010-08-01" });
		final String getOutput = mGet.getResponseBodyAsString();
		mGet.releaseConnection();
		System.out.println("response : " + new String(getOutput.getBytes("ISO-8859-1")));
		final ObjectMapper getMapper = new ObjectMapper();
		final GetDealerEntryItemStatusResponse getResponse = getMapper.readValue(getOutput.getBytes(),
				GetDealerEntryItemStatusResponse.class);
		Assert.assertNotNull(getResponse);
		Assert.assertEquals(1, getResponse.getDetail().size());
		Assert.assertEquals(6, getResponse.getDetail().get(0).getId().intValue());
		Assert.assertEquals("2010-08-01", getResponse.getValidDate().getText());
		
		// missing valid date and return error
		Assert.assertEquals(400, createGetMethod(ROOT_URL + "menu/entrystatus",
						new String[] { "dealerID:9" }).getStatusCode());
		// unknown dealer id
		Assert.assertEquals(400, createGetMethod(ROOT_URL + "menu/entrystatus",
				new String[] { "dealerID:90", "validDate:2010-08-01" }).getStatusCode());
	}

	@Test
	public void verifyVehicleSalesJournal() throws Exception {
		final StringRequestEntity requestEntity = new StringRequestEntity(
			      "{\"dealerID\": 11," +
			      "\"departmentID\": 1, " +
				  "\"validDate\": \"2011-08-01\"," +
				  "\"updateBy\": \"chenkai\", " +
				  "\"detail\": " +
				  "  [" +
				  "     {" +
				  "       \"vehicleID\": 2," + 
				  "       \"amount\": 1234.00," +
				  "       \"margin\": 4321.00," +
				  "       \"count\": 134 " +
				  "      }" +
				  "  ]}",
			    "application/json",
			    "UTF-8");
		
		final PostMethod mPost = createPostMethod(ROOT_URL + "vehicleSalesRevenue",
				requestEntity);
		final String postOutput = mPost.getResponseBodyAsString();
		mPost.releaseConnection();
		System.out.println("response : " + postOutput);
		final ObjectMapper postMapper = new ObjectMapper();
		final GeneralSaveResponse response = postMapper.readValue(postOutput.getBytes(),
				GeneralSaveResponse.class);
		Assert.assertNotNull(response);
		Assert.assertEquals(Boolean.TRUE, response.getSuccess());
		
		// query journal and try to match the saved journal
		
		final GetMethod mGet = createGetMethod(ROOT_URL + "vehicleSalesRevenue",
				new String[] { "dealerID:11", "validDate:2011-08-01" });
		final String getOutput = mGet.getResponseBodyAsString();
		mGet.releaseConnection();
		System.out.println("response : " + new String(getOutput.getBytes("ISO-8859-1")));
		final ObjectMapper getMapper = new ObjectMapper();
		final GetVehicleSalesJournalResponse getResponse = getMapper.readValue(getOutput.getBytes(),
				GetVehicleSalesJournalResponse.class);
		Assert.assertNotNull(getResponse);
		Assert.assertEquals(1, getResponse.getDetail().size());
		Assert.assertEquals(2, getResponse.getDetail().get(0).getVehicleID().intValue());
		Assert.assertEquals(1234.0, getResponse.getDetail().get(0).getAmount());
		Assert.assertEquals("2011-08-01", getResponse.getValidDate().getText());
	}
	
	@Test
	public void verifySalesServiceJournal() throws Exception {
		final StringRequestEntity requestEntity = new StringRequestEntity(
			      "{\"dealerID\": 11," +
			      "\"departmentID\": 1, " +
				  "\"validDate\": \"2011-07-01\"," +
				  "\"updateBy\": \"chenkai\", " +
				  "\"detail\": " +
				  "  [" +
				  "     {" +
				  "       \"itemID\": 2," + 
				  "       \"amount\": 123.00," +
				  "       \"margin\": 4321.00," +
				  "       \"count\": 134 " +
				  "      }" +
				  "  ]}",
			    "application/json",
			    "UTF-8");
		
		final PostMethod mPost = createPostMethod(ROOT_URL + "salesServiceRevenue",
				requestEntity);
		final String postOutput = mPost.getResponseBodyAsString();
		mPost.releaseConnection();
		System.out.println("response : " + postOutput);
		final ObjectMapper postMapper = new ObjectMapper();
		final GeneralSaveResponse response = postMapper.readValue(postOutput.getBytes(),
				GeneralSaveResponse.class);
		Assert.assertNotNull(response);
		Assert.assertEquals(Boolean.TRUE, response.getSuccess());
		
		// query journal and try to match the saved journal
		
		final GetMethod mGet = createGetMethod(ROOT_URL + "salesServiceRevenue",
				new String[] { "dealerID:11", "departmentID:1", "validDate:2011-07-01" });
		final String getOutput = mGet.getResponseBodyAsString();
		mGet.releaseConnection();
		System.out.println("response : " + new String(getOutput.getBytes("ISO-8859-1")));
		final ObjectMapper getMapper = new ObjectMapper();
		final GetSalesServiceJournalResponse getResponse = getMapper.readValue(getOutput.getBytes(),
				GetSalesServiceJournalResponse.class);
		Assert.assertNotNull(getResponse);
		Assert.assertEquals(1, getResponse.getDetail().size());
		Assert.assertEquals(2, getResponse.getDetail().get(0).getItemID().intValue());
		Assert.assertEquals(123.0, getResponse.getDetail().get(0).getAmount());
		Assert.assertEquals(4321.0, getResponse.getDetail().get(0).getMargin());
		Assert.assertEquals("2011-07-01", getResponse.getValidDate().getText());
	}
	
	@Test
	public void verifyGeneralJournal() throws Exception {
		final StringRequestEntity requestEntity = new StringRequestEntity(
			      "{\"dealerID\": 8," +
			      "\"departmentID\": 1, " +
				  "\"validDate\": \"2011-06-01\"," +
				  "\"updateBy\": \"chenkai\", " +
				  "\"detail\": " +
				  "  [" +
				  "     {" +
				  "       \"itemID\": 2," + 
				  "       \"amount\": 123.00" +
				  "      }" +
				  "  ]}",
			    "application/json",
			    "UTF-8");
		
		final PostMethod mPost = createPostMethod(ROOT_URL + "generalJournal",
				requestEntity);
		final String postOutput = mPost.getResponseBodyAsString();
		mPost.releaseConnection();
		System.out.println("response : " + postOutput);
		final ObjectMapper postMapper = new ObjectMapper();
		final GeneralSaveResponse response = postMapper.readValue(postOutput.getBytes(),
				GeneralSaveResponse.class);
		Assert.assertNotNull(response);
		Assert.assertEquals(Boolean.TRUE, response.getSuccess());
		
		// query journal and try to match the saved journal
		final GetMethod mGet = createGetMethod(ROOT_URL + "generalJournal",
				new String[] { "dealerID:8", "departmentID:1", "validDate:2011-06-01" });
		final String getOutput = mGet.getResponseBodyAsString();
		mGet.releaseConnection();
		System.out.println("response : " + new String(getOutput.getBytes("ISO-8859-1")));
		final ObjectMapper getMapper = new ObjectMapper();
		final GetGeneralJournalResponse getResponse = getMapper.readValue(getOutput.getBytes(),
				GetGeneralJournalResponse.class);
		Assert.assertNotNull(getResponse);
		Assert.assertEquals(1, getResponse.getDetail().size());
		Assert.assertEquals(2, getResponse.getDetail().get(0).getItemID().intValue());
		Assert.assertEquals(123.0, getResponse.getDetail().get(0).getAmount());
		Assert.assertEquals("2011-06-01", getResponse.getValidDate().getText());
	}
	
	@Test
	public void verifyTaxJournal() throws Exception {
		final StringRequestEntity requestEntity = new StringRequestEntity(
			      "{\"dealerID\": 8," +
			      "\"validDate\": \"2011-06-01\"," +
			      "\"tax\": 123.0," +
				  "\"updateBy\": \"chenkai\" " +
				  "}",
			    "application/json",
			    "UTF-8");
		
		final PostMethod mPost = createPostMethod(ROOT_URL + "tax",
				requestEntity);
		final String postOutput = mPost.getResponseBodyAsString();
		mPost.releaseConnection();
		System.out.println("response : " + postOutput);
		final ObjectMapper postMapper = new ObjectMapper();
		final GeneralSaveResponse response = postMapper.readValue(postOutput.getBytes(),
				GeneralSaveResponse.class);
		Assert.assertNotNull(response);
		Assert.assertEquals(Boolean.TRUE, response.getSuccess());
		
		// query journal and try to match the saved journal
		final GetMethod mGet = createGetMethod(ROOT_URL + "tax",
				new String[] { "dealerID:8", "validDate:2011-06-01" });
		final String getOutput = mGet.getResponseBodyAsString();
		mGet.releaseConnection();
		System.out.println("response : " + new String(getOutput.getBytes("ISO-8859-1")));
		final ObjectMapper getMapper = new ObjectMapper();
		final GetTaxResponse getResponse = getMapper.readValue(getOutput.getBytes(),
				GetTaxResponse.class);
		Assert.assertNotNull(getResponse);
		Assert.assertEquals(123.0, getResponse.getTax());
		Assert.assertEquals("2011-06-01", getResponse.getValidDate().getText());
	}
	
	@Test
	public void verifyEmployeeFeeJournal() throws Exception {
		final StringRequestEntity requestEntity = new StringRequestEntity(
			      "{\"dealerID\": 9," +
			      "\"departmentID\": 6, " +
				  "\"validDate\": \"2010-08-01\"," +
				  "\"updateBy\": \"chenkai\", " +
				  "\"detail\": " +
				  "  [" +
				  "     {" +
				  "       \"itemID\": 2," + 
				  "       \"amount\": 4234.00," +
				  "       \"feeTypeID\": 1 " +
				  "      }" +
				  "  ]}",
			    "application/json",
			    "UTF-8");
		
		final PostMethod mPost = createPostMethod(ROOT_URL + "employee/fee",
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
		final GetMethod mGet = createGetMethod(ROOT_URL + "employee/fee",
				new String[] { "dealerID:9", "departmentID:6", "validDate:2010-08-01" });
		final String getOutput = mGet.getResponseBodyAsString();
		mGet.releaseConnection();
		System.out.println("response : " + new String(getOutput.getBytes("ISO-8859-1")));
		final ObjectMapper getMapper = new ObjectMapper();
		final GetEmployeeFeeResponse getResponse = getMapper.readValue(getOutput.getBytes(),
				GetEmployeeFeeResponse.class);
		Assert.assertNotNull(getResponse);
		Assert.assertEquals(1, getResponse.getDetail().size());
		Assert.assertEquals(2, getResponse.getDetail().get(0).getItemID().intValue());
		Assert.assertEquals(4234.0, getResponse.getDetail().get(0).getAmount());
		Assert.assertEquals("2010-08-01", getResponse.getValidDate().getText());
	}
	
	@Test
	public void verifyAccountReceivableDuration() throws Exception {
		final StringRequestEntity requestEntity = new StringRequestEntity(
			      "{\"dealerID\": 9," +
			      "\"validDate\": \"2010-08-01\"," +
				  "\"updateBy\": \"chenkai\", " +
				  "\"detail\": " +
				  "  [" +
				  "     {" +
				  "       \"itemID\": 2," + 
				  "       \"amount\": 4234.00," +
				  "       \"durationID\": 2 " +
				  "      }" +
				  "  ]}",
			    "application/json",
			    "UTF-8");
		
		final PostMethod mPost = createPostMethod(ROOT_URL + "accountReceivable/duration",
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
		final GetMethod mGet = createGetMethod(ROOT_URL + "accountReceivable/duration",
				new String[] { "dealerID:9", "validDate:2010-08-01" });
		final String getOutput = mGet.getResponseBodyAsString();
		mGet.releaseConnection();
		System.out.println("response : " + new String(getOutput.getBytes("ISO-8859-1")));
		final ObjectMapper getMapper = new ObjectMapper();
		final GetAccountReceivableDurationResponse getResponse = getMapper.readValue(getOutput.getBytes(),
				GetAccountReceivableDurationResponse.class);
		Assert.assertNotNull(getResponse);
		Assert.assertEquals(1, getResponse.getDetail().size());
		Assert.assertEquals(2, getResponse.getDetail().get(0).getItemID().intValue());
		Assert.assertEquals(4234.0, getResponse.getDetail().get(0).getAmount());
		Assert.assertEquals("2010-08-01", getResponse.getValidDate().getText());
	}
	
	@Test
	public void verifyInventoryDuration() throws Exception {
		final StringRequestEntity requestEntity = new StringRequestEntity(
			      "{\"dealerID\": 9," +
			      "\"validDate\": \"2010-08-01\"," +
			      "\"updateBy\": \"chenkai\", " +
				  "\"departmentID\": 2, " +
				  "\"detail\": " +
				  "  [" +
				  "     {" +
				  "       \"itemID\": 2," + 
				  "       \"amount\": 4234.00," +
				  "       \"durationID\": 2 " +
				  "      }" +
				  "  ]}",
			    "application/json",
			    "UTF-8");
		
		final PostMethod mPost = createPostMethod(ROOT_URL + "inventory/duration",
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
		final GetMethod mGet = createGetMethod(ROOT_URL + "inventory/duration",
				new String[] { "dealerID:9", "departmentID:2", "validDate:2010-08-01" });
		final String getOutput = mGet.getResponseBodyAsString();
		mGet.releaseConnection();
		System.out.println("response : " + new String(getOutput.getBytes("ISO-8859-1")));
		final ObjectMapper getMapper = new ObjectMapper();
		final GetInventoryDurationResponse getResponse = getMapper.readValue(getOutput.getBytes(),
				GetInventoryDurationResponse.class);
		Assert.assertNotNull(getResponse);
		Assert.assertEquals(1, getResponse.getDetail().size());
		Assert.assertEquals(2, getResponse.getDetail().get(0).getItemID().intValue());
		Assert.assertEquals(4234.0, getResponse.getDetail().get(0).getAmount());
		Assert.assertEquals("2010-08-01", getResponse.getValidDate().getText());
	}
	
	@Test
	public void verifyEmployeeFeeSummaryJournal() throws Exception {
		final StringRequestEntity requestEntity = new StringRequestEntity(
			      "{\"dealerID\": 9," +
			      "\"departmentID\": 6, " +
				  "\"validDate\": \"2010-08-01\"," +
				  "\"updateBy\": \"chenkai\", " +
				  "\"detail\": " +
				  "  [" +
				  "     {" +
				  "       \"itemID\": 2," + 
				  "       \"amount\": 4234.00" +
				  "      }" +
				  "  ]}",
			    "application/json",
			    "UTF-8");
		
		final PostMethod mPost = createPostMethod(ROOT_URL + "employee/feeSummary",
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
		final GetMethod mGet = createGetMethod(ROOT_URL + "employee/feeSummary",
				new String[] { "dealerID:9", "departmentID:6", "validDate:2010-08-01" });
		final String getOutput = mGet.getResponseBodyAsString();
		mGet.releaseConnection();
		System.out.println("response : " + new String(getOutput.getBytes("ISO-8859-1")));
		final ObjectMapper getMapper = new ObjectMapper();
		final GetEmployeeFeeSummaryResponse getResponse = getMapper.readValue(getOutput.getBytes(),
				GetEmployeeFeeSummaryResponse.class);
		Assert.assertNotNull(getResponse);
		Assert.assertEquals(1, getResponse.getDetail().size());
		Assert.assertEquals(2, getResponse.getDetail().get(0).getItemID().intValue());
		Assert.assertEquals(4234.0, getResponse.getDetail().get(0).getAmount());
		Assert.assertEquals("2010-08-01", getResponse.getValidDate().getText());
		
		// missing department id and return error
		Assert.assertEquals(400, createGetMethod(ROOT_URL + "employee/feeSummary",
				new String[] { "dealerID:9", "validDate:2010-08-01" }).getStatusCode());
		// missing valid date and return error
		Assert.assertEquals(400, createGetMethod(ROOT_URL + "employee/feeSummary",
						new String[] { "dealerID:9", "departmentID:6" }).getStatusCode());
		// unknown dealer id
		Assert.assertEquals(400, createGetMethod(ROOT_URL + "employee/feeSummary",
				new String[] { "dealerID:90", "departmentID:6", "validDate:2010-08-01" }).getStatusCode());
	}
	
	@Test
	public void verifyHumanResourceAllocation() throws Exception {
		final StringRequestEntity requestEntity = new StringRequestEntity(
			      "{\"dealerID\": 9," +
			      "\"departmentID\":6," +
				  "\"validDate\": \"2010-08-01\"," +
				  "\"updateBy\": \"chenkai\", " +
				  "\"detail\": " +
				  "  [" +
				  "     {" +
				  "       \"itemID\": 2," + 
				  "       \"allocation\": 0.2" +
				  "      }" +
				  "  ]}",
			    "application/json",
			    "UTF-8");
		
		final PostMethod mPost = createPostMethod(ROOT_URL + "hr/allocation",
				requestEntity);
		final String output = mPost.getResponseBodyAsString();
		mPost.releaseConnection();
		System.out.println("response : " + output);
		final ObjectMapper mapper = new ObjectMapper();
		final GeneralSaveResponse response = mapper.readValue(output.getBytes(),
				GeneralSaveResponse.class);
		Assert.assertNotNull(response);
		Assert.assertEquals(Boolean.TRUE, response.getSuccess());
		
		final GetMethod mGet = createGetMethod(ROOT_URL + "hr/allocation",
				new String[] { "dealerID:9", "departmentID:6", "validDate:2010-08-01" });
		final String getOutput = mGet.getResponseBodyAsString();
		mGet.releaseConnection();
		System.out.println("response : " + new String(getOutput.getBytes("ISO-8859-1")));
		final ObjectMapper getMapper = new ObjectMapper();
		final GetHumanResourceAllocationResponse getResponse = getMapper.readValue(getOutput.getBytes(),
				GetHumanResourceAllocationResponse.class);
		Assert.assertNotNull(getResponse);
		Assert.assertEquals(1, getResponse.getDetail().size());
		Assert.assertEquals(2, getResponse.getDetail().get(0).getItemID().intValue());
		Assert.assertEquals(0.2, getResponse.getDetail().get(0).getAllocation());
		Assert.assertEquals("2010-08-01", getResponse.getValidDate().getText());
	}
	
}
