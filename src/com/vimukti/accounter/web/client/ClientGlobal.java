package com.vimukti.accounter.web.client;


import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocaleInfo;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.i18n.AccounterNumberFormat;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class ClientGlobal extends AbstractGlobal {
	private DayAndMonthUtil andMonthUtil;

	@Override
	public ClientCompanyPreferences preferences() {
		return Accounter.getCompany().getPreferences();
	}

	@Override
	public AccounterMessages messages() {
		return Accounter.getMessages();
	}

	@Override
	public AccounterNumberFormat getFormater() {
		return AccounterNumberFormat.getCurrencyFormat();
	}

	@Override
	public DayAndMonthUtil getDayAndMonthUtil() {
		if (andMonthUtil == null) {
			andMonthUtil = new DayAndMonthUtil(LocaleInfo.getCurrentLocale()
					.getDateTimeFormatInfo());
		}
		return andMonthUtil;
	}
	
	@Override
	public ClientFinanceDate stringAsFinanceDate(String date, String format) {
		if (date == null) {
			return null;
		}
		date = date.trim();
		if (date.isEmpty()) {
			return null;
		}
		DateTimeFormat dateFormatter = DateTimeFormat.getFormat(format);
		try {
			Date parse = dateFormatter.parse(date);
			return new ClientFinanceDate(parse);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
}
