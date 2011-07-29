package com.vimukti.accounter.web.client.ui.customers;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.company.NewItemAction;

public class NewCustomerItemAction extends NewItemAction {

	public NewCustomerItemAction(String text) {
		super(text, true);
		this.catagory = Accounter.constants().customer();
	}

	@Override
	public String getHistoryToken() {
		return "newCustomerItem";
	}

}
