package com.jdc.themis.dealer.web.domain;

import java.io.Serializable;
import java.util.List;

import javax.time.calendar.LocalDate;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SaveVehicleSalesRevenueRequest implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer dealerID;
	private LocalDate validDate;
	private List<VehicleSalesRevenueDetail> detail;
	
	public Integer getDealerID() {
		return dealerID;
	}
	public void setDealerID(Integer dealerID) {
		this.dealerID = dealerID;
	}
	public LocalDate getValidDate() {
		return validDate;
	}
	public void setValidDate(LocalDate validDate) {
		this.validDate = validDate;
	}
	public List<VehicleSalesRevenueDetail> getDetail() {
		return detail;
	}
	public void setDetail(List<VehicleSalesRevenueDetail> detail) {
		this.detail = detail;
	}
	
	
}
