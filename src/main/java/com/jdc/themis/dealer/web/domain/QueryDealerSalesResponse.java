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
public class QueryDealerSalesResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String reportName;
	@XmlElement(name = "report")
	private List<ReportDealerSalesDataList> detail = Lists.newArrayList();
	
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public List<ReportDealerSalesDataList> getDetail() {
		return detail;
	}
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("reportName", reportName)
				.append("report", detail)
				.getStringBuffer().toString();
	}
}
