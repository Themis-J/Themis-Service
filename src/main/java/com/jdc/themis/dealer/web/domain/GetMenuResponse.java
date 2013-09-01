package com.jdc.themis.dealer.web.domain;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import com.google.common.collect.Lists;
import javax.xml.bind.annotation.XmlAccessType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GetMenuResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name = "menu")
	private final List<MenuDetail> items = Lists.newArrayList();

	public List<MenuDetail> getItems() {
		return items;
	}

}
