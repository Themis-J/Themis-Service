package com.jdc.themis.common.json.adaptor;

import java.io.IOException;

import javax.time.Instant;
import javax.time.calendar.TimeZone;
import javax.time.calendar.ZonedDateTime;

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
	    jgen.writeStringField("utc", value.toString());
	    jgen.writeStringField("text", ZonedDateTime.fromInstant(value, TimeZone.of("GMT+08:00:00")).toString().replaceAll("\\+08:00\\[UTC\\+08:00\\]", ""));
	    jgen.writeEndObject();
	}

}