package com.jdc.themis.dealer.utils;

import java.util.Date;

import javax.time.Instant;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jdc.themis.dealer.web.domain.GeneralResponse;

/**
 * The aspect is designed to monitor the web service function and capture the exception if there any. 
 * 
 * @author Kai Chen
 *
 */
@Aspect
public class RestServiceErrorHandlerAspect {
	private final static Logger logger = LoggerFactory.getLogger(RestServiceErrorHandlerAspect.class);
	
	@Around("@annotation(com.jdc.themis.dealer.utils.RestServiceErrorHandler)")
	public Object proceed(final ProceedingJoinPoint pjp) throws Throwable {
		try {
			return  pjp.proceed();
		} catch (Exception e) {
			logger.error("capture error in call " + pjp.toLongString(), e);
			final GeneralResponse response = new GeneralResponse();
			response.setErrorMsg(e.getMessage());
			response.setSuccess(false);
			response.setTimestamp(Instant.millis(new Date().getTime()));
			return Response.ok(response).status(Status.BAD_REQUEST).build();
		}
	}

}
