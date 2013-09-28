package com.jdc.themis.dealer.service.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jdc.themis.dealer.service.DealerIncomeReportService;
import com.jdc.themis.dealer.utils.RestServiceErrorHandler;
import com.jdc.themis.dealer.utils.Utils;
import com.jdc.themis.dealer.web.domain.GeneralResponse;
import com.jdc.themis.dealer.web.domain.ImportReportDataRequest;

import fj.data.Option;

@Service
public class DealerReportRestService {

	@Autowired
	private DealerIncomeReportService dealerIncomeReportService;
	
	public void setDealerIncomeReportService(
			DealerIncomeReportService dealerIncomeReportService) {
		this.dealerIncomeReportService = dealerIncomeReportService;
	}

	/**
	 * Import report data for given year and month.
	 * 
	 * @param request
	 * @return
	 */
	@POST
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	@Path("/import")
	@RestServiceErrorHandler
	public Response importReportData(final ImportReportDataRequest request) {
		final GeneralResponse response = new GeneralResponse();
		response.setErrorMsg("");
		response.setSuccess(true);
		dealerIncomeReportService
			.importReportData(request);
		response.setTimestamp(Utils.currentTimestamp());
		return Response.ok(response).status(Status.CREATED).build();
	}

	/**
	 * Query overall income report.
	 * 
	 * @param year
	 * @return
	 */
	@GET
	@Path("/query/overallIncomeReport")
	@Produces({ "application/json", "application/xml" })
	@RestServiceErrorHandler
	public Response queryDealerOverallIncomeReport(
			@QueryParam("year") Integer year, 
			@QueryParam("monthOfYear") Integer monthOfYear, 
			@QueryParam("departmentID") Integer departmentID, 
			@QueryParam("denominator") Integer denominator) {
		return Response.ok(
				dealerIncomeReportService.queryOverallIncomeReport(
						year, 
						Option.fromNull(monthOfYear), 
						Option.fromNull(departmentID),
						Option.fromNull(denominator))).build();
	}
	
	/**
	 * Query department income report.
	 * 
	 * @param year
	 * @param dealer id
	 * @return
	 */
	@GET
	@Path("/query/departmentIncomeReport")
	@Produces({ "application/json", "application/xml" })
	@RestServiceErrorHandler
	public Response queryDealerDepartmentIncomeReport(
			@QueryParam("year") Integer year, 
			@QueryParam("monthOfYear") Integer monthOfYear, 
			@QueryParam("dealerID") Integer dealerID) {
		return Response.ok(
				dealerIncomeReportService.queryDepartmentIncomeReport(
						year, 
						Option.fromNull(dealerID), 
						Option.fromNull(monthOfYear))).build();
	}
}
