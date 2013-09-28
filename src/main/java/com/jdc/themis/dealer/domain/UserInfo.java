package com.jdc.themis.dealer.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.time.Instant;
import javax.time.calendar.LocalDate;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.jdc.themis.dealer.data.hibernate.type.PersistentTimestamp;

@FilterDefs(
		{
			@org.hibernate.annotations.FilterDef(name="userInfoFilter", 
					parameters = {
					@org.hibernate.annotations.ParamDef(name="referenceTime", type="com.jdc.themis.dealer.data.hibernate.type.PersistentTimestamp"), 
					@org.hibernate.annotations.ParamDef(name="username", type="string")}), 
		}
		)
@Filters( {
    @Filter(name="userInfoFilter", condition="username = :username and timestamp < :referenceTime and timeEnd >= :referenceTime"), 
} )
@TypeDefs({ @TypeDef(name = "datetime", typeClass = PersistentTimestamp.class),})
@Entity
public class UserInfo implements TemporalEntity, Serializable {
	private static final long serialVersionUID = 1L;
	public static final String FILTER = "userInfoFilter";
	
	@Id
	private String username;
	private String password;
	private Integer userRoleID;
	private Boolean active;
	private Integer dealerID;
	@Id
	@Type(type = "datetime")
	private Instant timestamp;
	@Type(type = "datetime")
	private Instant timeEnd;
	private Integer version;
	private String updatedBy;
	
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Integer getDealerID() {
		return dealerID;
	}
	public void setDealerID(Integer dealerID) {
		this.dealerID = dealerID;
	}
	@Id
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getUserRoleID() {
		return userRoleID;
	}
	public void setUserRoleID(Integer userRoleID) {
		this.userRoleID = userRoleID;
	}
	
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}

	@Type(type="datetime")
	@Id
	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	@Type(type="datetime")
	public Instant getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(Instant timeEnd) {
		this.timeEnd = timeEnd;
	}
	@Override
	@Transient
	public LocalDate getValidDate() {
		throw new UnsupportedOperationException();
	}
	@Override
	@Transient
	public void setValidDate(LocalDate validDate) {
		throw new UnsupportedOperationException();
	}
	@Version
	public Integer getVersion() {
		return version;
	}

	//DO NOT set this field manually, it is set by hibernate to achieve optimistic locking
	protected void setVersion(Integer version) {
		this.version = version;
	}
	
	public String toString() {
		return new ToStringBuilder(this)
				.append("username", username)
				.append("userRoleID", userRoleID)
				.append("dealerID", dealerID)
				.append("active", active)
				.append("updatedBy", updatedBy)
				.append("timestamp", timestamp)
				.getStringBuffer().toString();
	}
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

}
