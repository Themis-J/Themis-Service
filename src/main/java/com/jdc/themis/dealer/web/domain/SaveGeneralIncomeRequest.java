package com.jdc.themis.dealer.web.domain;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Lists;

@XmlRootElement
public class SaveGeneralIncomeRequest implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer dealerID;
	private String validDate;
	private final List<GeneralJournalDetail> detail = Lists.newArrayList();
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
	public String getValidDate() {
		return validDate;
	}
	public void setValidDate(String validDate) {
		this.validDate = validDate;
	}
	public List<GeneralJournalDetail> getDetail() {
		return detail;
	}
	
}
