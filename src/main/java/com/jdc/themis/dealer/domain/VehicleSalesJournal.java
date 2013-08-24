package com.jdc.themis.dealer.domain;

import java.io.Serializable;
import java.math.BigDecimal;

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
public class VehicleSalesJournal implements TemporalEntity, Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer dealerID;
	private BigDecimal amount;
	private String updateBy;
	private Integer departmentID;
	private Integer count;
	private Integer countType;
	private BigDecimal margin;
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
	
	
	public Integer getDepartmentID() {
		return departmentID;
	}
	public void setDepartmentID(Integer departmentID) {
		this.departmentID = departmentID;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getCountType() {
		return countType;
	}
	public void setCountType(Integer countType) {
		this.countType = countType;
	}
	public BigDecimal getMargin() {
		return margin;
	}
	public void setMargin(BigDecimal margin) {
		this.margin = margin;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getDealerID() {
		return dealerID;
	}
	public void setDealerID(Integer dealerID) {
		this.dealerID = dealerID;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

}
