package com.jdc.themis.dealer.service.rest;

import java.util.List;


import com.google.common.collect.Lists;

public class GetSalesServiceJournalResponse {
	private Integer dealerID;
	private LocalDate validDate;
	private final List<SalesServiceJournalDetail> detail = Lists.newArrayList();
	private Integer departmentID;
	
	public Integer getDepartmentID() {
		return departmentID;
	}
	public void setDepartmentID(Integer departmentID) {
		this.departmentID = departmentID;
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
	public List<SalesServiceJournalDetail> getDetail() {
		return detail;
	}
	
}
