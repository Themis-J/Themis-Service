package com.jdc.themis.dealer.web.domain;

import java.io.Serializable;

import javax.time.Instant;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class CompletedEntryItem implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String name;
	private Instant timestamp; 
	
	public Instant getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}
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
