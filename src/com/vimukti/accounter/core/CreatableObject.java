package com.vimukti.accounter.core;

import java.sql.Timestamp;

public abstract class CreatableObject {

	protected long id;

	protected User createdBy;
	protected User lastModifier;
	protected Timestamp createdDate;
	protected Timestamp lastModifiedDate;

	void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	User getCreatedBy() {
		return this.createdBy;
	}

	void setLastModifier(User lastModifier) {
		this.lastModifier = lastModifier;
	}

	User getLastModifier() {
		return this.lastModifier;
	}

	void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	Timestamp getCreatedDate() {
		return this.createdDate;
	}

	void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	long getID() {
		return id;
	}
}
