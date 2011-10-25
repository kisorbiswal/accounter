package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.web.client.core.TemplateAccount;

public abstract class TemplateAccountRequirement extends
		ListRequirement<TemplateAccount> {

	public TemplateAccountRequirement(String requirementName,
			String displayString, String recordName, boolean isOptional,
			boolean isAllowFromContext, ChangeListner<TemplateAccount> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected String getEmptyString() {
		return null;
	}

	@Override
	protected Record createRecord(TemplateAccount value) {
		Record itemRec = new Record(value);
		itemRec.add("", value.getName());
		itemRec.add("", value.getType());
		return itemRec;
	}

	@Override
	protected String getDisplayValue(TemplateAccount value) {
		return value.getName();
	}

	@Override
	protected String getCreateCommandString() {
		return null;
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getConstants().Account());
	}

}
