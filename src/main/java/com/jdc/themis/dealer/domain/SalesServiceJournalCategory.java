package com.jdc.themis.dealer.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SalesServiceJournalCategory implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	
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

}
