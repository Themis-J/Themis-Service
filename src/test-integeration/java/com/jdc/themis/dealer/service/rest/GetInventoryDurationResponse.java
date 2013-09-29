package com.jdc.themis.dealer.service.rest;

import java.util.List;

import com.google.common.collect.Lists;

public class GetInventoryDurationResponse {
	private Integer dealerID;
	private Integer deparmentID;
	private LocalDate validDate;
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
