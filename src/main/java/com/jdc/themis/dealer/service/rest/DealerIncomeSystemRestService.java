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

import com.jdc.themis.dealer.service.DealerIncomeEntryService;
import com.jdc.themis.dealer.service.RefDataQueryService;
import com.jdc.themis.dealer.utils.RestServiceErrorHandler;
import com.jdc.themis.dealer.web.domain.GeneralResponse;
import com.jdc.themis.dealer.web.domain.SaveAccountReceivableDurationRequest;
import com.jdc.themis.dealer.web.domain.SaveDealerEntryItemStatusRequest;
import com.jdc.themis.dealer.web.domain.SaveEmployeeFeeRequest;
import com.jdc.themis.dealer.web.domain.SaveEmployeeFeeSummaryRequest;
import com.jdc.themis.dealer.web.domain.SaveGeneralJournalRequest;
import com.jdc.themis.dealer.web.domain.SaveHumanResourceAllocationRequest;
import com.jdc.themis.dealer.web.domain.SaveInventoryDurationRequest;
import com.jdc.themis.dealer.web.domain.SaveSalesServiceRevenueRequest;
import com.jdc.themis.dealer.web.domain.SaveTaxRequest;
import com.jdc.themis.dealer.web.domain.SaveVehicleSalesJournalRequest;

import fj.data.Option;

/**
 * Main entry to dealer income entry system.
 * 
 * @author Kai Chen
 * 
 */
@Service
public class DealerIncomeSystemRestService {

	@Autowired
	private RefDataQueryService refDataQueryService;

	@Autowired
	private DealerIncomeEntryService dealerIncomeEntryService;
	
	public void setRefDataQueryService(final RefDataQueryService refDataQueryService) {
		this.refDataQueryService = refDataQueryService;
	}

	/**
	 * Get full list of menu. Each menu includes its parent id and child id
	 * list.
	 * 
	 * @return
	 */
	@GET
	@Path("/menu")
	@Produces({ "application/json", "application/xml" })
	@RestServiceErrorHandler
	public Response getMenu() {
		return Response.ok(this.refDataQueryService.getMenu()).build();
	}
	
	/**
	 * Get all deparment details.
	 * 
	 * @return
	 */
	@GET
	@Path("/list")
	@Produces({ "application/json", "application/xml" })
	@RestServiceErrorHandler
	public Response getDealers() {
		return Response.ok(this.refDataQueryService.getDealers()).build();
	}
	
	/**
	 * Get all deparment details.
	 * 
	 * @return
	 */
	@GET
	@Path("/department")
	@Produces({ "application/json", "application/xml" })
	@RestServiceErrorHandler
	public Response getDepartments() {
		return Response.ok(this.refDataQueryService.getDepartments()).build();
	}

	/**
	 * Get full list of vehicles.
	 * 
	 * @return
	 */
	@GET
	@Path("/vehicle")
	@Produces({ "application/json", "application/xml" })
	@RestServiceErrorHandler
	public Response getVehicles(@QueryParam("categoryID") Integer categoryID) {
		return Response.ok(
				this.refDataQueryService.getVehicles(Option.<Integer>fromNull(categoryID))).build();
	}

	/**
	 * Get full list of sales & service revenue items.
	 * 
	 * @return
	 */
	@GET
	@Path("/salesServiceRevenue/items")
	@Produces({ "application/json", "application/xml" })
	@RestServiceErrorHandler
	public Response getSalesServiceRevenueItems(@QueryParam("categoryID") Integer categoryID) {
		return Response.ok(
				this.refDataQueryService.getSalesServiceRevenueItems(Option.fromNull(categoryID)))
				.build();
	}

	/**
	 * Save a list of vehicle sales revenue.
	 * 
	 * @param request
	 * @return
	 */
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/vehicleSalesRevenue")
	@RestServiceErrorHandler
	public Response saveVehicleSalesRevenue(
			final SaveVehicleSalesJournalRequest request) {
		final GeneralResponse response = new GeneralResponse();
		response.setErrorMsg("");
		response.setSuccess(true);
		response.setTimestamp(dealerIncomeEntryService
				.saveVehicleSalesRevenue(request));
		return Response.ok(response).status(Status.CREATED).build();
	}

	/**
	 * Get a list of vehicle sales revenue per vehicle type.
	 * 
	 * @param dealerID
	 *            Dealer company id
	 * @param validDate
	 *            Date to query report
	 * @return
	 */
	@GET
	@Path("/vehicleSalesRevenue")
	@Produces({ "application/json", "application/xml" })
	@RestServiceErrorHandler
	public Response getVehicleSalesRevenue(
			@QueryParam("dealerID") Integer dealerID,
			@QueryParam("departmentID") Integer departmentID,
			@QueryParam("validDate") String validDate, 
			@QueryParam("categoryID") Integer categoryID) {
		return Response.ok(
				dealerIncomeEntryService.getVehicleSalesRevenue(
						dealerID,
						Option.fromNull(departmentID), 
						validDate, 
						Option.fromNull(categoryID))).build();
	}

	/**
	 * Save a list of sales & service revenue.
	 * 
	 * @param request
	 * @return
	 */
	@POST
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	@Path("/salesServiceRevenue")
	@RestServiceErrorHandler
	public Response saveSalesServiceRevenue(
			final SaveSalesServiceRevenueRequest request) {
		final GeneralResponse response = new GeneralResponse();
		response.setErrorMsg("");
		response.setSuccess(true);
		response.setTimestamp(dealerIncomeEntryService
				.saveSalesServiceRevenue(request));
		return Response.ok(response).status(Status.CREATED).build();
	}

	/**
	 * Get a list sales & service revenue per sales item.
	 * 
	 * @param dealerID
	 *            Dealer company id
	 * @param departmentID
	 *            Department id
	 * @param validDate
	 *            Date to query report
	 * @return
	 */
	@GET
	@Path("/salesServiceRevenue")
	@Produces({ "application/json", "application/xml" })
	@RestServiceErrorHandler
	public Response getSalesServiceRevenue(
			@QueryParam("dealerID") Integer dealerID,
			@QueryParam("departmentID") Integer departmentID,
			@QueryParam("validDate") String validDate, 
			@QueryParam("categoryID") Integer categoryID) {
		return Response.ok(
				dealerIncomeEntryService.getSalesServiceRevenue(dealerID,
						departmentID, validDate, Option.fromNull(categoryID))).build();
	}

	/**
	 * Save income tax.
	 * 
	 * @param request
	 * @return
	 */
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/tax")
	@RestServiceErrorHandler
	public Response saveIncomeTax(final SaveTaxRequest request) {
		final GeneralResponse response = new GeneralResponse();
		response.setErrorMsg("");
		response.setSuccess(true);
		response.setTimestamp(dealerIncomeEntryService.saveIncomeTax(request));
		return Response.ok(response).status(Status.CREATED).build();
	}

	/**
	 * Get a list sales & service revenue per sales item.
	 * 
	 * @param dealerID
	 *            Dealer company id
	 * @param departmentID
	 *            Department id
	 * @param validDate
	 *            Date to query report
	 * @return
	 */
	@GET
	@Path("/tax")
	@Produces({ "application/json", "application/xml" })
	@RestServiceErrorHandler
	public Response getIncomeTax(@QueryParam("dealerID") Integer dealerID,
			@QueryParam("validDate") String validDate) {
		return Response.ok(
				dealerIncomeEntryService.getIncomeTax(dealerID, validDate))
				.build();
	}

	/**
	 * Save dealer entry item status.
	 * 
	 * @param request
	 * @return
	 */
	@POST
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	@Path("/menu/entrystatus")
	@RestServiceErrorHandler
	public Response saveDealerEntryItemStatus(
			final SaveDealerEntryItemStatusRequest request) {
		final GeneralResponse response = new GeneralResponse();
		response.setErrorMsg("");
		response.setSuccess(true);
		response.setTimestamp(dealerIncomeEntryService
				.saveDealerEntryItemStatus(request));
		return Response.ok(response).status(Status.CREATED).build();

	}

	/**
	 * Get dealer entry item status.
	 * 
	 * @param dealerID
	 *            Dealer company id
	 * @param validDate
	 *            Date to query report
	 * @return
	 */
	@GET
	@Path("/menu/entrystatus")
	@Produces({ "application/json", "application/xml" })
	@RestServiceErrorHandler
	public Response getDealerEntryItemStatus(
			@QueryParam("dealerID") Integer dealerID,
			@QueryParam("validDate") String validDate) {
		return Response.ok(
				dealerIncomeEntryService.getDealerEntryItemStatus(dealerID,
						validDate)).build();
	}

	/**
	 * Get full list of general income items.
	 * 
	 * @return
	 */
	@GET
	@Path("/generalJournal/items")
	@Produces({ "application/json", "application/xml" })
	@RestServiceErrorHandler
	public Response getGeneralIncomeItems(@QueryParam("categoryID") Integer categoryID) {
		return Response.ok(this.refDataQueryService.getGeneralIncomeItems(
				Option.fromNull(categoryID)))
				.build();
	}

	/**
	 * Save a list of general income journals.
	 * 
	 * @param request
	 * @return
	 */
	@POST
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	@Path("/generalJournal")
	@RestServiceErrorHandler
	public Response saveGeneralIncome(final SaveGeneralJournalRequest request) {
		final GeneralResponse response = new GeneralResponse();
		response.setErrorMsg("");
		response.setSuccess(true);
		response.setTimestamp(dealerIncomeEntryService
				.saveGeneralIncome(request));
		return Response.ok(response).status(Status.CREATED).build();

	}

	/**
	 * Get a list general income journal per income item.
	 * 
	 * @param dealerID
	 *            Dealer company id
	 * @param departmentID
	 *            Department id
	 * @param validDate
	 *            Date to query report
	 * @return
	 */
	@GET
	@Path("/generalJournal")
	@Produces({ "application/json", "application/xml" })
	@RestServiceErrorHandler
	public Response getGeneralIncome(@QueryParam("dealerID") Integer dealerID,
			@QueryParam("departmentID") Integer departmentID,
			@QueryParam("validDate") String validDate, 
			@QueryParam("categoryID") Integer categoryID) {
		return Response.ok(
				dealerIncomeEntryService.getGeneralIncome(dealerID,
						departmentID, validDate, Option.fromNull(categoryID))).build();
	}
	
	/**
	 * Save a list of account receivable duration.
	 * 
	 * @param request
	 * @return
	 */
	@POST
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	@Path("/accountReceivable/duration")
	@RestServiceErrorHandler
	public Response saveAccountReceivableDuration(final SaveAccountReceivableDurationRequest request) {
		final GeneralResponse response = new GeneralResponse();
		response.setErrorMsg("");
		response.setSuccess(true);
		response.setTimestamp(dealerIncomeEntryService
				.saveAccountReceivableDuration(request));
		return Response.ok(response).status(Status.CREATED).build();
	}

	/**
	 * Get a list account receivable duration.
	 * 
	 * @param dealerID
	 *            Dealer company id
	 * @param validDate
	 *            Date to query report
	 * @return
	 */
	@GET
	@Path("/accountReceivable/duration")
	@Produces({ "application/json", "application/xml" })
	@RestServiceErrorHandler
	public Response getAccountReceivableDuration(@QueryParam("dealerID") Integer dealerID,
			@QueryParam("validDate") String validDate) {
		return Response.ok(
				dealerIncomeEntryService.getAccountReceivableDuration(dealerID,
						validDate)).build();
	}
	
	/**
	 * Get a list account receivable duration items.
	 * 
	 * @return
	 */
	@GET
	@Path("/accountReceivable/duration/items")
	@Produces({ "application/json", "application/xml" })
	@RestServiceErrorHandler
	public Response getAccountReceivableDurationItems() {
		return Response.ok(this.refDataQueryService.getAccountReceivableDurationItems())
				.build();
	}

	/**
	 * Save a list of inventory duration.
	 * 
	 * @param request
	 * @return
	 */
	@POST
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	@Path("/inventory/duration")
	@RestServiceErrorHandler
	public Response saveInventoryDuration(final SaveInventoryDurationRequest request) {
		final GeneralResponse response = new GeneralResponse();
		response.setErrorMsg("");
		response.setSuccess(true);
		response.setTimestamp(dealerIncomeEntryService
				.saveInventoryDuration(request));
		return Response.ok(response).status(Status.CREATED).build();
	}

	/**
	 * Get a list inventory duration.
	 * 
	 * @param dealerID
	 *            Dealer company id
	 * @param validDate
	 *            Date to query report
	 * @return
	 */
	@GET
	@Path("/inventory/duration")
	@Produces({ "application/json", "application/xml" })
	@RestServiceErrorHandler
	public Response getInventoryDuration(@QueryParam("dealerID") Integer dealerID,
			@QueryParam("departmentID") Integer departmentID,
			@QueryParam("validDate") String validDate) {
		return Response.ok(
				dealerIncomeEntryService.getInventoryDuration(dealerID, departmentID,
						validDate)).build();
	}
	
	/**
	 * Get a list inventory duration items.
	 * 
	 * @return
	 */
	@GET
	@Path("/inventory/duration/items")
	@Produces({ "application/json", "application/xml" })
	@RestServiceErrorHandler
	public Response getInventoryDurationItems() {
		return Response.ok(this.refDataQueryService.getInventoryDurationItems())
				.build();
	}
	
	/**
	 * Save a list of employee fee.
	 * 
	 * @param request
	 * @return
	 */
	@POST
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	@Path("/employee/fee")
	@RestServiceErrorHandler
	public Response saveEmployeeFee(final SaveEmployeeFeeRequest request) {
		final GeneralResponse response = new GeneralResponse();
		response.setErrorMsg("");
		response.setSuccess(true);
		response.setTimestamp(dealerIncomeEntryService
				.saveEmployeeFee(request));
		return Response.ok(response).status(Status.CREATED).build();

	}

	/**
	 * Get a list employee fee.
	 * 
	 * @param dealerID
	 *            Dealer company id
	 * @param departmentID
	 *            Department id
	 * @param validDate
	 *            Date to query report
	 * @return
	 */
	@GET
	@Path("/employee/fee")
	@Produces({ "application/json", "application/xml" })
	@RestServiceErrorHandler
	public Response getEmployeeFee(@QueryParam("dealerID") Integer dealerID,
			@QueryParam("departmentID") Integer departmentID,
			@QueryParam("validDate") String validDate) {
		return Response.ok(
				dealerIncomeEntryService.getEmployeeFee(dealerID,
						departmentID, validDate)).build();
	}
	
	/**
	 * Get a list inventory duration items.
	 * 
	 * @return
	 */
	@GET
	@Path("/employee/fee/items")
	@Produces({ "application/json", "application/xml" })
	@RestServiceErrorHandler
	public Response getEmployeeFeeItems() {
		return Response.ok(this.refDataQueryService.getEmployeeFeeItems())
				.build();
	}
	
	/**
	 * Save a list of employee fee summary.
	 * 
	 * @param request
	 * @return
	 */
	@POST
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	@Path("/employee/feeSummary")
	@RestServiceErrorHandler
	public Response saveEmployeeFeeSummary(final SaveEmployeeFeeSummaryRequest request) {
		final GeneralResponse response = new GeneralResponse();
		response.setErrorMsg("");
		response.setSuccess(true);
		response.setTimestamp(dealerIncomeEntryService
				.saveEmployeeFeeSummary(request));
		return Response.ok(response).status(Status.CREATED).build();

	}

	/**
	 * Get a list employee fee summary.
	 * 
	 * @param dealerID
	 *            Dealer company id
	 * @param departmentID
	 *            Department id
	 * @param validDate
	 *            Date to query report
	 * @return
	 */
	@GET
	@Path("/employee/feeSummary")
	@Produces({ "application/json", "application/xml" })
	@RestServiceErrorHandler
	public Response getEmployeeFeeSummary(@QueryParam("dealerID") Integer dealerID,
			@QueryParam("departmentID") Integer departmentID,
			@QueryParam("validDate") String validDate) {
		return Response.ok(
				dealerIncomeEntryService.getEmployeeFeeSummary(dealerID,
						departmentID, validDate)).build();
	}
	
	/**
	 * Get a list inventory duration items.
	 * 
	 * @return
	 */
	@GET
	@Path("/employee/feeSummary/items")
	@Produces({ "application/json", "application/xml" })
	@RestServiceErrorHandler
	public Response getEmployeeFeeSummaryItems() {
		return Response.ok(this.refDataQueryService.getEmployeeFeeSummaryItems())
				.build();
	}
	
	/**
	 * Save a list of human resource allocations.
	 * 
	 * @param request
	 * @return
	 */
	@POST
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	@Path("/hr/allocation")
	@RestServiceErrorHandler
	public Response saveHumanResourceAllocation(final SaveHumanResourceAllocationRequest request) {
		final GeneralResponse response = new GeneralResponse();
		response.setErrorMsg("");
		response.setSuccess(true);
		response.setTimestamp(dealerIncomeEntryService
				.saveHumanResourceAllocation(request));
		return Response.ok(response).status(Status.CREATED).build();

	}

	/**
	 * Get a list human resource allocations.
	 * 
	 * @param dealerID
	 *            Dealer company id
	 * @param validDate
	 *            Date to query report
	 * @return
	 */
	@GET
	@Path("/hr/allocation")
	@Produces({ "application/json", "application/xml" })
	@RestServiceErrorHandler
	public Response getHumanResourceAllocation(@QueryParam("dealerID") Integer dealerID,
			@QueryParam("validDate") String validDate) {
		return Response.ok(
				dealerIncomeEntryService.getHumanResourceAllocation(dealerID, validDate)).build();
	}
	
	/**
	 * Get a list inventory duration items.
	 * 
	 * @return
	 */
	@GET
	@Path("/hr/allocation/items")
	@Produces({ "application/json", "application/xml" })
	@RestServiceErrorHandler
	public Response getHumanResourceAllocationItems() {
		return Response.ok(this.refDataQueryService.getHumanResourceAllocationItems())
				.build();
	}
	
}
