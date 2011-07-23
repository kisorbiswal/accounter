package com.vimukti.accounter.core;

import java.sql.Timestamp;

public abstract class CreatableObject {

	protected long id;

	protected User createdBy;
	protected User lastModifier;
	protected Timestamp createdDate;
	protected Timestamp lastModifiedDate;

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public User getCreatedBy() {
		return this.createdBy;
	}

	public void setLastModifier(User lastModifier) {
		this.lastModifier = lastModifier;
	}

	public User getLastModifier() {
		return this.lastModifier;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public long getID() {
		return id;
	}
}
