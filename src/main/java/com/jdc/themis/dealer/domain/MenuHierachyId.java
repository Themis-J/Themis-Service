package com.jdc.themis.dealer.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class MenuHierachyId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer parentID;
	private Integer childID;

	public Integer getParentID() {
		return parentID;
	}

	public void setParentID(Integer parentID) {
		this.parentID = parentID;
	}

	public Integer getChildID() {
		return childID;
	}

	public void setChildID(Integer childID) {
		this.childID = childID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((parentID == null) ? 0 : parentID.hashCode());
		result = prime * result + ((childID == null) ? 0 : childID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MenuHierachyId other = (MenuHierachyId) obj;
		if (parentID == null) {
			if (other.parentID != null)
				return false;
		} else if (!parentID.equals(other.parentID))
			return false;
		if (childID == null) {
			if (other.childID != null)
				return false;
		} else if (!childID.equals(other.childID))
			return false;
		return true;
	}

}
