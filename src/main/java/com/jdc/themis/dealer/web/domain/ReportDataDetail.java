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
public class ReportDataDetail implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer year;
	private Integer month;
	@XmlElement(name = "dealer")
	private List<ReportDataDealerDetail> detail = Lists.newArrayList();
	@XmlElement(name = "department")
	private List<ReportDataDepartmentDetail> departmentDetail = Lists.newArrayList();
	@XmlElement(name = "sales")
	private List<ReportDataDealerSalesDetail> salesDetail = Lists.newArrayList();
	
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public List<ReportDataDealerDetail> getDetail() {
		return detail;
	}
	public List<ReportDataDepartmentDetail> getDepartmentDetail() {
		return departmentDetail;
	}
	public List<ReportDataDealerSalesDetail> getSalesDetail() {
		return salesDetail;
	}
	
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("year", year)
				.append("month", month)
				.append("dealer", detail)
				.append("department", departmentDetail)
				.append("sales", salesDetail)
				.getStringBuffer().toString();
	}
}
