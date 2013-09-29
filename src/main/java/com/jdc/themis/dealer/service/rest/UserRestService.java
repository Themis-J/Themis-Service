package com.jdc.themis.dealer.service.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jdc.themis.dealer.service.UserService;
import com.jdc.themis.dealer.utils.RestServiceErrorHandler;
import com.jdc.themis.dealer.utils.Utils;
import com.jdc.themis.dealer.web.domain.AddNewUserRequest;
import com.jdc.themis.dealer.web.domain.GeneralResponse;
import com.jdc.themis.dealer.web.domain.ResetPasswordRequest;

@Service
public class UserRestService {

	@Autowired
	private UserService userService;

	@Context
	private HttpServletRequest req;

	/**
	 * Query user information.
	 * 
	 * @param username
	 * @return
	 */
	@GET
	@Path("/info")
	@Produces({ "application/json", "application/xml" })
	@RestServiceErrorHandler
	public Response getUser(@QueryParam("username") String username) {
		return Response.ok(userService.getUser(username)).build();
	}

	@POST
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	@Path("/add")
	@RestServiceErrorHandler
	public Response addNewUser(final AddNewUserRequest request) {
		final GeneralResponse response = new GeneralResponse();
		response.setErrorMsg("");
		response.setSuccess(true);
		userService.addNewUser(request);
		response.setTimestamp(Utils.currentTimestamp());
		return Response.ok().build();
	}

	@POST
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	@Path("/resetpwd")
	@RestServiceErrorHandler
	public Response resetPassword(final ResetPasswordRequest request) {
		final GeneralResponse response = new GeneralResponse();
		response.setErrorMsg("");
		response.setSuccess(true);
		userService.resetPassword(request);
		response.setTimestamp(Utils.currentTimestamp());
		return Response.ok().build();
	}

	@GET
	@Path("/isAlive")
	public Response isAlive() {
		// TODO
		return null;
	}

	@POST
	@Path("/logout")
	public Response logout() {
		// TODO
		return null;
	}
}
