package com.jdc.themis.dealer.web.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.time.Instant;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.jdc.themis.common.jaxb.adaptor.JaxbCalendarInstantAdaptor;
import com.jdc.themis.common.json.adaptor.JsonCalendarInstantDeserializer;
import com.jdc.themis.common.json.adaptor.JsonCalendarInstantSerializer;

@XmlRootElement
public class VehicleSalesRevenueDetail implements Serializable { 
	
	private static final long serialVersionUID = 1L;
	private Integer vehicleID;
	private String name;
	private Instant timestamp;
	private Long count;
	private BigDecimal amount;
	private BigDecimal margin;
	
	public Integer getVehicleID() {
		return vehicleID;
	}
	public void setVehicleID(Integer vehicleID) {
		this.vehicleID = vehicleID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@XmlJavaTypeAdapter(JaxbCalendarInstantAdaptor.class)
	@JsonSerialize(using = JsonCalendarInstantSerializer.class)
	public Instant getTimestamp() {
		return timestamp;
	}
	@JsonDeserialize(using = JsonCalendarInstantDeserializer.class)
	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getMargin() {
		return margin;
	}
	public void setMargin(BigDecimal margin) {
		this.margin = margin;
	}

}
