package com.vimukti.accounter.web.client.ui.customers;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.ui.BaseDataSource;

public class CustomerDataSource extends BaseDataSource {
	
	AsyncCallback<List<ClientCustomer>> callback;

	public CustomerDataSource(AsyncCallback<List<ClientCustomer>> callback) {
		this.callback = callback;
		//FinanceApplication.createGETService().getCustomers( callback);	
//		FinanceApplication.getCompany().getCustomers();
		
		
	}
}
