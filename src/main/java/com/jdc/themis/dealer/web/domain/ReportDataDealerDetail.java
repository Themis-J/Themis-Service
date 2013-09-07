package com.jdc.themis.dealer.web.domain;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.google.common.collect.Lists;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ReportDataDealerDetail implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String code;
	private ReportDataDealerDetailAmount revenue = new ReportDataDealerDetailAmount();
	private ReportDataDealerDetailAmount expense = new ReportDataDealerDetailAmount();
	private ReportDataDealerDetailAmount margin = new ReportDataDealerDetailAmount();
	private ReportDataDealerDetailAmount nonRecurrentPnL = new ReportDataDealerDetailAmount();
	@XmlElement(name = "department")
	private List<ReportDataDealerDepartmentDetail> detail = Lists.newArrayList();

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public ReportDataDealerDetailAmount getNonRecurrentPnL() {
		return nonRecurrentPnL;
	}
	public void setNonRecurrentPnL(ReportDataDealerDetailAmount nonRecurrentPnL) {
		this.nonRecurrentPnL = nonRecurrentPnL;
	}
	public ReportDataDealerDetailAmount getMargin() {
		return margin;
	}
	public void setMargin(ReportDataDealerDetailAmount margin) {
		this.margin = margin;
	}
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
	public ReportDataDealerDetailAmount getRevenue() {
		return revenue;
	}
	public void setRevenue(ReportDataDealerDetailAmount revenue) {
		this.revenue = revenue;
	}
	public ReportDataDealerDetailAmount getExpense() {
		return expense;
	}
	public void setExpense(ReportDataDealerDetailAmount expense) {
		this.expense = expense;
	}
	public List<ReportDataDealerDepartmentDetail> getDetail() {
		return detail;
	}
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("id", id)
				.append("name", name)
				.append("revenue", revenue)
				.append("margin", margin)
				.append("expense", expense)
				.append("nonRecurrentPnL", nonRecurrentPnL)
				.append("dealer", detail)
				.getStringBuffer().toString();
	}
}
