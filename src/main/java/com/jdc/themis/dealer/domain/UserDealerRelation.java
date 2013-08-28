package com.jdc.themis.dealer.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name=	"userDealer")
public class UserDealerRelation  implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	private Integer userID;
	@Id
	private Integer dealerID;
	@Id
	public Integer getUserID() {
		return userID;
	}
	public void setUserID(Integer userID) {
		this.userID = userID;
	}
	@Id
	public Integer getDealerID() {
		return dealerID;
	}
	public void setDealerID(Integer dealerID) {
		this.dealerID = dealerID;
	}

}
