package com.jdc.themis.dealer.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class UserRoleEntitlement implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	private Integer roleID;
	@Id
	private Integer resourceID;
	
	@Id
	public Integer getRoleID() {
		return roleID;
	}
	public void setRoleID(Integer roleID) {
		this.roleID = roleID;
	}
	@Id
	public Integer getResourceID() {
		return resourceID;
	}
	public void setResourceID(Integer resourceID) {
		this.resourceID = resourceID;
	}

}
