package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAccount;

public class AccountDataSource extends BaseDataSource {
	
	

	public AccountDataSource(AsyncCallback<List<ClientAccount>> callback) {
		FinanceApplication.getCompany().getAccounts();
	}
}
