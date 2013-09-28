package com.jdc.themis.dealer.service.rest;


/**
 * Detail about a general income journal. 
 * 
 * @author Kai Chen
 *
 */
public class HumanResourceAllocationDetail {
	private Integer itemID;
	private String name;
	private Timestamp timestamp;
	private Double allocation;
	private Integer departmentID;
	
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
	
	public Double getAllocation() {
		return allocation;
	}
	public void setAllocation(Double allocation) {
		this.allocation = allocation;
	}
}
