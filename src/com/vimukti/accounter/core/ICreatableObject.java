package com.vimukti.accounter.core;

import java.sql.Timestamp;

public interface ICreatableObject {

	void setCreatedBy(long createdBy);

	long getCreatedBy();

	void setLastModifier(long lastModifier);

	long getLastModifier();

	void setCreatedDate(Timestamp createdDate);

	Timestamp getCreatedDate();

	void setLastModifiedDate(Timestamp lastModifiedDate);

	Timestamp getLastModifiedDate();
}
