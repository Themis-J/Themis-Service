package com.jdc.themis.dealer.service.rest;

import java.io.Serializable;
import java.util.List;
import com.google.common.collect.Lists;

public class GetVehicleSalesJournalResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer dealerID;
	private Integer departmentID;
	public Integer getDepartmentID() {
		return departmentID;
	}
	public void setDepartmentID(Integer departmentID) {
		this.departmentID = departmentID;
	}
	private LocalDate validDate;
	private final List<VehicleSalesJournalDetail> detail = Lists.newArrayList();
	
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
	public List<VehicleSalesJournalDetail> getDetail() {
		return detail;
	}
	
}
