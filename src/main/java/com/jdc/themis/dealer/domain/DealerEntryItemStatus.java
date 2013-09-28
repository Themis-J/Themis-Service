package com.jdc.themis.dealer.domain;

import java.io.Serializable;

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
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.jdc.themis.dealer.data.hibernate.type.PersistentLocalDate;
import com.jdc.themis.dealer.data.hibernate.type.PersistentTimestamp;

@FilterDefs(
		{
			@org.hibernate.annotations.FilterDef(name="dealerEntryItemStatusFilter", 
					parameters = {
					@org.hibernate.annotations.ParamDef(name="referenceTime", type="com.jdc.themis.dealer.data.hibernate.type.PersistentTimestamp"), 
					@org.hibernate.annotations.ParamDef(name="referenceDate", type="com.jdc.themis.dealer.data.hibernate.type.PersistentLocalDate"), 
					@org.hibernate.annotations.ParamDef(name="dealerID", type="integer")}), 
			@org.hibernate.annotations.FilterDef(name="dealerEntryItemStatusFilterSingleEntryItem", 
					parameters = {
					@org.hibernate.annotations.ParamDef(name="referenceTime", type="com.jdc.themis.dealer.data.hibernate.type.PersistentTimestamp"), 
					@org.hibernate.annotations.ParamDef(name="referenceDate", type="com.jdc.themis.dealer.data.hibernate.type.PersistentLocalDate"), 
					@org.hibernate.annotations.ParamDef(name="dealerID", type="integer"), 
					@org.hibernate.annotations.ParamDef(name="entryItemID", type="integer")}), 
		}
		)
@Filters( {
    @Filter(name="dealerEntryItemStatusFilter", condition="validDate = :referenceDate and dealerID = :dealerID and timestamp < :referenceTime and timeEnd >= :referenceTime"),
    @Filter(name="dealerEntryItemStatusFilterSingleEntryItem", condition="entryItemID = :entryItemID and validDate = :referenceDate and dealerID = :dealerID and timestamp < :referenceTime and timeEnd >= :referenceTime")
} )
@TypeDefs({ @TypeDef(name = "datetime", typeClass = PersistentTimestamp.class),
	@TypeDef(name = "localdate", typeClass = PersistentLocalDate.class)})
@Entity
public class DealerEntryItemStatus implements TemporalEntity, Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String FILTER = "dealerEntryItemStatusFilter";
	public static final String FILER_SINGLEENTRYITEM = "dealerEntryItemStatusFilterSingleEntryItem";

	@Id
	private Integer entryItemID;
	@Id
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
	
	public String toString() {
		return new ToStringBuilder(this).append("itemID", entryItemID)
				.append("dealerID", dealerID)
				.append("updatedBy", updateBy)
				.append("timestamp", timestamp)
				.append("timeEnd", timeEnd)
				.append("validDate", validDate).getStringBuffer().toString();
	}

}
