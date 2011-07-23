package com.vimukti.accounter.core;

import java.sql.Timestamp;

public abstract class CreatableObject {

	protected long createdBy;
	protected long lastModifier;
	protected Timestamp createdDate;
	protected Timestamp lastModifiedDate;

	void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
	}

	long getCreatedBy() {
		return this.createdBy;
	}

	void setLastModifier(long lastModifier) {
		this.lastModifier = lastModifier;
	}

	long getLastModifier() {
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
}
