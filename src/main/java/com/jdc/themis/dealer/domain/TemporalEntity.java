package com.jdc.themis.dealer.domain;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;
import javax.time.Instant;
import javax.time.calendar.LocalDate;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.jdc.themis.dealer.data.hibernate.type.PersistentLocalDate;
import com.jdc.themis.dealer.data.hibernate.type.PersistentTimestamp;

/**
 * 
 * @author chen386_2000
 * 
 * @TODO: finish implementing temporal entity.
 * 
 */
@TypeDefs({ @TypeDef(name = "datetime", typeClass = PersistentTimestamp.class),
		@TypeDef(name = "localdate", typeClass = PersistentLocalDate.class) })
@MappedSuperclass
public class TemporalEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	public static String REFTIME_FILER = "referenceTime";

	@Type(type = "datetime")
	private Instant timestamp;
	@Type(type = "datetime")
	private Instant timeEnd;
	@Type(type = "localdate")
	private LocalDate validDate;

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public Instant getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(Instant timeEnd) {
		this.timeEnd = timeEnd;
	}

	public LocalDate getValidDate() {
		return validDate;
	}

	public void setValidDate(LocalDate validDate) {
		this.validDate = validDate;
	}

}
