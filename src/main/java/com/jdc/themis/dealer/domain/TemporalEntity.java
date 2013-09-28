package com.jdc.themis.dealer.domain;

import javax.time.Instant;
import javax.time.calendar.LocalDate;
import javax.time.calendar.LocalDateTime;
import javax.time.calendar.TimeZone;

/**
 * Common interface for all bi-temporal entities. 
 * 
 * @author Kai Chen
 * 
 */
public interface TemporalEntity {
	Instant INFINITE_TIMEEND = LocalDateTime.parse("9999-01-01T00:00:00.000").atZone(TimeZone.UTC).toInstant();
	
	Instant getTimestamp();

	void setTimestamp(Instant timestamp);

	Instant getTimeEnd();

	void setTimeEnd(Instant timeEnd);

	LocalDate getValidDate();
	
	void setValidDate(LocalDate validDate);
	
	Integer getVersion();
	
}
