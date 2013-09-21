package com.jdc.themis.dealer.web.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ReportDataDepartmentDetail implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private ReportDataDetailAmount revenue = new ReportDataDetailAmount();
	private ReportDataDetailAmount expense = new ReportDataDetailAmount();
	private ReportDataDetailAmount margin = new ReportDataDetailAmount();
	private ReportDataDetailAmount opProfit = new ReportDataDetailAmount();
	
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
	
	public ReportDataDetailAmount getRevenue() {
		return revenue;
	}
	public void setRevenue(ReportDataDetailAmount revenue) {
		this.revenue = revenue;
	}
	public ReportDataDetailAmount getExpense() {
		return expense;
	}
	public void setExpense(ReportDataDetailAmount expense) {
		this.expense = expense;
	}
	public ReportDataDetailAmount getMargin() {
		return margin;
	}
	public void setMargin(ReportDataDetailAmount margin) {
		this.margin = margin;
	}
	public ReportDataDetailAmount getOpProfit() {
		return opProfit;
	}
	public void setOpProfit(ReportDataDetailAmount opProfit) {
		this.opProfit = opProfit;
	}
	
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("id", id)
				.append("name", name)
				.append("revenue", revenue)
				.append("margin", margin)
				.append("expense", expense)
				.append("opProfit", opProfit)
				.getStringBuffer().toString();
	}
}
