package com.vimukti.accounter.mobile.requirements;

import java.text.SimpleDateFormat;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

public class DateRequirement extends SingleRequirement<ClientFinanceDate> {

	public DateRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional2, boolean isAllowFromContext2) {
		super(requirementName, enterString, recordName, isOptional2,
				isAllowFromContext2);
		setDefaultValue(new ClientFinanceDate());
	}

	@Override
	protected String getDisplayValue(ClientFinanceDate value) {
		SimpleDateFormat format = new SimpleDateFormat(getClientCompany()
				.getPreferences().getDateFormat());
		return format.format(value.getDateAsObject());
	}

	@Override
	protected ClientFinanceDate getInputFromContext(Context context) {
		return context.getDate();
	}

}
