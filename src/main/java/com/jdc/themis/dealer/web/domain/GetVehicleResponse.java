package com.jdc.themis.dealer.web.domain;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Lists;

@XmlRootElement
public class GetVehicleResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private final List<VehicleItem> items = Lists.newArrayList();

	public List<VehicleItem> getItems() {
		return items;
	}

}
