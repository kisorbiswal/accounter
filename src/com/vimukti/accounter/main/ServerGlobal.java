package com.vimukti.accounter.main;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.mattbertolini.hermes.Hermes;
import com.vimukti.accounter.web.client.AbstractGlobal;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public class ServerGlobal extends AbstractGlobal {

	private Map<Locale, AccounterConstants> constents = new HashMap<Locale, AccounterConstants>();
	private Map<Locale, AccounterMessages> messages = new HashMap<Locale, AccounterMessages>();

	public ServerGlobal() throws IOException {
	}

	@Override
	public AccounterConstants constants() {
		Locale locale = ServerLocal.get();
		AccounterConstants accounterConstants = this.constents.get(locale);
		if (accounterConstants == null) {
			try {
				accounterConstants = createAccounterConstants(locale);
				this.constents.put(locale, accounterConstants);
			} catch (IOException e) {
				throw new RuntimeException();
			}
		}
		return accounterConstants;
	}

	private AccounterConstants createAccounterConstants(Locale locale)
			throws IOException {
		AccounterConstants constants = Hermes.get(AccounterConstants.class,
				locale.getLanguage());
		return constants;
	}

	private AccounterMessages createAccounterMessages(Locale locale)
			throws IOException {
		AccounterMessages messages = Hermes.get(AccounterMessages.class,
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
