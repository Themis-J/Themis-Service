package com.jdc.themis.common.json.adaptor;

import java.io.IOException;

import javax.time.calendar.LocalDate;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class JsonCalendarLocalDateSerializer extends JsonSerializer<LocalDate>
{

	@Override
	public void serialize(LocalDate value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeStartObject();
	    jgen.writeStringField("text", value.toString());
	    jgen.writeNumberField("days", value.getDayOfMonth());
	    jgen.writeNumberField("month", value.getMonthOfYear().getValue());
	    jgen.writeNumberField("year", value.getYear());
	    jgen.writeEndObject();
	}

}