package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.main.CompanyPreferenceThreadLocal;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.ui.CoreUtils;

public abstract class CurrencyRequirement extends
		ListRequirement<ClientCurrency> {

	public CurrencyRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<ClientCurrency> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
		ClientCompanyPreferences clientCompanyPreferences = CompanyPreferenceThreadLocal
				.get();
		if (clientCompanyPreferences != null)
			setDefaultValue(CompanyPreferenceThreadLocal.get()
					.getPrimaryCurrency());

	}

	@Override
	protected Record createRecord(ClientCurrency value) {
		Record record = new Record(value);
		record.add(getMessages().currency(), value.getFormalName() + " - "
				+ value.getDisplayName());
		return record;
	}

	@Override
	protected String getDisplayValue(ClientCurrency value) {
		return value != null ? value.getFormalName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {

	}

	@Override
	protected String getSetMessage() {
		return getMessages().hasSelected(getMessages().currency());
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getMessages().currency());
	}

	@Override
	protected boolean filter(ClientCurrency e, String name) {
		return e.getName().contains(name);
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getMessages().currency());
	}

}
