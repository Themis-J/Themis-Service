package com.jdc.themis.dealer.service.rest;

import java.util.List;

import com.google.common.collect.Lists;

public class GetEmployeeFeeSummaryResponse {
	private Integer dealerID;
	private LocalDate validDate;
	private final List<EmployeeFeeSummaryDetail> detail = Lists.newArrayList();
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
	public List<EmployeeFeeSummaryDetail> getDetail() {
		return detail;
	}
	
}
