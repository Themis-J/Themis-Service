package com.jdc.themis.dealer.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class PerformanceAspect {
	private final static Logger logger = LoggerFactory.getLogger(PerformanceAspect.class);
	
	@Around("@annotation(com.jdc.themis.dealer.utils.Performance)")
	public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable {
		long start = System.currentTimeMillis();
		Object retVal = pjp.proceed();
		long end = System.currentTimeMillis();
		
		logger.info("Function {} execution time {}", pjp.toLongString(), end - start);
		return retVal;
	}

}
