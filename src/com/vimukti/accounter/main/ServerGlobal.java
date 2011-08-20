package com.vimukti.accounter.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.mattbertolini.hermes.Hermes;
import com.vimukti.accounter.web.client.IGlobal;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;

public class ServerGlobal implements IGlobal {

	private Map<Locale, AccounterConstants> constents = new HashMap<Locale, AccounterConstants>();

	public ServerGlobal() throws IOException {
	}

	@Override
	public AccounterConstants constants() {
		Locale locale = ServerLocal.get();
		AccounterConstants accounterConstants = this.constents.get(locale);
		if(accounterConstants==null){
			try {
				accounterConstants=	createAccounterConstants(locale);
				this.constents.put(locale, accounterConstants);
			} catch (IOException e) {
				throw new RuntimeException();
			}
		}
		return accounterConstants;
	}

	private AccounterConstants createAccounterConstants(Locale locale) throws IOException {
		AccounterConstants constants=Hermes.get(AccounterConstants.class, locale.getLanguage());
		return constants;
	}

	@Override
	public ClientCompanyPreferences preferences() {
		return CompanyPreferenceThreadLocal.get();
	}

	

}
