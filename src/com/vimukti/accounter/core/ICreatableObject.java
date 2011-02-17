package com.vimukti.accounter.core;


public interface ICreatableObject {
	
	void setCreatedBy(String createdBy);

	String getCreatedBy();

	void setLastModifier(String lastModifier);

	String getLastModifier();

	void setCreatedDate(FinanceDate createdDate);

	FinanceDate getCreatedDate();

	void setLastModifiedDate(FinanceDate lastModifiedDate);

	FinanceDate getLastModifiedDate();
}
