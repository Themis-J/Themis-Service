package com.jdc.themis.dealer.domain;

import javax.time.Instant;
import javax.time.calendar.LocalDate;

/**
 * Common interface for all bi-temporal entities. 
 * 
 * @author Kai Chen
 * 
 */
public interface TemporalEntity {

	Instant getTimestamp();

	void setTimestamp(Instant timestamp);

	Instant getTimeEnd();

	void setTimeEnd(Instant timeEnd);

	LocalDate getValidDate();
	
	void setValidDate(LocalDate validDate);
	
	Integer getVersion();
	
}
