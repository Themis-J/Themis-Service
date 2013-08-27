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
public class GetEmployeeFeeSummaryItemResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	private final List<EmployeeFeeSummaryItemDetail> items = Lists.newArrayList();
	
	public List<EmployeeFeeSummaryItemDetail> getItems() {
		return items;
	}
}
