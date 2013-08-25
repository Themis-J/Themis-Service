package com.jdc.themis.dealer.service.rest;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.time.Instant;
import javax.time.calendar.LocalDate;
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
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jdc.themis.dealer.data.dao.IncomeJournalDAO;
import com.jdc.themis.dealer.data.dao.RefDataDAO;
import com.jdc.themis.dealer.domain.DealerEntryItemStatus;
import com.jdc.themis.dealer.domain.Menu;
import com.jdc.themis.dealer.domain.MenuHierachy;
import com.jdc.themis.dealer.domain.SalesServiceJournal;
import com.jdc.themis.dealer.domain.SalesServiceJournalCategory;
import com.jdc.themis.dealer.domain.SalesServiceJournalItem;
import com.jdc.themis.dealer.domain.TaxJournal;
import com.jdc.themis.dealer.domain.Vehicle;
import com.jdc.themis.dealer.domain.VehicleSalesJournal;
import com.jdc.themis.dealer.utils.Performance;
import com.jdc.themis.dealer.web.domain.CompletedEntryItem;
import com.jdc.themis.dealer.web.domain.GeneralSaveResponse;
import com.jdc.themis.dealer.web.domain.GetDealerEntryItemStatusResponse;
import com.jdc.themis.dealer.web.domain.GetMenuResponse;
import com.jdc.themis.dealer.web.domain.GetSalesServiceRevenueItemResponse;
import com.jdc.themis.dealer.web.domain.GetSalesServiceRevenueResponse;
import com.jdc.themis.dealer.web.domain.GetTaxResponse;
import com.jdc.themis.dealer.web.domain.GetVehicleResponse;
import com.jdc.themis.dealer.web.domain.GetVehicleSalesRevenueResponse;
import com.jdc.themis.dealer.web.domain.MenuItem;
import com.jdc.themis.dealer.web.domain.MenuOrderItem;
import com.jdc.themis.dealer.web.domain.SalesServiceRevenueDetail;
import com.jdc.themis.dealer.web.domain.SalesServiceRevenueItem;
import com.jdc.themis.dealer.web.domain.SaveDealerEntryItemStatusRequest;
import com.jdc.themis.dealer.web.domain.SaveSalesServiceRevenueRequest;
import com.jdc.themis.dealer.web.domain.SaveTaxRequest;
import com.jdc.themis.dealer.web.domain.SaveVehicleSalesRevenueRequest;
import com.jdc.themis.dealer.web.domain.VehicleItem;
import com.jdc.themis.dealer.web.domain.VehicleSalesRevenueDetail;

/**
 * Main entry to dealer income entry system.
 * 
 * @author Kai Chen
 *
 */
@Service
public class DealerIncomeEntryRestService {

	@Autowired
	private RefDataDAO refDataDAL;
	
	@Autowired
	private IncomeJournalDAO incomeJournalDAL;

	public IncomeJournalDAO getIncomeJournalDAL() {
		return incomeJournalDAL;
	}

	public void setIncomeJournalDAL(IncomeJournalDAO incomeJournalDAL) {
		this.incomeJournalDAL = incomeJournalDAL;
	}

	public RefDataDAO getRefDataDAL() {
		return refDataDAL;
	}

	public void setRefDataDAL(RefDataDAO refDataDAL) {
		this.refDataDAL = refDataDAL;
	}

	/**
	 * Get menu item details by id. 
	 * 
	 * @param id
	 * @return
	 */
	private MenuItem getMenuByID(Integer id) {
		final Menu menu = refDataDAL.getMenu(id);
		final MenuItem item = new MenuItem();
		item.setId(menu.getId());
		item.setName(menu.getName());
		item.setDisplayText(menu.getDisplayText());
		item.setParentID(refDataDAL.getParentMenuID(id));
		
		final Collection<MenuHierachy> children = refDataDAL.getChildMenus(id);
		if ( children != null ) {
			for (final MenuHierachy child : children) {
				item.getChildren().add(
						new MenuOrderItem(
								child.getMenuHierachyID().getChildID(), 
								refDataDAL.getMenu(child.getMenuHierachyID().getChildID()).getName(), 
								child.getItemOrder()));
			}
		}
		return item;
	}
	
	/* Services */
	
	/**
	 * Get full list of menu. 
	 * Each menu includes its parent id and child id list.
	 * 
	 * @return
	 */
	@GET
	@Path("/menu")
	@Produces({ "application/json", "application/xml" })
	@Transactional(readOnly = true)
	@Performance
	public Response getAllMenu() {
		final GetMenuResponse response = new GetMenuResponse();

		for (final Menu menu : refDataDAL.getMenuList()) {
			final MenuItem item = getMenuByID(menu.getId());
			response.getItems().add(item);
		}
		return Response.ok(response).build();
	}
	private enum GetCategoryIDFunction implements Function<SalesServiceJournalCategory, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(SalesServiceJournalCategory item) {
	        return item.getId();
	    }
	}
	private String getSalesCategory(Integer id) {
		return Maps.uniqueIndex(refDataDAL.getSalesServiceJournalCategoryList(), GetCategoryIDFunction.INSTANCE).get(id).getName();
	}
	/**
	 * Get full list of vehicles. 
	 * 
	 * @return
	 */
	@GET
	@Path("/vehicle")
	@Produces({ "application/json", "application/xml" })
	@Transactional(readOnly = true)
	@Performance
	public Response getAllVehicles() {
		final GetVehicleResponse response = new GetVehicleResponse();

		for (final Vehicle vehicle : refDataDAL.getVehicleList()) {
			final VehicleItem item = new VehicleItem();
			item.setId(vehicle.getId());
			item.setName(vehicle.getName());
			item.setCategory(getSalesCategory(vehicle.getCategoryID()));
			item.setTimestamp(vehicle.getTimestamp());
			response.getItems().add(item);
		}
		return Response.ok(response).build();
	}
	
	/**
	 * Get full list of sales & service revenue items. 
	 * 
	 * @return
	 */
	@GET
	@Path("/salesServiceRevenue/items")
	@Produces({ "application/json", "application/xml" })
	@Transactional(readOnly = true)
	@Performance
	public Response getAllSalesServiceRevenueItems() {
		final GetSalesServiceRevenueItemResponse response = new GetSalesServiceRevenueItemResponse();

		for (final SalesServiceJournalItem ssj : refDataDAL.getSalesServiceJournalItemList()) {
			final SalesServiceRevenueItem item = new SalesServiceRevenueItem();
			item.setId(ssj.getId());
			item.setName(ssj.getName());
			response.getItems().add(item);
		}
		return Response.ok(response).build();
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
	@Transactional
	@Performance
	public Response saveVehicleSalesRevenue(
			final SaveVehicleSalesRevenueRequest request) {
		try {
			Preconditions.checkNotNull(request.getDealerID(), "dealer id can't be null");
			Preconditions.checkNotNull(request.getValidDate(), "valid date can't be null");
			Preconditions.checkNotNull(request.getDetail().size() == 0, "no detail is posted");
			
			final List<VehicleSalesJournal> journals = Lists.newArrayList();
			final Integer requestedDeparmentID = request.getDepartmentID() == null ? DEFAULT_VEHICLE_DEPARTMENT_ID : request.getDepartmentID();
			for ( final VehicleSalesRevenueDetail detail : request.getDetail() ) {
				final VehicleSalesJournal journal = new VehicleSalesJournal();
				journal.setAmount(new BigDecimal(detail.getAmount()));
				journal.setMargin(new BigDecimal(detail.getMargin()));
				journal.setCount(detail.getCount());
				journal.setDealerID(request.getDealerID());
				Preconditions.checkNotNull(detail.getVehicleID(), "vehicle id can't be null");
				journal.setId(detail.getVehicleID());
				journal.setDepartmentID(requestedDeparmentID);
				journal.setUpdatedBy(request.getUpdateBy());
				journal.setValidDate(LocalDate.parse(request.getValidDate()));
				journals.add(journal);
				
			} 
			final Instant timestamp = incomeJournalDAL.saveVehicleSalesJournal(request.getDealerID(), requestedDeparmentID, journals);
			if ( timestamp == null ) {
				throw new RuntimeException("Database save returns null timestamp!");
			}
			final GeneralSaveResponse response = new GeneralSaveResponse();
			response.setErrorMsg("");
			response.setSuccess(true);
			response.setTimestamp(timestamp);
			return Response.ok(response).status(Status.CREATED).build();
		} catch (Exception e) {
			final GeneralSaveResponse response = new GeneralSaveResponse();
			response.setErrorMsg(e.getMessage());
			response.setSuccess(false);
			response.setTimestamp(Instant.millis(new Date().getTime()));
			return Response.ok(response).status(Status.BAD_REQUEST).build();
		} 
	}

	private final static Integer DEFAULT_VEHICLE_DEPARTMENT_ID = 1; // new vehicle department
	private enum GetVehicleIDFunction implements Function<Vehicle, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(Vehicle item) {
	        return item.getId();
	    }
	}
	private String getVehicleName(Integer id) {
		return Maps.uniqueIndex(refDataDAL.getVehicleList(), GetVehicleIDFunction.INSTANCE).get(id).getName();
	}
	/**
	 * Get a list of vehicle sales revenue per vehicle type. 
	 * 
	 * @param dealerID Dealer company id
	 * @param validDate Date to query report
	 * @return
	 */
	@GET
	@Path("/vehicleSalesRevenue")
	@Produces({ "application/json", "application/xml" })
	@Transactional
	@Performance
	public Response getVehicleSalesRevenue(
			@QueryParam("dealerID") Integer dealerID,
			@QueryParam("departmentID") Integer departmentID,
			@QueryParam("validDate") String validDate) {
		try {
			Preconditions.checkNotNull(dealerID, "dealer id can't be null");
			Preconditions.checkNotNull(validDate, "valid date can't be null");
			final GetVehicleSalesRevenueResponse response = new GetVehicleSalesRevenueResponse();
			final Integer requestedDeparmentID = departmentID == null ? DEFAULT_VEHICLE_DEPARTMENT_ID : departmentID;
			final Collection<VehicleSalesJournal> list = 
					incomeJournalDAL.getVehicleSalesJournal(dealerID, requestedDeparmentID, LocalDate.parse(validDate));
			
			response.setDealerID(dealerID);
			response.setDepartmentID(requestedDeparmentID);
			response.setValidDate(LocalDate.parse(validDate));
			for (final VehicleSalesJournal journal : list) {
				final VehicleSalesRevenueDetail item = new VehicleSalesRevenueDetail();
				item.setAmount(journal.getAmount().doubleValue());
				item.setCount(journal.getCount());
				item.setMargin(journal.getMargin().doubleValue());
				item.setName(getVehicleName(journal.getId()));
				item.setVehicleID(journal.getId());
				item.setTimestamp(journal.getTimestamp());
				response.getDetail().add(item);
			}
			
			return Response.ok(response).build();
		} catch (Exception e) {
			final GeneralSaveResponse response = new GeneralSaveResponse();
			response.setErrorMsg(e.getMessage());
			response.setSuccess(false);
			response.setTimestamp(Instant.millis(new Date().getTime()));
			return Response.ok(response).status(Status.BAD_REQUEST).build();
		}
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
	@Transactional
	@Performance
	public Response saveSalesServiceRevenue(
			final SaveSalesServiceRevenueRequest request) {
		try {
			Preconditions.checkNotNull(request.getDealerID(), "dealer id can't be null");
			Preconditions.checkNotNull(request.getDepartmentID(), "department id can't be null");
			Preconditions.checkNotNull(request.getValidDate(), "valid date can't be null");
			Preconditions.checkNotNull(request.getDetail().size() == 0, "no detail is posted");
			
			final List<SalesServiceJournal> journals = Lists.newArrayList();
			for ( final SalesServiceRevenueDetail detail : request.getDetail() ) {
				final SalesServiceJournal journal = new SalesServiceJournal();
				journal.setAmount(new BigDecimal(detail.getAmount()));
				journal.setMargin(new BigDecimal(detail.getMargin()));
				journal.setCount(detail.getCount());
				journal.setDealerID(request.getDealerID());
				Preconditions.checkNotNull(detail.getItemID(), "item id can't be null");
				journal.setId(detail.getItemID());
				journal.setDepartmentID(request.getDepartmentID());
				journal.setUpdatedBy(request.getUpdateBy());
				journal.setValidDate(LocalDate.parse(request.getValidDate()));
				journals.add(journal);
				
			} 
			final Instant timestamp = incomeJournalDAL.saveSalesServiceJournal(request.getDealerID(), request.getDepartmentID(), journals);
			if ( timestamp == null ) {
				throw new RuntimeException("Database save returns null timestamp!");
			}
			final GeneralSaveResponse response = new GeneralSaveResponse();
			response.setErrorMsg("");
			response.setSuccess(true);
			response.setTimestamp(timestamp);
			return Response.ok(response).status(Status.CREATED).build();
		} catch (Exception e) {
			final GeneralSaveResponse response = new GeneralSaveResponse();
			response.setErrorMsg(e.getMessage());
			response.setSuccess(false);
			response.setTimestamp(Instant.millis(new Date().getTime()));
			return Response.ok(response).status(Status.BAD_REQUEST).build();
		} 
	}

	private enum GetSalesServiceIDFunction implements Function<SalesServiceJournalItem, Integer> {
	    INSTANCE;

	    @Override
	    public Integer apply(SalesServiceJournalItem item) {
	        return item.getId();
	    }
	}
	private String getSalesServiceJournalItemName(Integer id) {
		return Maps.uniqueIndex(refDataDAL.getSalesServiceJournalItemList(), GetSalesServiceIDFunction.INSTANCE).get(id).getName();
	}
	/**
	 * Get a list sales & service revenue per sales item.
	 * 
	 * @param dealerID Dealer company id
	 * @param departmentID Department id
	 * @param validDate Date to query report
	 * @return
	 */
	@GET
	@Path("/salesServiceRevenue")
	@Produces({ "application/json", "application/xml" })
	@Transactional
	@Performance
	public Response getSalesServiceRevenue(
			@QueryParam("dealerID") Integer dealerID,
			@QueryParam("departmentID") Integer departmentID,
			@QueryParam("validDate") String validDate) {
		try {
			Preconditions.checkNotNull(dealerID, "dealer id can't be null");
			Preconditions.checkNotNull(departmentID, "department id can't be null");
			Preconditions.checkNotNull(validDate, "valid date can't be null");
			final GetSalesServiceRevenueResponse response = new GetSalesServiceRevenueResponse();
			final Collection<SalesServiceJournal> list = 
					incomeJournalDAL.getSalesServiceJournal(dealerID, departmentID, LocalDate.parse(validDate));
			
			response.setDealerID(dealerID);
			response.setDepartmentID(departmentID);
			response.setValidDate(LocalDate.parse(validDate));
			for (final SalesServiceJournal journal : list) {
				final SalesServiceRevenueDetail item = new SalesServiceRevenueDetail();
				item.setAmount(journal.getAmount().doubleValue());
				item.setCount(journal.getCount());
				item.setMargin(journal.getMargin().doubleValue());
				item.setName(getSalesServiceJournalItemName(journal.getId()));
				item.setItemID(journal.getId());
				item.setTimestamp(journal.getTimestamp());
				response.getDetail().add(item);
			}
			
			return Response.ok(response).build();
		} catch (Exception e) {
			final GeneralSaveResponse response = new GeneralSaveResponse();
			response.setErrorMsg(e.getMessage());
			response.setSuccess(false);
			response.setTimestamp(Instant.millis(new Date().getTime()));
			return Response.ok(response).status(Status.BAD_REQUEST).build();
		}
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
	@Transactional
	@Performance
	public Response saveIncomeTax(
			final SaveTaxRequest request) {
		try {
			Preconditions.checkNotNull(request.getDealerID(), "dealer id can't be null");
			Preconditions.checkNotNull(request.getValidDate(), "valid date can't be null");
			Preconditions.checkNotNull(request.getTax(), "tax amount can't be null");
			final List<TaxJournal> journals = Lists.newArrayList();
			final TaxJournal taxJournal = new TaxJournal();
			taxJournal.setAmount(new BigDecimal(request.getTax()));
			taxJournal.setDealerID(request.getDealerID());
			taxJournal.setUpdatedBy(request.getUpdateBy());
			taxJournal.setValidDate(LocalDate.parse(request.getValidDate()));
			journals.add(taxJournal);
			final Instant timestamp = incomeJournalDAL.saveTaxJournal(request.getDealerID(), journals);
			if ( timestamp == null ) {
				throw new RuntimeException("Database save returns null timestamp!");
			}
			final GeneralSaveResponse response = new GeneralSaveResponse();
			response.setErrorMsg("");
			response.setSuccess(true);
			response.setTimestamp(timestamp);
			return Response.ok(response).status(Status.CREATED).build();
		} catch (Exception e) {
			final GeneralSaveResponse response = new GeneralSaveResponse();
			response.setErrorMsg(e.getMessage());
			response.setSuccess(false);
			response.setTimestamp(Instant.millis(new Date().getTime()));
			return Response.ok(response).status(Status.BAD_REQUEST).build();
		} 
	}

	/**
	 * Get a list sales & service revenue per sales item.
	 * 
	 * @param dealerID Dealer company id
	 * @param departmentID Department id
	 * @param validDate Date to query report
	 * @return
	 */
	@GET
	@Path("/tax")
	@Produces({ "application/json", "application/xml" })
	@Transactional
	@Performance
	public Response getIncomeTax(
			@QueryParam("dealerID") Integer dealerID,
			@QueryParam("validDate") String validDate) {
		try {
			final GetTaxResponse response = new GetTaxResponse();
			final Collection<TaxJournal> list = incomeJournalDAL.getTaxJournal(dealerID, LocalDate.parse(validDate));
			
			response.setDealerID(dealerID);
			response.setValidDate(LocalDate.parse(validDate));
			for (final TaxJournal journal : list) {
				response.setTax(journal.getAmount().doubleValue());
			}
			
			return Response.ok(response).build();
		} catch (Exception e) {
			final GeneralSaveResponse response = new GeneralSaveResponse();
			response.setErrorMsg(e.getMessage());
			response.setSuccess(false);
			response.setTimestamp(Instant.millis(new Date().getTime()));
			return Response.ok(response).status(Status.BAD_REQUEST).build();
		}
	}
	
	/**
	 * Save dealer entry item status. 
	 * 
	 * @param request
	 * @return
	 */
	@POST
	@Produces({"application/json", "application/xml" })
	@Consumes({"application/json", "application/xml" })
	@Path("/menu/entrystatus")
	@Transactional
	@Performance
	public Response saveDealerEntryItemStatus(
			final SaveDealerEntryItemStatusRequest request) {
		try {
			final List<DealerEntryItemStatus> journals = Lists.newArrayList();
			final DealerEntryItemStatus journal = new DealerEntryItemStatus();
			journal.setEntryItemID(request.getItemID());
			journal.setDealerID(request.getDealerID());
			journal.setUpdateBy(request.getUpdateBy());
			journal.setValidDate(LocalDate.parse(request.getValidDate()));
			journals.add(journal);
			final Instant timestamp = incomeJournalDAL.saveDealerEntryItemStatus(request.getDealerID(), journals);
			if ( timestamp == null ) {
				throw new RuntimeException("Database save returns null timestamp!");
			}
			final GeneralSaveResponse response = new GeneralSaveResponse();
			response.setErrorMsg("");
			response.setSuccess(true);
			response.setTimestamp(timestamp);
			return Response.ok(response).status(Status.CREATED).build();
		} catch (Exception e) {
			final GeneralSaveResponse response = new GeneralSaveResponse();
			response.setErrorMsg(e.getMessage());
			response.setSuccess(false);
			response.setTimestamp(Instant.millis(new Date().getTime()));
			return Response.ok(response).status(Status.BAD_REQUEST).build();
		} 
	}

	/**
	 * Get dealer entry item status.
	 * 
	 * @param dealerID Dealer company id
	 * @param validDate Date to query report
	 * @return
	 */
	@GET
	@Path("/menu/entrystatus")
	@Produces({ "application/json", "application/xml" })
	@Transactional
	@Performance
	public Response getDealerEntryItemStatus(
			@QueryParam("dealerID") Integer dealerID,
			@QueryParam("validDate") String validDate) {
		Preconditions.checkNotNull(dealerID, "dealer id can't be null");
		Preconditions.checkNotNull(validDate, "valid date can't be null");
		final GetDealerEntryItemStatusResponse response = new GetDealerEntryItemStatusResponse();
		final Collection<DealerEntryItemStatus> list = incomeJournalDAL.getDealerEntryItemStatus(dealerID, LocalDate.parse(validDate));
		
		response.setDealerID(dealerID);
		response.setValidDate(LocalDate.parse(validDate));
		for (final DealerEntryItemStatus journal : list) {
			final CompletedEntryItem status = new CompletedEntryItem();
			status.setId(journal.getEntryItemID());
			status.setName(refDataDAL.getMenu(status.getId()).getName());
			status.setTimestamp(journal.getTimestamp());
			response.getDetail().add(status);
		}
		
		return Response.ok(response).build();
	}
}
