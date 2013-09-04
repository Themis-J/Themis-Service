package com.jdc.themis.dealer.web.domain;

import java.io.Serializable;
import java.util.List;

import javax.time.calendar.LocalDate;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.common.collect.Lists;
import com.jdc.themis.common.jaxb.adaptor.JaxbCalendarLocalDateAdaptor;

import com.jdc.themis.common.json.adaptor.JsonCalendarLocalDateSerializer;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GetInventoryDurationResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer dealerID;
	private Integer deparmentID;
	@XmlJavaTypeAdapter(JaxbCalendarLocalDateAdaptor.class)
	private LocalDate validDate;
	@XmlElement(name = "detail")
	private final List<InventoryDurationDetail> detail = Lists.newArrayList();

	public Integer getDeparmentID() {
		return deparmentID;
	}
	public void setDeparmentID(Integer deparmentID) {
		this.deparmentID = deparmentID;
	}
	public Integer getDealerID() {
		return dealerID;
	}
	public void setDealerID(Integer dealerID) {
		this.dealerID = dealerID;
	}
	@JsonSerialize(using = JsonCalendarLocalDateSerializer.class)
	public LocalDate getValidDate() {
		return validDate;
	}
	public void setValidDate(LocalDate validDate) {
		this.validDate = validDate;
	}
	public List<InventoryDurationDetail> getDetail() {
		return detail;
	}
	
}
