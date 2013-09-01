package com.jdc.themis.dealer.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class DealerIncomeExpenseFact implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer timeID;
	private Integer dealerID;
	private Integer departmentID;
	private Integer itemID;
	private BigDecimal amount;
	
	public Integer getTimeID() {
		return timeID;
	}
	public void setTimeID(Integer timeID) {
		this.timeID = timeID;
	}
	public Integer getDealerID() {
		return dealerID;
	}
	public void setDealerID(Integer dealerID) {
		this.dealerID = dealerID;
	}
	public Integer getDepartmentID() {
		return departmentID;
	}
	public void setDepartmentID(Integer departmentID) {
		this.departmentID = departmentID;
	}
	public Integer getItemID() {
		return itemID;
	}
	public void setItemID(Integer itemID) {
		this.itemID = itemID;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
