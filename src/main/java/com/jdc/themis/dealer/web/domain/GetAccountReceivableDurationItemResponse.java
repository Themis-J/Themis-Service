package com.jdc.themis.dealer.web.domain;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * Response of getting general income item detail. 
 * 
 * @author Kai Chen
 *
 */
public class GetAccountReceivableDurationItemResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	private final List<AccountReceivableDurationItemDetail> items = Lists.newArrayList();
	
	public List<AccountReceivableDurationItemDetail> getItems() {
		return items;
	}
}
