package com.jdc.themis.dealer.service.rest;

import javax.time.calendar.LocalDate;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jdc.themis.dealer.data.dao.RefDataDAO;
import com.jdc.themis.dealer.domain.Menu;
import com.jdc.themis.dealer.domain.Vehicle;
import com.jdc.themis.dealer.web.domain.GetMenuResponse;
import com.jdc.themis.dealer.web.domain.GetVehicleResponse;
import com.jdc.themis.dealer.web.domain.GetVehicleSalesRevenueResponse;
import com.jdc.themis.dealer.web.domain.MenuItem;
import com.jdc.themis.dealer.web.domain.SaveVehicleSalesRevenueRequest;
import com.jdc.themis.dealer.web.domain.VehicleItem;

@Service
public class DealerIncomeEntryRestService {

	@Autowired
	private RefDataDAO refDataDAL;

	public RefDataDAO getRefDataDAL() {
		return refDataDAL;
	}

	public void setRefDataDAL(RefDataDAO refDataDAL) {
		this.refDataDAL = refDataDAL;
	}

	@POST
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/vehicleSalesRevenue")
	public Response saveVehicleSalesRevenue(
			final SaveVehicleSalesRevenueRequest request) {
		return Response.ok().build();
	}

	@GET
	@Path("/vehicleSalesRevenue")
	// @Produces({"application/xml","application/json"})
	@Produces({ "application/json" })
	public Response getVehicleSalesRevenue(
			@QueryParam("dealerID") Integer dealerID,
			@QueryParam("validDate") String validDate) {
		final GetVehicleSalesRevenueResponse response = new GetVehicleSalesRevenueResponse();
		response.setDealerID(1);
		response.setValidDate(LocalDate.of(2013, 8, 1));
		return Response.ok(response).build();
	}

	@GET
	@Path("/menu")
	// @Produces({"application/xml","application/json"})
	@Produces({ "application/json" })
	@Transactional(readOnly = true)
	public Response getMenuByID(@QueryParam("id") Integer id) {
		final GetMenuResponse response = new GetMenuResponse();

		final Menu menu = refDataDAL.getMenu(id);
		final MenuItem item = new MenuItem();
		item.setId(menu.getId());
		item.setName(menu.getName());
		item.setDisplayText(menu.getDisplayText());
		item.setParentID(refDataDAL.getParentMenuID(id));
		
		response.getItems().add(item);

		return Response.ok(response).build();
	}
	
	@GET
	@Path("/menu/all")
	// @Produces({"application/xml","application/json"})
	@Produces({ "application/json" })
	@Transactional(readOnly = true)
	public Response getAllMenu() {
		final GetMenuResponse response = new GetMenuResponse();

		for (final Menu menu : refDataDAL.getMenuList()) {
			final MenuItem item = new MenuItem();
			item.setId(menu.getId());
			item.setName(menu.getName());
			item.setDisplayText(menu.getDisplayText());
			item.setParentID(refDataDAL.getParentMenuID(menu.getId()));
			
			response.getItems().add(item);
		}
		return Response.ok(response).build();
	}
	
	@GET
	@Path("/vehicle/all")
	// @Produces({"application/xml","application/json"})
	@Produces({ "application/json" })
	@Transactional(readOnly = true)
	public Response getAllVehicles() {
		final GetVehicleResponse response = new GetVehicleResponse();

		for (final Vehicle vehicle : refDataDAL.getVehicleList()) {
			final VehicleItem item = new VehicleItem();
			item.setId(vehicle.getId());
			item.setName(vehicle.getName());
			item.setTimestamp(vehicle.getTimestamp());
			response.getItems().add(item);
		}
		return Response.ok(response).build();
	}

}
