package com.jdc.themis.dealer.web.domain;

import java.io.Serializable;

import javax.time.Instant;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.jdc.themis.common.jaxb.adaptor.JaxbCalendarInstantAdaptor;
import com.jdc.themis.common.json.adaptor.JsonCalendarInstantDeserializer;
import com.jdc.themis.common.json.adaptor.JsonCalendarInstantSerializer;

@XmlRootElement
public class VehicleItem implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String name;
	private Instant timestamp;
	
	@XmlJavaTypeAdapter(JaxbCalendarInstantAdaptor.class)
	@JsonSerialize(using = JsonCalendarInstantSerializer.class)
	public Instant getTimestamp() {
		return timestamp;
	}
	@JsonDeserialize(using = JsonCalendarInstantDeserializer.class)
	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}