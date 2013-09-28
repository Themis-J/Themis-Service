package com.jdc.themis.dealer.service.rest;


/**
 * The item has been marked to be 'completed' status. 
 * 
 * @author Kai Chen
 *
 */
public class DealerEntryItemStatusDetail {

	private Integer id;
	private String name;
	private Timestamp timestamp; 
	
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
