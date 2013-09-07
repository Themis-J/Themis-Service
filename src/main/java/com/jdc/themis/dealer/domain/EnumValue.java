package com.jdc.themis.dealer.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
public class EnumValue implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	private Integer typeID;
	@Id
	private Integer value;
	private String name;
	
	@Id
	public Integer getTypeID() {
		return typeID;
	}


	public void setTypeID(Integer typeID) {
		this.typeID = typeID;
	}

	@Id
	public Integer getValue() {
		return value;
	}


	public void setValue(Integer value) {
		this.value = value;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return new ToStringBuilder(this).append("typeId", typeID)
				.append("name", name)
				.append("value", value)
				.getStringBuffer().toString();
	}
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}
