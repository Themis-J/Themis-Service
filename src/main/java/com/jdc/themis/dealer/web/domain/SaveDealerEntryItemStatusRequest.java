package com.jdc.themis.dealer.web.domain;

import java.io.Serializable;

import javax.time.calendar.LocalDate;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SaveDealerEntryItemStatusRequest implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer dealerID;
	private LocalDate validDate;
	private Integer itemID;
	private String updateBy;
	
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Integer getItemID() {
		return itemID;
	}
	public void setItemID(Integer itemID) {
		this.itemID = itemID;
	}
	public Integer getDealerID() {
		return dealerID;
	}
	public void setDealerID(Integer dealerID) {
		this.dealerID = dealerID;
	}
	public LocalDate getValidDate() {
		return validDate;
	}
	public void setValidDate(LocalDate validDate) {
		this.validDate = validDate;
	}
	
}
