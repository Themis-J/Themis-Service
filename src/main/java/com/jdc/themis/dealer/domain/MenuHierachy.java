package com.jdc.themis.dealer.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
public class MenuHierachy implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private MenuHierachyId menuHierachyID;
	private Integer itemOrder;
	
	public MenuHierachyId getMenuHierachyID() {
		return menuHierachyID;
	}
	public void setMenuHierachyID(MenuHierachyId menuHierachyID) {
		this.menuHierachyID = menuHierachyID;
	}
	public Integer getItemOrder() {
		return itemOrder;
	}
	public void setItemOrder(Integer itemOrder) {
		this.itemOrder = itemOrder;
	}
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}
