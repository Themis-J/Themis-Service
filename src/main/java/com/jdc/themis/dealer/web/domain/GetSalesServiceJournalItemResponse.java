package com.jdc.themis.dealer.web.domain;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

public class GetSalesServiceJournalItemResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	private final List<SalesServiceJournalItemDetail> items = Lists.newArrayList();
	
	public List<SalesServiceJournalItemDetail> getItems() {
		return items;
	}
}
