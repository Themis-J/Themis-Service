package com.jdc.themis.dealer.web.domain;

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
public class ReportDepartmentDataList extends ReportDataDetail {

	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "detail")
	private List<ReportDataDepartmentDetail> detail = Lists.newArrayList();

	public List<ReportDataDepartmentDetail> getDetail() {
		return detail;
	}
	
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("year", getYear())
				.append("month", getMonth())
				.append("detail", detail)
				.getStringBuffer().toString();
	}
}
