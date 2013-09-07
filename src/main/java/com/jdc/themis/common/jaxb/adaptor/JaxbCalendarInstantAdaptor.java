package com.jdc.themis.common.jaxb.adaptor;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.time.Instant;
import javax.time.calendar.LocalDateTime;
import javax.time.calendar.TimeZone;
import javax.time.calendar.ZonedDateTime;

public class JaxbCalendarInstantAdaptor extends XmlAdapter<String, Instant> {

    @Override
    public Instant unmarshal(String v) throws Exception {
        return LocalDateTime.parse(v).atZone(TimeZone.UTC).toInstant();
    }

    @Override
    public String marshal(Instant v) throws Exception {
        return LocalDateTime.from(ZonedDateTime.fromInstant(v, TimeZone.UTC)).toString();
    }

}