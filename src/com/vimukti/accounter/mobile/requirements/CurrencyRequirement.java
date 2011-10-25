package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.web.client.core.ClientCurrency;

public abstract class CurrencyRequirement extends
		ListRequirement<ClientCurrency> {

	public CurrencyRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<ClientCurrency> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected Record createRecord(ClientCurrency value) {
		Record record = new Record(value);
		record.add("", value.getName());
		return record;
	}

	@Override
	protected String getDisplayValue(ClientCurrency value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected String getCreateCommandString() {
		return null;
	}

	@Override
	protected String getSetMessage() {
		return "Curency has been selected.";
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getConstants().currency());
	}

	@Override
	protected boolean filter(ClientCurrency e, String name) {
		return e.getName().contains(name);
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getConstants().currency());
	}
}
