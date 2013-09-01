package com.jdc.themis.dealer.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EntitlementResource {

	@Id
	private Integer id;
	private String name;
	private Integer resourceType;
	
	@Id
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
	public Integer getResourceType() {
		return resourceType;
	}
	public void setResourceType(Integer resourceType) {
		this.resourceType = resourceType;
	}
	
}
