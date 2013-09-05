package com.jdc.themis.dealer.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
			@org.hibernate.annotations.FilterDef(name="reportTimeValidDateFilter", 
					parameters = {
					@org.hibernate.annotations.ParamDef(name="referenceDate", type="com.jdc.themis.dealer.data.hibernate.type.PersistentLocalDate")
					}), 
			@org.hibernate.annotations.FilterDef(name="reportTimeYearFilter", 
					parameters = {
					@org.hibernate.annotations.ParamDef(name="year", type="integer")
					}), 
			@org.hibernate.annotations.FilterDef(name="reportTimeMonthFilter", 
					parameters = {
					@org.hibernate.annotations.ParamDef(name="year", type="integer"),
					@org.hibernate.annotations.ParamDef(name="monthOfYear", type="integer")
					}), 
		}
		)
@Filters( {
    @Filter(name="reportTimeValidDateFilter", condition="validDate = :referenceDate"), 
    @Filter(name="reportTimeYearFilter", condition="year = :year"), 
    @Filter(name="reportTimeMonthFilter", condition="year = :year and monthOfYear = :monthOfYear")
} )
@TypeDefs({ @TypeDef(name = "datetime", typeClass = PersistentTimestamp.class),
	@TypeDef(name = "localdate", typeClass = PersistentLocalDate.class)})
@Entity
public class ReportTime implements Serializable {

	private static final long serialVersionUID = 1L;
	public static String FILTER = "reportTimeValidDateFilter";
	public static String FILTER_YEAR = "reportTimeYearFilter";
	public static String FILTER_MONTH = "reportTimeMonthFilter";
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@Type(type = "localdate")
	private LocalDate validDate;
	private Integer monthOfYear;
	private Integer year;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Type(type = "localdate")
	public LocalDate getValidDate() {
		return validDate;
	}
	public void setValidDate(LocalDate validDate) {
		this.validDate = validDate;
	}
	public Integer getMonthOfYear() {
		return monthOfYear;
	}
	public void setMonthOfYear(Integer monthOfYear) {
		this.monthOfYear = monthOfYear;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public String toString() {
		return new ToStringBuilder(this).append("id", id)
				.append("month", monthOfYear)
				.append("year", year)
				.append("validDate", validDate).getStringBuffer().toString();
	}
}
