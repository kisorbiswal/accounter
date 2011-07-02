package com.vimukti.accounter.web.client.ui.customers;

import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.company.NewItemAction;

public class NewCustomerItemAction extends NewItemAction {

	public NewCustomerItemAction(String text, String iconString) {
		super(text, iconString, true);
		this.catagory = FinanceApplication.getCustomersMessages().customer();
	}
	
	@Override
	public String getHistoryToken() {
		return "newCustomerItem";
	}

}
