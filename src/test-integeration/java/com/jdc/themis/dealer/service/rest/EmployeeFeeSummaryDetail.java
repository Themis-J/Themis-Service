package com.jdc.themis.dealer.service.rest;


/**
 * Detail about a general income journal. 
 * 
 * @author Kai Chen
 *
 */
public class EmployeeFeeSummaryDetail {
	private Integer itemID;
	private String name;
	private Timestamp timestamp;
	private Double amount;
	
	public Integer getItemID() {
		return itemID;
	}
	public void setItemID(Integer itemID) {
		this.itemID = itemID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
}
