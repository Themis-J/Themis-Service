package com.jdc.themis.dealer.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;

@FilterDefs(
		{
			@org.hibernate.annotations.FilterDef(name="ssjiFilter", 
					parameters = {
					@org.hibernate.annotations.ParamDef(name="categoryID", type="integer")}), 
		}
		)
@Filters( {
    @Filter(name="ssjiFilter", condition="categoryID = :categoryID")
} )
@Entity
public class SalesServiceJournalItem implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String FILTER = "ssjiFilter";
	
	private Integer id;
	private String name;
	private Integer categoryID;
	private Integer journalType;
	
	public Integer getJournalType() {
		return journalType;
	}
	public void setJournalType(Integer journalType) {
		this.journalType = journalType;
	}
	@Id
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getCategoryID() {
		return categoryID;
	}
	public void setCategoryID(Integer categoryID) {
		this.categoryID = categoryID;
	}
	public String toString() {
		return new ToStringBuilder(this).append("id", id)
				.append("name", name)
				.getStringBuffer().toString();
	}
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}
