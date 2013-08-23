package com.jdc.themis.common.json.adaptor;

import java.io.IOException;

import javax.time.Instant;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class JsonCalendarInstantSerializer extends JsonSerializer<Instant>
{
	@Override
	public void serialize(Instant value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeStartObject();
	    jgen.writeStringField("text", value.toString());
	    jgen.writeEndObject();
	}

}