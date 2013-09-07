package com.jdc.themis.common.jaxb.adaptor;

import javax.time.calendar.LocalDateTime;
import javax.time.calendar.TimeZone;

import junit.framework.Assert;

import org.junit.Test;

public class TestJaxbCalendarInstantAdaptor {

	@Test
	public void marshall() throws Exception {
		Assert.assertEquals("2013-08-01T11:12", new JaxbCalendarInstantAdaptor().marshal(LocalDateTime.of(2013, 8, 1, 11, 12).atZone(TimeZone.UTC).toInstant()));
	}
	
	@Test
	public void unmarshall() throws Exception {
		Assert.assertEquals(LocalDateTime.of(2013, 8, 1, 11, 12).atZone(TimeZone.UTC).toInstant(), 
				new JaxbCalendarInstantAdaptor().unmarshal("2013-08-01T11:12:00"));
	}
	
}
