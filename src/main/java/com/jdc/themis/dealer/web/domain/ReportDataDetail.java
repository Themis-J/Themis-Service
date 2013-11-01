package com.jdc.themis.dealer.web.domain;

import java.io.Serializable;

public abstract class ReportDataDetail implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer year;
	private Integer month;
	
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
}
