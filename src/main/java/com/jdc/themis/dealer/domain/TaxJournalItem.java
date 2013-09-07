package com.jdc.themis.dealer.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.time.Instant;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.jdc.themis.dealer.data.hibernate.type.PersistentTimestamp;

@TypeDef(
		   name = "datetime",
		   typeClass = PersistentTimestamp.class
		)
@Entity
public class TaxJournalItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String name;
	@Type(type="datetime")
	private Instant timestamp;
	
	// Kai: you might find it' strange to declare this custom type on both type and get function
	// but suprised that it will fail either embedded database or postgres if I remove one of them. 
	// Damn!
	@Type(type="datetime")
	public Instant getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}
	@Id
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
	public String toString() {
		return new ToStringBuilder(this).append("id", id)
				.append("name", name)
				.getStringBuffer().toString();
	}
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}
