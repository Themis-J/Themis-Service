package com.jdc.themis.dealer.web.domain;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Lists;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GetDepartmentResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name = "department")
	private final List<DepartmentDetail> items = Lists.newArrayList();

	public List<DepartmentDetail> getItems() {
		return items;
	}

}
