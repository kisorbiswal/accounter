package com.vimukti.accounter.main;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.vimukti.accounter.web.client.AbstractGlobal;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.server.i18n.ServerSideMessages;

public class ServerGlobal extends AbstractGlobal {

	private Map<Locale, AccounterMessages> messages = new HashMap<Locale, AccounterMessages>();

	public ServerGlobal() throws IOException {
	}

	private AccounterMessages createAccounterMessages(Locale locale)
			throws IOException {
		AccounterMessages messages = ServerSideMessages.get(AccounterMessages.class,
				locale.getLanguage());
		return messages;
	}

	@Override
	public ClientCompanyPreferences preferences() {
		return CompanyPreferenceThreadLocal.get();
	}

	@Override
	public AccounterMessages messages() {
		Locale locale = ServerLocal.get();
		AccounterMessages accounterMessages = this.messages.get(locale);
		if (accounterMessages == null) {
			try {
				accounterMessages = createAccounterMessages(locale);
				this.messages.put(locale, accounterMessages);
			} catch (IOException e) {
				throw new RuntimeException();
			}
		}
		return accounterMessages;
	}

	@Override
	public String toCurrencyFormat(double amount) {
		DecimalFormat format = new DecimalFormat("#,##0.00");
		return format.format(amount);
	}

}
