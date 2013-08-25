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

	public Instant getTimestamp();

	public void setTimestamp(Instant timestamp);

	public Instant getTimeEnd();

	public void setTimeEnd(Instant timeEnd);

	public LocalDate getValidDate();
	
	public void setValidDate(LocalDate validDate);
	
}
