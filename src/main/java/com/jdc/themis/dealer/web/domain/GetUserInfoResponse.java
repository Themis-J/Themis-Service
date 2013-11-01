package com.jdc.themis.dealer.web.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GetUserInfoResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private String username;
	private String role;
	private DealerDetail dealer;
	private Boolean active;
	
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public DealerDetail getDealer() {
		return dealer;
	}
	public void setDealer(DealerDetail dealer) {
		this.dealer = dealer;
	}

}
