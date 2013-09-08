package com.jdc.themis.dealer.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
public class GeneralJournalCategory implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer id;
	private String name;
	private Integer categoryType;
	
	public Integer getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(Integer categoryType) {
		this.categoryType = categoryType;
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
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}
