package com.jdc.themis.dealer.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
public class UserInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String password;
	private Integer userRoleID;
	private Boolean active;
	private Integer dealerID;
	
	public Integer getDealerID() {
		return dealerID;
	}
	public void setDealerID(Integer dealerID) {
		this.dealerID = dealerID;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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
	public Integer getUserRoleID() {
		return userRoleID;
	}
	public void setUserRoleID(Integer userRoleID) {
		this.userRoleID = userRoleID;
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
				.append("userRoleID", userRoleID)
				.append("dealerID", dealerID)
				.append("active", active)
				.getStringBuffer().toString();
	}
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}
