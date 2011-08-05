package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.customers.CustomerGroupListDialog;

public class CustomerGroupListAction extends Action {

	public CustomerGroupListAction(String text) {
		super(text);
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed To Load Customer Groups", t);

			}

			public void onCreated() {
				try {

					CustomerGroupListDialog dialog = new CustomerGroupListDialog(
							Accounter.constants().manageCustomerGroup(),
							Accounter.constants().toAddCustomerGroup());
					dialog.show();

				} catch (Throwable e) {
					onCreateFailed(e);

				}

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
}
