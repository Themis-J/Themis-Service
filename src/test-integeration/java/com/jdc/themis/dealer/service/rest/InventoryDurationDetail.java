package com.jdc.themis.dealer.service.rest;


/**
 * Detail about a general income journal. 
 * 
 * @author Kai Chen
 *
 */
public class InventoryDurationDetail {
	private Integer itemID;
	private String name;
	private Timestamp timestamp;
	private Double amount;
	private Integer durationID;
	private String durationDesc;
	
	public Integer getDurationID() {
		return durationID;
	}
	public void setDurationID(Integer durationID) {
		this.durationID = durationID;
	}
	public String getDurationDesc() {
		return durationDesc;
	}
	public void setDurationDesc(String durationDesc) {
		this.durationDesc = durationDesc;
	}
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
