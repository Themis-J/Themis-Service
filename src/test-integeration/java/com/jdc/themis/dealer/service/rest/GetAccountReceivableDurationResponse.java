package com.jdc.themis.dealer.service.rest;

import java.util.List;

import com.google.common.collect.Lists;

public class GetAccountReceivableDurationResponse {
	private Integer dealerID;
	private LocalDate validDate;
	private final List<AccountReceivableDurationDetail> detail = Lists.newArrayList();

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
	public List<AccountReceivableDurationDetail> getDetail() {
		return detail;
	}
	
}
