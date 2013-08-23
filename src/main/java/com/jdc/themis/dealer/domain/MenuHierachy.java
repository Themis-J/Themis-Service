package com.jdc.themis.dealer.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class MenuHierachy implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private MenuHierachyId menuHierachyID;
	
	public MenuHierachyId getMenuHierachyID() {
		return menuHierachyID;
	}
	public void setMenuHierachyID(MenuHierachyId menuHierachyID) {
		this.menuHierachyID = menuHierachyID;
	}
	private Integer itemOrder;
	public Integer getItemOrder() {
		return itemOrder;
	}
	public void setItemOrder(Integer itemOrder) {
		this.itemOrder = itemOrder;
	}

}
