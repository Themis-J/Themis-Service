package com.jdc.themis.dealer.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
public class UserInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	private Integer id;
	private String username;
	private String password;
	private Integer userType;
	private Boolean active;
	
	@Id
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public String toString() {
		return new ToStringBuilder(this).append("id", id)
				.append("username", username)
				.append("userType", userType)
				.append("active", active)
				.getStringBuffer().toString();
	}
}
