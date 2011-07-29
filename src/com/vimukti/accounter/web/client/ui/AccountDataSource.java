package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAccount;

public class AccountDataSource extends BaseDataSource {

	public AccountDataSource(AccounterAsyncCallback<List<ClientAccount>> callback) {
		Accounter.getCompany().getAccounts();
	}
}
