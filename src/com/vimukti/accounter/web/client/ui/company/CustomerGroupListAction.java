package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;
import com.vimukti.accounter.web.client.ui.customers.CustomerGroupListDialog;

public class CustomerGroupListAction extends Action {

	public CustomerGroupListAction(String text) {
		super(text);
	}

	@Override
	public void run() {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				CustomerGroupListDialog dialog = new CustomerGroupListDialog(
						Accounter.messages().manageCustomerGroup(
								Global.get().Customer()), Accounter.messages()
								.toAddCustomerGroup(Global.get().Customer()));
				dialog.show();

			}

		});
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().customers();
	}

	@Override
	public String getHistoryToken() {
		return "customerGroupList";
	}

	@Override
	public String getHelpToken() {
		return "customer-group";
	}
}
