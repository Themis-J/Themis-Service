package com.jdc.themis.dealer.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.jdc.themis.dealer.data.hibernate.type.PersistentDecimal;

@TypeDef (
	name = "decimal", typeClass = PersistentDecimal.class
)
@Entity
public class VehicleSalesJournal extends TemporalEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer dealerID;
	@Type(type="decimal")
	private BigDecimal amount;
	private String updateBy;
	private Integer departmentID;
	private Integer count;
	private Integer countType;
	@Type(type="decimal")
	private BigDecimal margin;
	
	public Integer getDepartmentID() {
		return departmentID;
	}
	public void setDepartmentID(Integer departmentID) {
		this.departmentID = departmentID;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getCountType() {
		return countType;
	}
	public void setCountType(Integer countType) {
		this.countType = countType;
	}
	public BigDecimal getMargin() {
		return margin;
	}
	public void setMargin(BigDecimal margin) {
		this.margin = margin;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getDealerID() {
		return dealerID;
	}
	public void setDealerID(Integer dealerID) {
		this.dealerID = dealerID;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

}
