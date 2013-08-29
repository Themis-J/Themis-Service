package com.jdc.themis.dealer.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestPerformanceAspect {
	@Mock
	private ProceedingJoinPoint function;
	@Before
	public void setup() {
		MockitoAnnotations.initMocks( this );
		function = mock(ProceedingJoinPoint.class);
	}
	
	@Test
	public void doBasicProfiling() throws Throwable {
		when(function.proceed()).thenReturn("SomeResult");
		
		Assert.assertEquals("SomeResult", new PerformanceAspect().doBasicProfiling(function).toString());
	}
	
}
