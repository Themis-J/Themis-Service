package com.jdc.themis.dealer.web.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ReportDataDetailAmount implements Serializable{

	private static final long serialVersionUID = 1L;
	private Double amount = 0.0;
	private Double percentage = 0.0;
	private Double reference = 0.0;
	
	public Double getReference() {
		return reference;
	}

	public void setReference(Double reference) {
		this.reference = reference;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	public String toString() {
		return new ToStringBuilder(this).append("amount", amount)
				.append("percentage", percentage)
				.append("reference", reference)
				.getStringBuffer().toString();
	}
}
