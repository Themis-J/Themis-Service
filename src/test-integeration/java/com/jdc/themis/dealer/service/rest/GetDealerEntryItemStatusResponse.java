package com.jdc.themis.dealer.service.rest;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * Response of getting dealer entry item complete status. 
 * 
 * @author Kai Chen
 *
 */
public class GetDealerEntryItemStatusResponse {
	private Integer dealerID;
	private LocalDate validDate;
	private final List<DealerEntryItemStatusDetail> status = Lists.newArrayList();
	
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
	public List<DealerEntryItemStatusDetail> getDetail() {
		return status;
	}
	
}
