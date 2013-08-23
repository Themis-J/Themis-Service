package com.jdc.themis.common.json.adaptor;

import java.io.IOException;

import javax.time.calendar.LocalDate;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

public class JsonCalendarLocalDateDeserializer extends JsonDeserializer<LocalDate>
{

	@Override
	public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		return LocalDate.parse(jp.getText());
	}

}