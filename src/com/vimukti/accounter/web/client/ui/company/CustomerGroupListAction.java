package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;
import com.vimukti.accounter.web.client.ui.customers.CustomerGroupListDialog;

public class CustomerGroupListAction extends Action {

	public CustomerGroupListAction() {
		super();
	}

	@Override
	public void run() {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				CustomerGroupListDialog dialog = new CustomerGroupListDialog(
						messages.manageCustomerGroup(
								Global.get().Customer()), messages
								.toAddPayeeGroup(Global.get().Customer()));
				dialog.show();
				
			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
//		AccounterAsync.createAsync(new CreateViewAsyncCallback() {
//
//			@Override
//			public void onCreated() {
//				
//
//			}
//
//		});
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

	@Override
	public String getText() {
		return messages.payeeGroupList(Global.get().Customer());
	}
}
