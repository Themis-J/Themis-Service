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
public class SalesServiceRevenueDetail implements Serializable { 
	
	private static final long serialVersionUID = 1L;
	private Integer itemID;
	private String name;
	private Instant timestamp;
	private Integer count;
	private Double amount;
	private Double margin;
	
	public Integer getItemID() {
		return itemID;
	}
	public void setItemID(Integer itemID) {
		this.itemID = itemID;
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
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getMargin() {
		return margin;
	}
	public void setMargin(Double margin) {
		this.margin = margin;
	}

}
