package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.customers.CustomerGroupListDialog;

public class CustomerGroupListAction extends Action {

	public CustomerGroupListAction(String text) {
		super(text);
	}

	public CustomerGroupListAction(String text, String iconString) {
		super(text, iconString);
	}

	@Override
	public ParentCanvas<?> getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed To Load Customer Groups", t);

			}

			public void onCreated() {
				try {

					CustomerGroupListDialog dialog = new CustomerGroupListDialog(
							FinanceApplication.getCompanyMessages()
									.manageCustomerGroup(), FinanceApplication
									.getCompanyMessages().toAddCustomerGroup());
					ViewManager viewManager = ViewManager.getInstance();
					viewManager.setCurrentDialog(dialog);
					// dialog.addCallBack(getViewConfiguration().getCallback());
					dialog.show();

				} catch (Throwable e) {
					onCreateFailed(e);

				}

			}
		});
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return FinanceApplication.getFinanceMenuImages().customers();
	}
@Override
public String getImageUrl() {
	return "/images/customers.png";
}

@Override
public String getHistoryToken() {
	// TODO Auto-generated method stub
	return "customerGroupList";
}
}
