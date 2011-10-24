package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;

public abstract class TaxAgencyRequirement extends
		ListRequirement<ClientTAXAgency> {

	public TaxAgencyRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<ClientTAXAgency> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected Record createRecord(ClientTAXAgency value) {
		Record record = new Record(value);
		record.add("Name", value.getName());
		return record;
	}

	@Override
	protected String getDisplayValue(ClientTAXAgency value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected String getCreateCommandString() {
		return getMessages().create(getConstants().taxAgency());
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getConstants().taxAgency());
	}

}
