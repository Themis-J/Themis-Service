package com.jdc.themis.dealer.web.domain;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Lists;

@XmlRootElement
public class GetDepartmentResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private final List<DepartmentDetail> items = Lists.newArrayList();

	public List<DepartmentDetail> getItems() {
		return items;
	}

}
