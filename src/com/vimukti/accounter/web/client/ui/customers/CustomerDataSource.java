package com.vimukti.accounter.web.client.ui.customers;

import java.util.List;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.ui.BaseDataSource;

public class CustomerDataSource extends BaseDataSource {
	
	AccounterAsyncCallback<List<ClientCustomer>> callback;

	public CustomerDataSource(AccounterAsyncCallback<List<ClientCustomer>> callback) {
		this.callback = callback;
		//FinanceApplication.createGETService().getCustomers( callback);	
//		FinanceApplication.getCompany().getCustomers();
		
		
	}
}
