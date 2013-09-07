package com.jdc.themis.dealer.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;

@FilterDefs(
		{
			@org.hibernate.annotations.FilterDef(name="reportItemFilter", 
					parameters = {
					@org.hibernate.annotations.ParamDef(name="sourceItemID", type="integer"),
					@org.hibernate.annotations.ParamDef(name="itemSource", type="integer")
					}), 
		}
		)
@Filters( {
    @Filter(name="reportItemFilter", condition="sourceItemID = :sourceItemID and itemSource = :itemSource")
} )
@Entity
public class ReportItem implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String FILTER = "reportItemFilter";
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	private Integer itemSource;
	private Integer sourceItemID;
	private String itemCategory;
	
	public String getItemCategory() {
		return itemCategory;
	}
	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}
	public Integer getSourceItemID() {
		return sourceItemID;
	}
	public void setSourceItemID(Integer sourceItemID) {
		this.sourceItemID = sourceItemID;
	}
	public Integer getItemSource() {
		return itemSource;
	}
	public void setItemSource(Integer itemSource) {
		this.itemSource = itemSource;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String toString() {
		return new ToStringBuilder(this).append("id", id)
				.append("sourceItemID", sourceItemID)
				.append("itemCategory", itemCategory)
				.append("itemSource", itemSource).getStringBuffer().toString();
	}
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}
