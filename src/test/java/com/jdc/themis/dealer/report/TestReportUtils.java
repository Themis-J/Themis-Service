package com.jdc.themis.dealer.report;

import junit.framework.Assert;

import org.junit.Test;

import com.google.common.collect.Lists;

public class TestReportUtils {

	@Test
	public void calculateReference() {
		final Double result = ReportUtils.calcReference(Lists.newArrayList(
				10.0D, 10.0D, 10.0D, 10D, 10D, 10D, 10D, 20D, 20D, 20.0D,
				1000000.0D));
		Assert.assertEquals(
				(10.0D + 10.0D + 10D + 10D + 10D + 10D + 20D + 20D + 20.0D) / 9,
				result);
	}

	@Test
	public void calculatePercentage() {
		Assert.assertEquals(0.5, ReportUtils.calcPercentage(1500.0, 1000.0));
	}
	
	@Test
	public void calculatePercentage2() {
		Assert.assertEquals(-0.5, ReportUtils.calcPercentage(-1500.0, -1000.0));
	}
	
	@Test
	public void calculatePercentage3() {
		Assert.assertEquals(0.0, ReportUtils.calcPercentage(-1000.0, -1000.0));
	}
	
	@Test
	public void calculatePercentage4() {
		Assert.assertEquals(0.0, ReportUtils.calcPercentage(-1000.0, 0.0));
	}
}
