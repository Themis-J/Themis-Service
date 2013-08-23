package com.jdc.themis.common.jaxb.adaptor;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.time.calendar.*;

public class JaxbCalendarLocalDateAdaptor extends XmlAdapter<String, LocalDate> {

    @Override
    public LocalDate unmarshal(String v) throws Exception {
        return LocalDate.parse(v);
    }

    @Override
    public String marshal(LocalDate v) throws Exception {
        return v.toString();
    }

}