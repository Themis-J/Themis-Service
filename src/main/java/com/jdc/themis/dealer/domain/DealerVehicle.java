package com.jdc.themis.dealer.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.time.calendar.LocalDate;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.jdc.themis.dealer.data.hibernate.type.PersistentLocalDate;

@TypeDefs({ 
	@TypeDef(name = "localdate", typeClass = PersistentLocalDate.class)})
@Entity
public class DealerVehicle implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	private Integer dealerID;
	@Id
	private Integer vehicleID;
	@Id
	@Type(type = "localdate")
	private LocalDate validDate;
	@Type(type = "localdate")
	private LocalDate validEnd;
	
	@Id
	public Integer getDealerID() {
		return dealerID;
	}
	public void setDealerID(Integer dealerID) {
		this.dealerID = dealerID;
	}
	@Id
	public Integer getVehicleID() {
		return vehicleID;
	}
	public void setVehicleID(Integer vehicleID) {
		this.vehicleID = vehicleID;
	}
	@Id
	@Type(type = "localdate")
	public LocalDate getValidDate() {
		return validDate;
	}
	public void setValidDate(LocalDate validDate) {
		this.validDate = validDate;
	}
	@Id
	@Type(type = "localdate")
	public LocalDate getValidEnd() {
		return validEnd;
	}
	public void setValidEnd(LocalDate validEnd) {
		this.validEnd = validEnd;
	}
	public String toString() {
		return new ToStringBuilder(this).append("dealerID", dealerID)
				.append("vehicleID", vehicleID)
				.append("validDate", validDate)
				.append("validEnd", validEnd)
				.getStringBuffer().toString();
	}
}
