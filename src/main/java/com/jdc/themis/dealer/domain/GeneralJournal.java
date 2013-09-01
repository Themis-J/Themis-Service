package com.jdc.themis.dealer.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.time.Instant;
import javax.time.calendar.LocalDate;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.Type;

@FilterDefs(
		{
			@org.hibernate.annotations.FilterDef(name="generalFilter", 
					parameters = {
					@org.hibernate.annotations.ParamDef(name="referenceTime", type="com.jdc.themis.dealer.data.hibernate.type.PersistentTimestamp"), 
					@org.hibernate.annotations.ParamDef(name="referenceDate", type="com.jdc.themis.dealer.data.hibernate.type.PersistentLocalDate"), 
					@org.hibernate.annotations.ParamDef(name="departmentID", type="integer"), 
					@org.hibernate.annotations.ParamDef(name="dealerID", type="integer")}), 
			@org.hibernate.annotations.FilterDef(name="generalFilterSingleItem", 
					parameters = {
					@org.hibernate.annotations.ParamDef(name="referenceTime", type="com.jdc.themis.dealer.data.hibernate.type.PersistentTimestamp"), 
					@org.hibernate.annotations.ParamDef(name="referenceDate", type="com.jdc.themis.dealer.data.hibernate.type.PersistentLocalDate"), 
					@org.hibernate.annotations.ParamDef(name="id", type="integer"), 
					@org.hibernate.annotations.ParamDef(name="departmentID", type="integer"), 
					@org.hibernate.annotations.ParamDef(name="dealerID", type="integer")}), 
		}
		)
@Filters( {
    @Filter(name="generalFilterSingleItem", condition="validDate = :referenceDate and id = :id and departmentID = :departmentID and dealerID = :dealerID and timestamp < :referenceTime and timeEnd >= :referenceTime"), 
    @Filter(name="generalFilter", condition="validDate = :referenceDate and departmentID = :departmentID and dealerID = :dealerID and timestamp < :referenceTime and timeEnd >= :referenceTime")
} )
@Entity
public class GeneralJournal implements TemporalEntity, Serializable {

	private static final long serialVersionUID = 1L;
	public static String FILTER = "generalFilter";
	public static String FILTER_SINGLEITEM = "generalFilterSingleItem";

	@Id
	private Integer id;
	@Id
	private Integer dealerID;
	@Id
	private Integer departmentID;
	private BigDecimal amount;
	@Id
	@Type(type = "datetime")
	private Instant timestamp;
	@Type(type = "datetime")
	private Instant timeEnd;
	@Id
	@Type(type = "localdate")
	private LocalDate validDate;
	private String updatedBy;
	private Integer version;

	 @Version
	public Integer getVersion() {
		return version;
	}

	//DO NOT set this field manually, it is set by hibernate to achieve optimistic locking
	protected void setVersion(Integer version) {
		this.version = version;
	}

	
	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Id
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

	@Id
	@Type(type="localdate")
	public LocalDate getValidDate() {
		return validDate;
	}

	public void setValidDate(LocalDate validDate) {
		this.validDate = validDate;
	}
	
	
	@Id
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Id
	public Integer getDealerID() {
		return dealerID;
	}
	public void setDealerID(Integer dealerID) {
		this.dealerID = dealerID;
	}
	@Id
	public Integer getDepartmentID() {
		return departmentID;
	}
	public void setDepartmentID(Integer departmentID) {
		this.departmentID = departmentID;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public String toString() {
		return new ToStringBuilder(this).append("id", id)
				.append("dealerID", dealerID)
				.append("departmentID", departmentID)
				.append("amount", amount)
				.append("updatedBy", updatedBy)
				.append("timestamp", timestamp)
				.append("timeEnd", timeEnd)
				.append("validDate", validDate).getStringBuffer().toString();
	}
}
