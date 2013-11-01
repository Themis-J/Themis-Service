package com.jdc.themis.dealer.service.rest;

import static com.jdc.themis.dealer.service.rest.RestClientUtils.createGetMethod;
import static com.jdc.themis.dealer.service.rest.RestClientUtils.createPostMethod;
import junit.framework.Assert;

import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.jdc.themis.dealer.web.domain.QueryDealerIncomeResponse;
import com.jdc.themis.dealer.web.domain.QueryDealerSalesResponse;
import com.jdc.themis.dealer.web.domain.QueryDepartmentIncomeResponse;
import com.jdc.themis.dealer.web.domain.ReportDealerDataList;

public class DealerReportRestServiceTest {

	//private final static String HOST = "115.28.15.122:8080";
	private final static String HOST = "localhost:8080";
	private final static String ROOT_URL = "http://" + HOST + "/themis/dealer/";
	private final static String REPORT_ROOT_URL = "http://" + HOST + "/themis/report/";
	
	@Before
	public void setUp() throws Exception {
		System.out.println("Setting up testing data");
		createVehicleSalesJournal(11, "2006-08-01", 50000.0, 6000.0, 500);
		createVehicleSalesJournal(11, "2005-08-01", 20000.0, 3000.0, 1000);
		
		createSalesServiceJournal(10, "2006-07-01", 100000.0, 2000.0, 5000);
		createSalesServiceJournal(10, "2005-07-01", 20000.0, 1000.0, 2500);
		
		createGeneralJournal(12, "2006-05-01", 80000.0);
		createGeneralJournal(12, "2005-05-01", 40000.0);
		
		createTaxJournal(13, "2006-04-01", 120000.0);
		createTaxJournal(13, "2005-04-01", 60000.0);
		
		this.createAccountReceivableDuration(9, "2006-06-01", 60000.0);
		this.createAccountReceivableDuration(9, "2005-06-01", 30000.0);
		
		this.createInventoryDuration(8, "2006-06-01", 8000.0);
		this.createInventoryDuration(8, "2005-06-01", 5000.0);
		
		this.createEmployeeFeeJournal(7, "2006-06-01", 100.0);
		this.createEmployeeFeeJournal(7, "2005-06-01", 500.0);
		
		this.createEmployeeFeeSummaryJournal(6, "2006-06-01", 800.0);
		this.createEmployeeFeeSummaryJournal(6, "2005-06-01", 500.0);
		
		this.createHumanResourceAllocation(6, "2006-06-01", 0.6);
		this.createHumanResourceAllocation(6, "2005-06-01", 0.3);
	}
	
	@Test
	@Ignore
	public void importData() throws Exception {
		final StringRequestEntity requestEntity = new StringRequestEntity(
			      "{" +
			      "\"fromDate\": \"2012-01-01\"," +
				  "\"toDate\": \"2013-09-01\" " +
				  "      }" +
				  "}",
			    "application/json",
			    "UTF-8");
		
		final PostMethod mPost = createPostMethod(REPORT_ROOT_URL + "import",
				requestEntity);
		final String postOutput = mPost.getResponseBodyAsString();
		mPost.releaseConnection();
		System.out.println("response : " + postOutput);
		final ObjectMapper postMapper = new ObjectMapper();
		final GeneralSaveResponse response = postMapper.readValue(postOutput.getBytes(),
				GeneralSaveResponse.class);
		Assert.assertNotNull(response);
	}
	
	@Test
	@Ignore
	public void query2013OverallIncomeReport() throws Exception {
		final GetMethod mGet = createGetMethod(REPORT_ROOT_URL + "query/overallIncomeReport",
				new String[] { "year:2013" });
		final String getOutput = mGet.getResponseBodyAsString();
		mGet.releaseConnection();
		System.out.println("response : " + new String(getOutput.getBytes("ISO-8859-1")));
		final ObjectMapper getMapper = new ObjectMapper();
		final QueryDealerIncomeResponse getResponse = getMapper.readValue(getOutput.getBytes(),
				QueryDealerIncomeResponse.class);
		Assert.assertNotNull(getResponse);
		Assert.assertEquals(2, getResponse.getDetail().size());
		/**
		 * Verify 2012 data
		 */
		final ReportDealerDataList report2012 = (ReportDealerDataList) getResponse.getDetail().get(0);
		System.out.println("2012: " + new String(report2012.toString().getBytes("ISO-8859-1")));
		Assert.assertEquals(20, report2012.getDetail().size());
		
		/**
		 * Verify 2013 data
		 */
		final ReportDealerDataList report2013 = (ReportDealerDataList) getResponse.getDetail().get(1);
		System.out.println("2013: " + new String(report2013.toString().getBytes("ISO-8859-1")));
		Assert.assertEquals(20, report2013.getDetail().size());
	}
	
	@Test
	public void query2006OverallIncomeReport() throws Exception {
		final StringRequestEntity requestEntity = new StringRequestEntity(
			      "{" +
			      "\"fromDate\": \"2005-08-01\"," +
				  "\"toDate\": \"2006-08-01\" " +
				  "      }" +
				  "}",
			    "application/json",
			    "UTF-8");
		
		final PostMethod mPost = createPostMethod(REPORT_ROOT_URL + "import",
				requestEntity);
		final String postOutput = mPost.getResponseBodyAsString();
		mPost.releaseConnection();
		System.out.println("response : " + postOutput);
		final ObjectMapper postMapper = new ObjectMapper();
		final GeneralSaveResponse response = postMapper.readValue(postOutput.getBytes(),
				GeneralSaveResponse.class);
		Assert.assertNotNull(response);
		
		final GetMethod mGet = createGetMethod(REPORT_ROOT_URL + "query/overallIncomeReport",
				new String[] { "year:2006" });
		final String getOutput = mGet.getResponseBodyAsString();
		mGet.releaseConnection();
		System.out.println("response : " + new String(getOutput.getBytes("ISO-8859-1")));
		final ObjectMapper getMapper = new ObjectMapper();
		final QueryDealerIncomeResponse getResponse = getMapper.readValue(getOutput.getBytes(),
				QueryDealerIncomeResponse.class);
		Assert.assertNotNull(getResponse);
		Assert.assertEquals(2, getResponse.getDetail().size());
		//2005
		Assert.assertEquals(20, getResponse.getDetail().get(0).getDetail().size());
		
		/**
		 * Verify 2006 data
		 */
		Assert.assertEquals(20, getResponse.getDetail().get(1).getDetail().size());
		// verify dealer 11
		Assert.assertEquals(50000.0, getResponse.getDetail().get(1).getDetail().get(10).getRevenue().getAmount());
		Assert.assertEquals(6000.0, getResponse.getDetail().get(1).getDetail().get(10).getMargin().getAmount());
		// verify dealer 10
		Assert.assertEquals(100000.0, getResponse.getDetail().get(1).getDetail().get(9).getRevenue().getAmount());
		Assert.assertEquals(2000.0, getResponse.getDetail().get(1).getDetail().get(9).getMargin().getAmount());
	
	}
	
	@Test
	public void query200608OverallIncomeReport() throws Exception {
		final StringRequestEntity requestEntity = new StringRequestEntity(
			      "{" +
			      "\"fromDate\": \"2005-08-01\"," +
				  "\"toDate\": \"2006-08-01\" " +
				  "      }" +
				  "}",
			    "application/json",
			    "UTF-8");
		
		final PostMethod mPost = createPostMethod(REPORT_ROOT_URL + "import",
				requestEntity);
		final String postOutput = mPost.getResponseBodyAsString();
		mPost.releaseConnection();
		System.out.println("response : " + postOutput);
		final ObjectMapper postMapper = new ObjectMapper();
		final GeneralSaveResponse response = postMapper.readValue(postOutput.getBytes(),
				GeneralSaveResponse.class);
		Assert.assertNotNull(response);
		
		final GetMethod mGet = createGetMethod(REPORT_ROOT_URL + "query/overallIncomeReport",
				new String[] { "year:2006", "monthOfYear:8" });
		final String getOutput = mGet.getResponseBodyAsString();
		mGet.releaseConnection();
		System.out.println("response : " + new String(getOutput.getBytes("ISO-8859-1")));
		final ObjectMapper getMapper = new ObjectMapper();
		final QueryDealerIncomeResponse getResponse = getMapper.readValue(getOutput.getBytes(),
				QueryDealerIncomeResponse.class);
		Assert.assertNotNull(getResponse);
		Assert.assertEquals(2, getResponse.getDetail().size());
		//2005
		Assert.assertEquals(20, getResponse.getDetail().get(0).getDetail().size());
		
		/**
		 * Verify 2006-08 data
		 */
		Assert.assertEquals(20, getResponse.getDetail().get(1).getDetail().size());
		// verify dealer 11
		Assert.assertEquals(50000.0, getResponse.getDetail().get(1).getDetail().get(10).getRevenue().getAmount());
		Assert.assertEquals(6000.0, getResponse.getDetail().get(1).getDetail().get(10).getMargin().getAmount());
		// verify dealer 10
		Assert.assertEquals(0.0, getResponse.getDetail().get(1).getDetail().get(9).getRevenue().getAmount());
		Assert.assertEquals(0.0, getResponse.getDetail().get(1).getDetail().get(9).getMargin().getAmount());
	
	}
	
	@Test
	public void query2006DepartmentIncomeReport() throws Exception {
		final StringRequestEntity requestEntity = new StringRequestEntity(
			      "{" +
			      "\"fromDate\": \"2005-08-01\"," +
				  "\"toDate\": \"2006-08-01\" " +
				  "      }" +
				  "}",
			    "application/json",
			    "UTF-8");
		
		final PostMethod mPost = createPostMethod(REPORT_ROOT_URL + "import",
				requestEntity);
		final String postOutput = mPost.getResponseBodyAsString();
		mPost.releaseConnection();
		System.out.println("response : " + postOutput);
		final ObjectMapper postMapper = new ObjectMapper();
		final GeneralSaveResponse response = postMapper.readValue(postOutput.getBytes(),
				GeneralSaveResponse.class);
		Assert.assertNotNull(response);
		
		final GetMethod mGet = createGetMethod(REPORT_ROOT_URL + "query/departmentIncomeReport",
				new String[] { "year:2006" });
		final String getOutput = mGet.getResponseBodyAsString();
		mGet.releaseConnection();
		System.out.println("response : " + new String(getOutput.getBytes("ISO-8859-1")));
		final ObjectMapper getMapper = new ObjectMapper();
		final QueryDepartmentIncomeResponse getResponse = getMapper.readValue(getOutput.getBytes(),
				QueryDepartmentIncomeResponse.class);
		Assert.assertNotNull(getResponse);
		Assert.assertEquals(2, getResponse.getDetail().size());
		//2005
		Assert.assertEquals(8, getResponse.getDetail().get(0).getDetail().size());
		
		/**
		 * Verify 2006 data
		 */
		Assert.assertEquals(8, getResponse.getDetail().get(1).getDetail().size());
		// verify department 1
		Assert.assertEquals(50000.0, getResponse.getDetail().get(1).getDetail().get(1).getRevenue().getAmount());
		Assert.assertEquals(6000.0, getResponse.getDetail().get(1).getDetail().get(1).getMargin().getAmount());
		// verify department 2
		Assert.assertEquals(100000.0, getResponse.getDetail().get(1).getDetail().get(2).getRevenue().getAmount());
		Assert.assertEquals(2000.0, getResponse.getDetail().get(1).getDetail().get(2).getMargin().getAmount());
	
	}
	
	@Test
	public void query2006DepartmentIncomeReportForNewVehicleDepartment() throws Exception {
		final StringRequestEntity requestEntity = new StringRequestEntity(
			      "{" +
			      "\"fromDate\": \"2005-08-01\"," +
				  "\"toDate\": \"2006-08-01\" " +
				  "      }" +
				  "}",
			    "application/json",
			    "UTF-8");
		
		final PostMethod mPost = createPostMethod(REPORT_ROOT_URL + "import",
				requestEntity);
		final String postOutput = mPost.getResponseBodyAsString();
		mPost.releaseConnection();
		System.out.println("response : " + postOutput);
		final ObjectMapper postMapper = new ObjectMapper();
		final GeneralSaveResponse response = postMapper.readValue(postOutput.getBytes(),
				GeneralSaveResponse.class);
		Assert.assertNotNull(response);
		
		final GetMethod mGet = createGetMethod(REPORT_ROOT_URL + "query/departmentIncomeReport",
				new String[] { "year:2006", "departmentID:1" });
		final String getOutput = mGet.getResponseBodyAsString();
		mGet.releaseConnection();
		System.out.println("response : " + new String(getOutput.getBytes("ISO-8859-1")));
		final ObjectMapper getMapper = new ObjectMapper();
		final QueryDepartmentIncomeResponse getResponse = getMapper.readValue(getOutput.getBytes(),
				QueryDepartmentIncomeResponse.class);
		Assert.assertNotNull(getResponse);
		Assert.assertEquals(2, getResponse.getDetail().size());
		//2005
		Assert.assertEquals(8, getResponse.getDetail().get(0).getDetail().size());
		
		/**
		 * Verify 2006 data
		 */
		Assert.assertEquals(8, getResponse.getDetail().get(1).getDetail().size());
		// verify department 1
		Assert.assertEquals(50000.0, getResponse.getDetail().get(1).getDetail().get(1).getRevenue().getAmount());
		Assert.assertEquals(6000.0, getResponse.getDetail().get(1).getDetail().get(1).getMargin().getAmount());
		// verify department 2
		Assert.assertEquals(0.0, getResponse.getDetail().get(1).getDetail().get(2).getRevenue().getAmount());
		Assert.assertEquals(0.0, getResponse.getDetail().get(1).getDetail().get(2).getMargin().getAmount());
	
	}
	
	@Test
	public void query2006DealerSalesReport() throws Exception {
		final StringRequestEntity requestEntity = new StringRequestEntity(
			      "{" +
			      "\"fromDate\": \"2005-08-01\"," +
				  "\"toDate\": \"2006-08-01\" " +
				  "      }" +
				  "}",
			    "application/json",
			    "UTF-8");
		
		final PostMethod mPost = createPostMethod(REPORT_ROOT_URL + "import",
				requestEntity);
		final String postOutput = mPost.getResponseBodyAsString();
		mPost.releaseConnection();
		System.out.println("response : " + postOutput);
		final ObjectMapper postMapper = new ObjectMapper();
		final GeneralSaveResponse response = postMapper.readValue(postOutput.getBytes(),
				GeneralSaveResponse.class);
		Assert.assertNotNull(response);
		
		final GetMethod mGet = createGetMethod(REPORT_ROOT_URL + "query/salesReport",
				new String[] { "year:2006" });
		final String getOutput = mGet.getResponseBodyAsString();
		mGet.releaseConnection();
		System.out.println("response : " + new String(getOutput.getBytes("ISO-8859-1")));
		final ObjectMapper getMapper = new ObjectMapper();
		final QueryDealerSalesResponse getResponse = getMapper.readValue(getOutput.getBytes(),
				QueryDealerSalesResponse.class);
		Assert.assertNotNull(getResponse);
		Assert.assertEquals(2, getResponse.getDetail().size());
		//2005
		Assert.assertEquals(20, getResponse.getDetail().get(0).getDetail().size());
		
		/**
		 * Verify 2006 data
		 */
		Assert.assertEquals(20, getResponse.getDetail().get(1).getDetail().size());
		// verify dealer 10
		Assert.assertEquals(5000.0, getResponse.getDetail().get(1).getDetail().get(9).getOverall().getAmount());
		Assert.assertEquals(0.0, getResponse.getDetail().get(1).getDetail().get(9).getRetail().getAmount());
		// verify dealer 11
		Assert.assertEquals(500.0, getResponse.getDetail().get(1).getDetail().get(10).getOverall().getAmount());
		Assert.assertEquals(500.0, getResponse.getDetail().get(1).getDetail().get(10).getRetail().getAmount());
	
	}
	
	
	@After
	public void tearDown() throws Exception {
		System.out.println("Clearing testing data");
		createVehicleSalesJournal(11, "2006-08-01", 0.0, 0.0, 0);
		createVehicleSalesJournal(11, "2005-08-01", 0.0, 0.0, 0);
		
		createSalesServiceJournal(10, "2006-07-01", 0.0, 0.0, 0);
		createSalesServiceJournal(10, "2005-07-01", 0.0, 0.0, 0);

		createGeneralJournal(12, "2006-05-01", 0.0);
		createGeneralJournal(12, "2005-05-01", 0.0);
		
		createTaxJournal(13, "2006-04-01", 0.0);
		createTaxJournal(13, "2005-04-01", 0.0);
		
		this.createAccountReceivableDuration(9, "2006-06-01", 0.0);
		this.createAccountReceivableDuration(9, "2005-06-01", 0.0);
		
		this.createInventoryDuration(8, "2006-06-01", 0.0);
		this.createInventoryDuration(8, "2005-06-01", 0.0);
		
		this.createEmployeeFeeJournal(7, "2006-06-01", 0.0);
		this.createEmployeeFeeJournal(7, "2005-06-01", 0.0);
		
		this.createEmployeeFeeSummaryJournal(6, "2006-06-01", 0.0);
		this.createEmployeeFeeSummaryJournal(6, "2005-06-01", 0.0);
		
		this.createHumanResourceAllocation(6, "2006-06-01", 0.0);
		this.createHumanResourceAllocation(6, "2005-06-01", 0.0);
		
		final StringRequestEntity requestEntity = new StringRequestEntity(
			      "{" +
			      "\"fromDate\": \"2005-08-01\"," +
				  "\"toDate\": \"2006-08-01\" " +
				  "      }" +
				  "}",
			    "application/json",
			    "UTF-8");
		
		final PostMethod mPost = createPostMethod(REPORT_ROOT_URL + "import",
				requestEntity);
		final String postOutput = mPost.getResponseBodyAsString();
		mPost.releaseConnection();
		System.out.println("response : " + postOutput);
	}
	private void createVehicleSalesJournal(final Integer dealerID, final String validDate, final Double amount, final Double margin, final Integer count) throws Exception {
		final StringRequestEntity requestEntity = new StringRequestEntity(
			      "{\"dealerID\": " + dealerID + "," +
			      "\"departmentID\": 1, " +
				  "\"validDate\": \"" + validDate + "\"," +
				  "\"updateBy\": \"chenkai\", " +
				  "\"detail\": " +
				  "  [" +
				  "     {" +
				  "       \"vehicleID\": 2," + 
				  "       \"amount\": " + amount + ", " + 
				  "       \"margin\": " + margin + ", " + 
				  "       \"count\": " + count +
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
	}
	
	private void createSalesServiceJournal(final Integer dealerID, final String validDate, final Double amount, final Double margin, final Integer count) throws Exception {
		final StringRequestEntity requestEntity = new StringRequestEntity(
			      "{\"dealerID\": " + dealerID + "," +
			      "\"departmentID\": 2, " +
				  "\"validDate\": \"" + validDate + "\"," +
				  "\"updateBy\": \"chenkai\", " +
				  "\"detail\": " +
				  "  [" +
				  "     {" +
				  "       \"itemID\": 2," + 
				  "       \"amount\": " + amount + ", " + 
				  "       \"margin\": " + margin + ", " + 
				  "       \"count\": " + count +
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
	}
	
	private void createGeneralJournal(final Integer dealerID, final String validDate, final Double amount) throws Exception {
		final StringRequestEntity requestEntity = new StringRequestEntity(
			      "{\"dealerID\": 8," +
			      "\"departmentID\": 3, " +
				  "\"validDate\": \"2006-06-01\"," +
				  "\"updateBy\": \"chenkai\", " +
				  "\"detail\": " +
				  "  [" +
				  "     {" +
				  "       \"itemID\": 2," + 
				  "       \"amount\": " + amount + 
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
	}
	
	private void createTaxJournal(final Integer dealerID, final String validDate, final Double amount) throws Exception {
		final StringRequestEntity requestEntity = new StringRequestEntity(
			      "{\"dealerID\": 8," +
			      "\"validDate\": \"2006-06-01\"," +
			      "\"tax\": " + amount + ", " +
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
		
	}
	
	private void createEmployeeFeeJournal(final Integer dealerID, final String validDate, final Double amount) throws Exception {
		final StringRequestEntity requestEntity = new StringRequestEntity(
			      "{\"dealerID\": 9," +
			      "\"departmentID\": 6, " +
				  "\"validDate\": \"2005-08-01\"," +
				  "\"updateBy\": \"chenkai\", " +
				  "\"detail\": " +
				  "  [" +
				  "     {" +
				  "       \"itemID\": 2," + 
				  "       \"amount\": " + amount + ", " +
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
		
	}
	
	private void createAccountReceivableDuration(final Integer dealerID, final String validDate, final Double amount) throws Exception {
		final StringRequestEntity requestEntity = new StringRequestEntity(
			      "{\"dealerID\": 9," +
			      "\"validDate\": \"2005-08-01\"," +
				  "\"updateBy\": \"chenkai\", " +
				  "\"detail\": " +
				  "  [" +
				  "     {" +
				  "       \"itemID\": 2," + 
				  "       \"amount\": " + amount + ", " +
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
	}
	
	private void createInventoryDuration(final Integer dealerID, final String validDate, final Double amount) throws Exception {
		final StringRequestEntity requestEntity = new StringRequestEntity(
			      "{\"dealerID\": 9," +
			      "\"validDate\": \"2005-08-01\"," +
			      "\"updateBy\": \"chenkai\", " +
				  "\"departmentID\": 2, " +
				  "\"detail\": " +
				  "  [" +
				  "     {" +
				  "       \"itemID\": 2," + 
				  "       \"amount\": " + amount + ", " +
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
	}
	
	private void createEmployeeFeeSummaryJournal(final Integer dealerID, final String validDate, final Double amount) throws Exception {
		final StringRequestEntity requestEntity = new StringRequestEntity(
			      "{\"dealerID\": 9," +
			      "\"departmentID\": 6, " +
				  "\"validDate\": \"2005-08-01\"," +
				  "\"updateBy\": \"chenkai\", " +
				  "\"detail\": " +
				  "  [" +
				  "     {" +
				  "       \"itemID\": 2," + 
				  "       \"amount\": " + amount +
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
	}
	
	private void createHumanResourceAllocation(final Integer dealerID, final String validDate, final Double amount) throws Exception {
		final StringRequestEntity requestEntity = new StringRequestEntity(
			      "{\"dealerID\": 9," +
			      "\"departmentID\":6," +
				  "\"validDate\": \"2005-08-01\"," +
				  "\"updateBy\": \"chenkai\", " +
				  "\"detail\": " +
				  "  [" +
				  "     {" +
				  "       \"itemID\": 2," + 
				  "       \"allocation\": " + amount +
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
	}
	
}
