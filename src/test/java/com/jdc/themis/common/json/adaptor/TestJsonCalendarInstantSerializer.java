package com.jdc.themis.common.json.adaptor;

import java.io.IOException;
import java.util.Date;

import javax.time.Instant;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class TestJsonCalendarInstantSerializer {

	private JsonCalendarInstantSerializer instance;
	@Mock
	private JsonGenerator jsonGenerator;
	@Mock
	private SerializerProvider provider;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks( this );
		jsonGenerator = mock(JsonGenerator.class);
		this.instance = new JsonCalendarInstantSerializer();
	}
	
	@Test
	public void serialize() throws JsonProcessingException, IOException {
		final Instant timestamp = Instant.millis(new Date().getTime());
		instance.serialize(timestamp, jsonGenerator, provider);
		verify(jsonGenerator, times(1)).writeStartObject();
		verify(jsonGenerator).writeStringField("text", timestamp.toString());
		verify(jsonGenerator, times(1)).writeEndObject();
	}
	
}
