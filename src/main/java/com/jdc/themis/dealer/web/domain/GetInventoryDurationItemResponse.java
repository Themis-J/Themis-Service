package com.jdc.themis.dealer.web.domain;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Lists;

/**
 * Response of getting general income item detail. 
 * 
 * @author Kai Chen
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GetInventoryDurationItemResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	@XmlElement(name = "item")
	private final List<InventoryDurationItemDetail> items = Lists.newArrayList();
	
	public List<InventoryDurationItemDetail> getItems() {
		return items;
	}
}
