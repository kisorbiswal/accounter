package com.vimukti.accounter.web.client;

import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;

public class ClientGlobal extends AbstractGlobal {

	@Override
	public ClientCompanyPreferences preferences() {
		return Accounter.getCompany().getPreferences();
	}

	@Override
	public AccounterMessages messages() {
		return Accounter.messages();
	}

	@Override
	public String toCurrencyFormat(double amount) {
		// NumberFormat nf = NumberFormat.getCurrencyFormat();
		// String test = nf.getPattern();
		int decimalNumber = preferences().getDecimalNumber();
		String pattern = "#,##0.";
		for (int i = 0; i < decimalNumber; i++) {
			pattern = pattern + "0";
		}
		com.google.gwt.i18n.client.NumberFormat format = com.google.gwt.i18n.client.NumberFormat
				.getFormat(pattern);
		return format.format(amount);
	}

}
