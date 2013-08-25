package com.jdc.themis.common.json.adaptor;

import java.io.IOException;
import javax.time.calendar.LocalDate;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class TestJsonCalendarLocalDateSerializer {

	private JsonCalendarLocalDateSerializer instance;
	@Mock
	private JsonGenerator jsonGenerator;
	@Mock
	private SerializerProvider provider;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks( this );
		jsonGenerator = mock(JsonGenerator.class);
		this.instance = new JsonCalendarLocalDateSerializer();
	}
	
	@Test
	public void serialize() throws JsonProcessingException, IOException {
		final LocalDate date = LocalDate.of(2013, 8, 1);
		instance.serialize(date, jsonGenerator, provider);
		verify(jsonGenerator, times(1)).writeStartObject();
		verify(jsonGenerator).writeStringField("text", date.toString());
		verify(jsonGenerator, times(1)).writeEndObject();
	}
	
}
