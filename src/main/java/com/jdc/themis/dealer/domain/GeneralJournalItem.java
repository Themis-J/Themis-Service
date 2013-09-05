package com.jdc.themis.dealer.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
public class GeneralJournalItem implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer id;
	private String name;
	private Integer categoryID;
	
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
}
