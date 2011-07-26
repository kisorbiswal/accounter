package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.InvalidOperationException;

/**
 * Adjustment reason POJO.
 * 
 * @author Srikanth.J
 * 
 */
public class AdjustmentReason extends CreatableObject implements
		IAccounterServerCore {

	private String stringID;
	private String section;
	private String name;

	String createdBy;
	String lastModifier;
	FinanceDate createdDate;
	FinanceDate lastModifiedDate;

	public AdjustmentReason() {
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {
		return false;
	}

}
