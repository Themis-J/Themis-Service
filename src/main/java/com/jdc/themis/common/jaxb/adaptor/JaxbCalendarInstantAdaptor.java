package com.jdc.themis.common.jaxb.adaptor;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.time.Instant;

public class JaxbCalendarInstantAdaptor extends XmlAdapter<String, Instant> {

    @Override
    public Instant unmarshal(String v) throws Exception {
        return Instant.parse(v);
    }

    @Override
    public String marshal(Instant v) throws Exception {
        return v.toString();
    }

}