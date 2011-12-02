package com.vimukti.accounter.main;

import java.io.IOException;
import java.text.DecimalFormat;

import com.vimukti.accounter.web.client.AbstractGlobal;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.server.i18n.ServerSideMessages;

public class ServerGlobal extends AbstractGlobal {


	AccounterMessages messages = ServerSideMessages.get(AccounterMessages.class);
	
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
	public String toCurrencyFormat(double amount) {
		DecimalFormat format = new DecimalFormat("#,##0.00");
		return format.format(amount);
	}

}
