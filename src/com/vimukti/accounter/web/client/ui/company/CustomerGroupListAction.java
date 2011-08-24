package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.customers.CustomerGroupListDialog;

public class CustomerGroupListAction extends Action {

	public CustomerGroupListAction(String text) {
		super(text);
	}

	@Override
	public void run() {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				CustomerGroupListDialog dialog = new CustomerGroupListDialog(
						Accounter.messages().manageCustomerGroup(
								Global.get().customer()), Accounter.messages()
								.toAddCustomerGroup(Global.get().customer()));
				dialog.show();

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

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
