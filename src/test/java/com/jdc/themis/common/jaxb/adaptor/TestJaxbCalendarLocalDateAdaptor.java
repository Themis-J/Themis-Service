package com.jdc.themis.common.jaxb.adaptor;

import javax.time.calendar.LocalDate;

import org.junit.Test;

import junit.framework.Assert;

public class TestJaxbCalendarLocalDateAdaptor {

	@Test
	public void marshall() throws Exception {
		Assert.assertEquals("2013-08-01", new JaxbCalendarLocalDateAdaptor().marshal(LocalDate.of(2013, 8, 1)));
	}
	
	@Test
	public void unmarshall() throws Exception {
		Assert.assertEquals(LocalDate.of(2013, 8, 1), new JaxbCalendarLocalDateAdaptor().unmarshal("2013-08-01"));
	}
	
}
