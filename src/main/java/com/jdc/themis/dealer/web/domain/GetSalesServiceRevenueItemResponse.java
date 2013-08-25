package com.jdc.themis.dealer.web.domain;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

public class GetSalesServiceRevenueItemResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	private final List<SalesServiceRevenueItem> items = Lists.newArrayList();
	
	public List<SalesServiceRevenueItem> getItems() {
		return items;
	}
}
