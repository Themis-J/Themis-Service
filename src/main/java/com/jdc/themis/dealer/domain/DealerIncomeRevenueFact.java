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
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.Type;

@FilterDefs(
		{
			@org.hibernate.annotations.FilterDef(name="dirfFilter", 
					parameters = {
					@org.hibernate.annotations.ParamDef(name="referenceTime", type="com.jdc.themis.dealer.data.hibernate.type.PersistentTimestamp"), 
					@org.hibernate.annotations.ParamDef(name="timeID", type="long"), 
					@org.hibernate.annotations.ParamDef(name="dealerID", type="integer"), 
					@org.hibernate.annotations.ParamDef(name="itemID", type="long"), 
					@org.hibernate.annotations.ParamDef(name="departmentID", type="integer")}), 
			@org.hibernate.annotations.FilterDef(name="dirfAllFilter", 
					parameters = {
					@org.hibernate.annotations.ParamDef(name="referenceTime", type="com.jdc.themis.dealer.data.hibernate.type.PersistentTimestamp"), 
					@org.hibernate.annotations.ParamDef(name="timeID", type="long"), 
					}), 
			@org.hibernate.annotations.FilterDef(name="dirfRefTimeFilter", 
					parameters = {
					@org.hibernate.annotations.ParamDef(name="referenceTime", type="com.jdc.themis.dealer.data.hibernate.type.PersistentTimestamp"), 
					}), 
		}
		)
@Filters( {
    @Filter(name="dirfFilter", condition="timeID = :timeID and dealerID = :dealerID and itemID = :itemID and departmentID = :departmentID and timestamp < :referenceTime and timeEnd >= :referenceTime"), 
    @Filter(name="dirfAllFilter", condition="timeID = :timeID and timestamp < :referenceTime and timeEnd >= :referenceTime"), 
    @Filter(name="dirfDepFilter", condition="timeID = :timeID and departmentID = :departmentID and timestamp < :referenceTime and timeEnd >= :referenceTime"), 
} )
@Entity
public class DealerIncomeRevenueFact implements Serializable, TemporalEntity {

	private static final long serialVersionUID = 1L;
	
	public static final String FILTER = "dirfFilter";
	public static final String FILTER_ALL = "dirfAllFilter";
	public static final String FILTER_REFTIME = "dirfRefTimeFilter";

	@Id
	private Long timeID;
	@Id
	private Integer dealerID;
	@Id
	private Integer departmentID;
	@Id
	private Long itemID;
	private BigDecimal amount;
	private BigDecimal margin;
	private Integer count;
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
	public Long getTimeID() {
		return timeID;
	}
	public void setTimeID(Long timeID) {
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
	public Long getItemID() {
		return itemID;
	}
	public void setItemID(Long itemID) {
		this.itemID = itemID;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getMargin() {
		return margin;
	}
	public void setMargin(BigDecimal margin) {
		this.margin = margin;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
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
	public String toString() {
		return new ToStringBuilder(this).append("timeID", timeID)
				.append("dealerID", dealerID)
				.append("itemID", itemID)
				.append("departmentID", departmentID)
				.append("amount", amount)
				.append("margin", margin)
				.append("count", count)
				.getStringBuffer().toString();
	}
}
