package com.jdc.themis.dealer.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.time.Instant;
import javax.time.calendar.LocalDate;

import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.jdc.themis.dealer.data.hibernate.type.PersistentLocalDate;
import com.jdc.themis.dealer.data.hibernate.type.PersistentTimestamp;

/**
 * Common mapped super class for all bi-temporal entities. 
 * 
 * @author chen386_2000
 * 
 */

@FilterDefs(
		{
			@org.hibernate.annotations.FilterDef(name="referenceTime", 
					defaultCondition="timestamp < :referenceTime and timeEnd >= :referenceTime", 
					parameters = {@org.hibernate.annotations.ParamDef(name="referenceTime", type="datetime")}), 
			@org.hibernate.annotations.FilterDef(name="referenceDate", defaultCondition="validDate = :referenceDate", 
			parameters = {@org.hibernate.annotations.ParamDef(name="referenceDate", type="localdate")}), 
		}
		)
@TypeDefs({ @TypeDef(name = "datetime", typeClass = PersistentTimestamp.class),
		@TypeDef(name = "localdate", typeClass = PersistentLocalDate.class) })
@MappedSuperclass
@Embeddable
public class TemporalEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	public static String REFTIME_FILER = "referenceTime";
	public static String REFDATE_FILER = "referenceDate";

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

}
