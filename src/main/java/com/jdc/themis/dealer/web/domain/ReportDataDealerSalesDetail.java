package com.jdc.themis.dealer.web.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ReportDataDealerSalesDetail implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String code;
	private ReportDataDetailAmount retail = new ReportDataDetailAmount();
	private ReportDataDetailAmount wholesale = new ReportDataDetailAmount();
	private ReportDataDetailAmount other = new ReportDataDetailAmount();
	private ReportDataDetailAmount overall = new ReportDataDetailAmount();
	
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

	public ReportDataDetailAmount getRetail() {
		return retail;
	}
	public void setRetail(ReportDataDetailAmount retail) {
		this.retail = retail;
	}
	public ReportDataDetailAmount getWholesale() {
		return wholesale;
	}
	public void setWholesale(ReportDataDetailAmount wholesale) {
		this.wholesale = wholesale;
	}
	public ReportDataDetailAmount getOther() {
		return other;
	}
	public void setOther(ReportDataDetailAmount other) {
		this.other = other;
	}
	public ReportDataDetailAmount getOverall() {
		return overall;
	}
	public void setOverall(ReportDataDetailAmount overall) {
		this.overall = overall;
	}
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("id", id)
				.append("name", name)
				.append("retail", retail)
				.append("wholesale", wholesale)
				.append("other", other)
				.append("overall", overall)
				.getStringBuffer().toString();
	}
}
