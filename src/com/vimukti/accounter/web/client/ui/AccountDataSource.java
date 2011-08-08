package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAccount;

public class AccountDataSource extends BaseDataSource {

	public AccountDataSource(
			AccounterAsyncCallback<ArrayList<ClientAccount>> callback) {
		Accounter.getCompany().getAccounts();
	}
}
