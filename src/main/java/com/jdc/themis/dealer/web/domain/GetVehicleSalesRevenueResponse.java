package com.jdc.themis.dealer.web.domain;

import java.io.Serializable;
import java.util.List;

import javax.time.calendar.LocalDate;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.common.collect.Lists;
import com.jdc.themis.common.jaxb.adaptor.JaxbCalendarLocalDateAdaptor;

import com.jdc.themis.common.json.adaptor.JsonCalendarLocalDateSerializer;

@XmlRootElement
public class GetVehicleSalesRevenueResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer dealerID;
	private LocalDate validDate;
	private final List<VehicleSalesRevenueDetail> detail = Lists.newArrayList();
	
	public Integer getDealerID() {
		return dealerID;
	}
	public void setDealerID(Integer dealerID) {
		this.dealerID = dealerID;
	}
	@XmlJavaTypeAdapter(JaxbCalendarLocalDateAdaptor.class)
	@JsonSerialize(using = JsonCalendarLocalDateSerializer.class)
	public LocalDate getValidDate() {
		return validDate;
	}
	public void setValidDate(LocalDate validDate) {
		this.validDate = validDate;
	}
	public List<VehicleSalesRevenueDetail> getDetail() {
		return detail;
	}
	
}
