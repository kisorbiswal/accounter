package com.vimukti.accounter.main;

import java.io.IOException;

import com.google.gwt.i18n.client.DateTimeFormatInfo;
import com.google.gwt.i18n.client.DefaultDateTimeFormatInfo;
import com.vimukti.accounter.web.client.AbstractGlobal;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.i18n.AccounterNumberFormat;
import com.vimukti.accounter.web.server.i18n.ServerSideMessages;

public class ServerGlobal extends AbstractGlobal {

	AccounterMessages messages = ServerSideMessages
			.get(AccounterMessages.class);

	public ServerGlobal() throws IOException {
	}

	@Override
	public ClientCompanyPreferences preferences() {
		return CompanyPreferenceThreadLocal.get();
	}

	@Override
	public AccounterMessages messages() {
		return messages;
	}

	@Override
	public String toCurrencyFormat(double amount, String currencyCode) {
		// AccounterNumberFormat nf = AccounterNumberFormat.getCurrencyFormat();
		// return nf.format(amount, currencyCode);
		return currencyCode + amount;
	}

	@Override
	public DateTimeFormatInfo createDateTimeFormatInfo() {
		return new DefaultDateTimeFormatInfo();
	}
}
