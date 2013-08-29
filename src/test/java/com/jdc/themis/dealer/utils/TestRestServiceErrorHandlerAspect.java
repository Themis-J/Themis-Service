package com.jdc.themis.dealer.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.apache.cxf.jaxrs.impl.ResponseImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.jdc.themis.dealer.web.domain.GeneralResponse;

public class TestRestServiceErrorHandlerAspect {
	@Mock
	private ProceedingJoinPoint function;
	@Before
	public void setup() {
		MockitoAnnotations.initMocks( this );
		function = mock(ProceedingJoinPoint.class);
	}
	
	@Test
	public void proceedSuccessfully() throws Throwable {
		when(function.proceed()).thenReturn("SomeResult");
		
		Assert.assertEquals("SomeResult", new RestServiceErrorHandlerAspect().proceed(function).toString());
	}
	
	@Test
	public void proceedFailed() throws Throwable {
		when(function.proceed()).thenThrow(new RuntimeException("SomeError"));
		
		Assert.assertEquals("SomeError", ((GeneralResponse) ((ResponseImpl) new RestServiceErrorHandlerAspect().proceed(function)).getEntity()).getErrorMsg());
		Assert.assertEquals(400, ((ResponseImpl) new RestServiceErrorHandlerAspect().proceed(function)).getStatus());
		Assert.assertEquals(Boolean.FALSE, ((GeneralResponse) ((ResponseImpl) new RestServiceErrorHandlerAspect().proceed(function)).getEntity()).getSuccess());
	}
}
