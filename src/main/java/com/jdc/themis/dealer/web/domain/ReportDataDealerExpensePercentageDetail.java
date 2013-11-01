package com.jdc.themis.dealer.web.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ReportDataDealerExpensePercentageDetail implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String code;
	private ReportDataDetailAmount amount = new ReportDataDetailAmount();
	private ReportDataDetailAmount currentMargin = new ReportDataDetailAmount();
	private ReportDataDetailAmount previousMargin = new ReportDataDetailAmount();

	public ReportDataDetailAmount getAmount() {
		return amount;
	}
	public void setAmount(ReportDataDetailAmount amount) {
		this.amount = amount;
	}
	public ReportDataDetailAmount getCurrentMargin() {
		return currentMargin;
	}
	public void setCurrentMargin(ReportDataDetailAmount currentMargin) {
		this.currentMargin = currentMargin;
	}
	public ReportDataDetailAmount getPreviousMargin() {
		return previousMargin;
	}
	public void setPreviousMargin(ReportDataDetailAmount previousMargin) {
		this.previousMargin = previousMargin;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
	
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("id", id)
				.append("name", name)
				.append("amount", amount)
				.append("currentMargin", currentMargin)
				.append("previousMargin", previousMargin)
				.getStringBuffer().toString();
	}
}
