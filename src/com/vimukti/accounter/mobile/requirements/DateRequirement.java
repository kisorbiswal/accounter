package com.vimukti.accounter.mobile.requirements;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.InputType;
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
		SimpleDateFormat format = new SimpleDateFormat(getPreferences()
				.getDateFormat());
		return format.format(value.getDateAsObject());
	}

	@Override
	protected ClientFinanceDate getInputFromContext(Context context) {
		String string = context.getString();
		Date date;
		try {
			date = new Date(Long.parseLong(string));
		} catch (Exception e) {
			date = new Date(string);
		}
		ClientFinanceDate clientFinanceDate = new ClientFinanceDate();
		clientFinanceDate.setDay(date.getDate());
		clientFinanceDate.setMonth(date.getMonth() + 1);
		clientFinanceDate.setYear(date.getYear() + 1900);
		return clientFinanceDate;
	}

	@Override
	public InputType getInputType() {
		return new InputType(INPUT_TYPE_DATE);
	}
}
