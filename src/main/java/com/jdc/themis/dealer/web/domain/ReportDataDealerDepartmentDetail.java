package com.jdc.themis.dealer.web.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ReportDataDealerDepartmentDetail implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private Double amount;
	private Double monthlyAvg;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getMonthlyAvg() {
		return monthlyAvg;
	}
	public void setMonthlyAvg(Double monthlyAvg) {
		this.monthlyAvg = monthlyAvg;
	}
	
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("id", id)
				.append("name", name)
				.append("amount", amount)
				.append("monthlyAvg", monthlyAvg)
				.getStringBuffer().toString();
	}
}
