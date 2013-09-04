package com.jdc.themis.dealer.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.time.Instant;
import javax.time.calendar.LocalDate;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;

@Entity
public class DealerIncomeExpenseFact implements Serializable, TemporalEntity {

	private static final long serialVersionUID = 1L;

	@Id
	private Integer timeID;
	@Id
	private Integer dealerID;
	@Id
	private Integer departmentID;
	@Id
	private Integer itemID;
	private BigDecimal amount;
	@Id
	@Type(type = "datetime")
	private Instant timestamp;
	@Type(type = "datetime")
	private Instant timeEnd;
	private Integer version;

	 @Version
	public Integer getVersion() {
		return version;
	}

	//DO NOT set this field manually, it is set by hibernate to achieve optimistic locking
	protected void setVersion(Integer version) {
		this.version = version;
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
	public Integer getTimeID() {
		return timeID;
	}
	public void setTimeID(Integer timeID) {
		this.timeID = timeID;
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
	@Id
	public Integer getItemID() {
		return itemID;
	}
	public void setItemID(Integer itemID) {
		this.itemID = itemID;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String toString() {
		return new ToStringBuilder(this).append("timeID", timeID)
				.append("dealerID", dealerID)
				.append("itemID", itemID)
				.append("departmentID", departmentID)
				.append("amount", amount)
				.getStringBuffer().toString();
	}

	@Override
	@Transient
	public LocalDate getValidDate() {
		throw new UnsupportedOperationException();
	}

	@Override
	@Transient
	public void setValidDate(LocalDate validDate) {
		throw new UnsupportedOperationException();
	}
	
}
