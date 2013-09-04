package com.jdc.themis.dealer.web.domain;

import java.io.Serializable;
import javax.time.calendar.LocalDate;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.jdc.themis.common.jaxb.adaptor.JaxbCalendarLocalDateAdaptor;

import com.jdc.themis.common.json.adaptor.JsonCalendarLocalDateSerializer;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GetTaxResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer dealerID;
	@XmlJavaTypeAdapter(JaxbCalendarLocalDateAdaptor.class)
	private LocalDate validDate;
	private Double tax;
	
	public Double getTax() {
		return tax;
	}
	public void setTax(Double tax) {
		this.tax = tax;
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
	
}
