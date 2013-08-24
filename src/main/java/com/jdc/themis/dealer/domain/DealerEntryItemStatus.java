package com.jdc.themis.dealer.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.time.Instant;
import javax.time.calendar.LocalDate;

import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.jdc.themis.dealer.data.hibernate.type.PersistentLocalDate;
import com.jdc.themis.dealer.data.hibernate.type.PersistentTimestamp;

@FilterDefs(
		{
			@org.hibernate.annotations.FilterDef(name="transactionTimeFilter", 
					defaultCondition="timestamp < :referenceTime and timeEnd >= :referenceTime", 
					parameters = {@org.hibernate.annotations.ParamDef(name="referenceTime", type="datetime")}), 
			@org.hibernate.annotations.FilterDef(name="validDateFilter", defaultCondition="validDate = :referenceDate", 
			parameters = {@org.hibernate.annotations.ParamDef(name="referenceDate", type="localdate")}), 
		}
		)
@TypeDefs({ @TypeDef(name = "datetime", typeClass = PersistentTimestamp.class),
	@TypeDef(name = "localdate", typeClass = PersistentLocalDate.class)})
@Entity
public class DealerEntryItemStatus implements TemporalEntity, Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer entryItemID;
	private Integer dealerID;
	private String updateBy;
	@Id
	@Type(type = "datetime")
	private Instant timestamp;
	@Type(type = "datetime")
	private Instant timeEnd;
	@Id
	@Type(type = "localdate")
	private LocalDate validDate;

	@Type(type="datetime")
	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	@Type(type="datetime")
	public Instant getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(Instant timeEnd) {
		this.timeEnd = timeEnd;
	}

	@Type(type="localdate")
	public LocalDate getValidDate() {
		return validDate;
	}

	public void setValidDate(LocalDate validDate) {
		this.validDate = validDate;
	}
	
	@Id
	public Integer getEntryItemID() {
		return entryItemID;
	}
	public void setEntryItemID(Integer entryItemID) {
		this.entryItemID = entryItemID;
	}
	@Id
	public Integer getDealerID() {
		return dealerID;
	}
	public void setDealerID(Integer dealerID) {
		this.dealerID = dealerID;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

}
