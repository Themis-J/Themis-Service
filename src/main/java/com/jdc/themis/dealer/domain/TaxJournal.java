package com.jdc.themis.dealer.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.time.Instant;
import javax.time.calendar.LocalDate;

import org.hibernate.annotations.Filters;
import org.hibernate.annotations.Filter;

import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.jdc.themis.dealer.data.hibernate.type.PersistentLocalDate;
import com.jdc.themis.dealer.data.hibernate.type.PersistentTimestamp;

/**
 * Entity for tax journal.
 * 
 * @author Kai Chen
 *
 */
@FilterDefs(
		{
			@org.hibernate.annotations.FilterDef(name="taxJournalFilter", 
					parameters = {
					@org.hibernate.annotations.ParamDef(name="referenceTime", type="com.jdc.themis.dealer.data.hibernate.type.PersistentTimestamp"), 
					@org.hibernate.annotations.ParamDef(name="referenceDate", type="com.jdc.themis.dealer.data.hibernate.type.PersistentLocalDate"), 
					@org.hibernate.annotations.ParamDef(name="id", type="integer"), 
					@org.hibernate.annotations.ParamDef(name="dealerID", type="integer")}), 
			@org.hibernate.annotations.FilterDef(name="taxDateFilter", 
					parameters = {
					@org.hibernate.annotations.ParamDef(name="referenceTime", type="com.jdc.themis.dealer.data.hibernate.type.PersistentTimestamp"), 
					@org.hibernate.annotations.ParamDef(name="referenceDate", type="com.jdc.themis.dealer.data.hibernate.type.PersistentLocalDate")}),
			
		}
		)
@Filters( {
    @Filter(name="taxJournalFilter", condition="validDate = :referenceDate and id = :id and dealerID = :dealerID and timestamp < :referenceTime and timeEnd >= :referenceTime"), 
    @Filter(name="taxDateFilter", condition="validDate = :referenceDate and timestamp < :referenceTime and timeEnd >= :referenceTime group by id, dealerID, validDate")
} )
@TypeDefs({ @TypeDef(name = "datetime", typeClass = PersistentTimestamp.class),
	@TypeDef(name = "localdate", typeClass = PersistentLocalDate.class)})
@Entity
public class TaxJournal implements TemporalEntity, Serializable {
	
	public static final String FILTER = "taxJournalFilter";
	public static final String FILTER_VALIDATE = "taxDateFilter";

	private static final long serialVersionUID = 1L;
	@Id
	private Integer id;
	@Id
	private Integer dealerID;
	private BigDecimal amount;
	private String updatedBy;
	@Id
	@Type(type = "datetime")
	private Instant timestamp;
	@Type(type = "datetime")
	private Instant timeEnd;
	@Id
	@Type(type = "localdate")
	private LocalDate validDate;
	private Integer version;

	 @Version
	public Integer getVersion() {
		return version;
	}

	//DO NOT set this field manually, it is set by hibernate to achieve optimistic locking
	protected void setVersion(Integer version) {
		this.version = version;
	}

	@Type(type="datetime")
	@Id
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
	@Id
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
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String toString() {
		return new ToStringBuilder(this).append("itemID", id)
				.append("dealerID", dealerID)
				.append("amount", amount)
				.append("version", version)
				.append("updatedBy", updatedBy)
				.append("timestamp", timestamp)
				.append("timeEnd", timeEnd)
				.append("validDate", validDate).getStringBuffer().toString();
	}
}
