package com.jdc.themis.dealer.service.rest;

import java.util.List;

import com.google.common.collect.Lists;

public class GetGeneralJournalResponse {
	private Integer dealerID;
	private LocalDate validDate;
	private final List<GeneralJournalDetail> detail = Lists.newArrayList();
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
	public List<GeneralJournalDetail> getDetail() {
		return detail;
	}
	
}
