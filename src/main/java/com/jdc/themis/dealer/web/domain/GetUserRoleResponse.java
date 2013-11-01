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
public class GetUserRoleResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "roles")
	private List<UserRoleDetail> detail = Lists.newArrayList();

	public List<UserRoleDetail> getDetail() {
		return detail;
	}
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("detail", detail)
				.getStringBuffer().toString();
	}
}
