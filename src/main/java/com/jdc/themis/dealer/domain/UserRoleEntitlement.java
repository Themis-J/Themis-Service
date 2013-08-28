package com.jdc.themis.dealer.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;

@FilterDefs(
		{
			@org.hibernate.annotations.FilterDef(name="ureFilter", 
					parameters = {
						@org.hibernate.annotations.ParamDef(name="roleID", type="integer")}), 
		}
		)
@Filters( {
    @Filter(name="ureFilter", condition="roleID = :roleID"), 
} )
@Entity
public class UserRoleEntitlement implements Serializable {

	public static final String FILTER = "ureFilter";
	
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
