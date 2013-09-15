package com.jdc.themis.dealer.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This aspect is designed to monitor how long the monitored function runs.
 * 
 * @author Kai Chen
 *
 */
@Aspect
public class PerformanceAspect {
	private final static Logger logger = LoggerFactory.getLogger(PerformanceAspect.class);
	
	@Around("@annotation(com.jdc.themis.dealer.utils.Performance)")
	public Object doBasicProfiling(final ProceedingJoinPoint pjp) throws Throwable {
		final long start = System.currentTimeMillis();
		final Object retVal = pjp.proceed();
		final long end = System.currentTimeMillis();
		
		logger.info("Function {} execution time {}", pjp.toLongString(), end - start);
		return retVal;
	}

}
