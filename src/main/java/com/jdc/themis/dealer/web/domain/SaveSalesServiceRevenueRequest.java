package com.jdc.themis.dealer.web.domain;

import java.io.Serializable;
import java.util.List;

import javax.time.calendar.LocalDate;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SaveSalesServiceRevenueRequest implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer dealerID;
	private LocalDate validDate;
	private List<SalesServiceRevenueDetail> detail;
	private String updateBy;
	private Integer departmentID;
	
	public Integer getDepartmentID() {
		return departmentID;
	}
	public void setDepartmentID(Integer departmentID) {
		this.departmentID = departmentID;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
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
	public List<SalesServiceRevenueDetail> getDetail() {
		return detail;
	}
	public void setDetail(List<SalesServiceRevenueDetail> detail) {
		this.detail = detail;
	}
	
	
}
