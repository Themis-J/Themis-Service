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
import com.jdc.themis.dealer.web.domain.SaveDealerEntryItemStatusRequest;
import com.jdc.themis.dealer.web.domain.SaveGeneralJournalRequest;
import com.jdc.themis.dealer.web.domain.SaveSalesServiceRevenueRequest;
import com.jdc.themis.dealer.web.domain.SaveTaxRequest;
import com.jdc.themis.dealer.web.domain.SaveVehicleSalesJournalRequest;

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
	public Response getAllMenu() {
		return Response.ok(this.refDataQueryService.getAllMenu()).build();
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
	public Response getAllVehicles() {
		return Response.ok(this.refDataQueryService.getAllVehicles()).build();
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
	public Response getAllSalesServiceRevenueItems() {
		return Response.ok(
				this.refDataQueryService.getAllSalesServiceRevenueItems())
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
			@QueryParam("validDate") String validDate) {
		return Response.ok(
				dealerIncomeEntryService.getVehicleSalesRevenue(dealerID,
						departmentID, validDate)).build();
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
			@QueryParam("validDate") String validDate) {
		return Response.ok(
				dealerIncomeEntryService.getSalesServiceRevenue(dealerID,
						departmentID, validDate)).build();
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
	@Path("/generalIncome/items")
	@Produces({ "application/json", "application/xml" })
	@RestServiceErrorHandler
	public Response getAllGeneralIncomeItems() {
		return Response.ok(this.refDataQueryService.getAllGeneralIncomeItems())
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
			@QueryParam("validDate") String validDate) {
		return Response.ok(
				dealerIncomeEntryService.getGeneralIncome(dealerID,
						departmentID, validDate)).build();
	}

}
