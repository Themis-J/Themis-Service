package com.jdc.themis.dealer.web.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * The item of the general income journal. 
 * 
 * @author Kai Chen
 *
 */
@XmlRootElement
public class HumanResourceAllocationItemDetail implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String name;
	
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
