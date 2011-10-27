package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

public class DateRequirement extends SingleRequirement<ClientFinanceDate> {

	public DateRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional2, boolean isAllowFromContext2) {
		super(requirementName, enterString, recordName, isOptional2,
				isAllowFromContext2);
	}

	@Override
	protected String getDisplayValue(ClientFinanceDate value) {
		return value.toString();
	}

	@Override
	protected ClientFinanceDate getInputFromContext(Context context) {
		return context.getDate();
	}

}
