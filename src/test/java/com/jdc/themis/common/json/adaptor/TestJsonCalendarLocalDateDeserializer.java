package com.jdc.themis.common.json.adaptor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.time.calendar.LocalDate;

import junit.framework.Assert;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestJsonCalendarLocalDateDeserializer {

	private JsonCalendarLocalDateDeserializer instance;
	@Mock
	private JsonParser jsonParser;
	@Mock
	private DeserializationContext context;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks( this );
		jsonParser = mock(JsonParser.class);
		this.instance = new JsonCalendarLocalDateDeserializer();
	}
	
	@Test
	public void serialize() throws JsonProcessingException, IOException {
		when(jsonParser.getText()).thenReturn("2013-08-01");
		final LocalDate date = instance.deserialize(jsonParser, context);
		Assert.assertEquals(LocalDate.of(2013, 8, 1), date);
	}
	
}
