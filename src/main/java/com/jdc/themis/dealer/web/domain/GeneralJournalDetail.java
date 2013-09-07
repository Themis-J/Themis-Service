package com.jdc.themis.dealer.web.domain;

import java.io.Serializable;

import javax.time.Instant;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.jdc.themis.common.jaxb.adaptor.JaxbCalendarInstantAdaptor;
import com.jdc.themis.common.json.adaptor.JsonCalendarInstantSerializer;

/**
 * Detail about a general income journal. 
 * 
 * @author Kai Chen
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GeneralJournalDetail implements Serializable { 
	
	private static final long serialVersionUID = 1L;
	private Integer itemID;
	private String name;
	private Instant timestamp;
	private Double amount;
	private String category;
	private Integer categoryID;
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Integer getCategoryID() {
		return categoryID;
	}
	public void setCategoryID(Integer categoryID) {
		this.categoryID = categoryID;
	}
	
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
	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}
	
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
}
