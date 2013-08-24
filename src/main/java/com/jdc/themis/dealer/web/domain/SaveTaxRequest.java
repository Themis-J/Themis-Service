package com.jdc.themis.dealer.web.domain;

import java.io.Serializable;
import javax.time.calendar.LocalDate;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SaveTaxRequest implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer dealerID;
	private LocalDate validDate;
	private String updateBy;
	private Double tax;
	
	public Double getTax() {
		return tax;
	}
	public void setTax(Double tax) {
		this.tax = tax;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
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
