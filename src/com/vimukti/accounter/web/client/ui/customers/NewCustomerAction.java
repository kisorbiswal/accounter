package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class NewCustomerAction extends Action {

	private CustomerView view;

	public NewCustomerAction(String text, String icon) {
		super(text, icon);
		this.catagory = Accounter.getCustomersMessages().customer();
		super.setToolTip(Accounter.getCustomersMessages().customer());
	}

	public NewCustomerAction(String text, String icon, ClientCustomer customer,
			AsyncCallback<Object> callback) {
		super(text, icon, customer, callback);
		this.catagory = Accounter.getCustomersMessages().customer();
	}

	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {

					view = new CustomerView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, NewCustomerAction.this);
					// UIUtils.setCanvas(view, getViewConfiguration());

				} catch (Throwable t) {
					onCreateFailed(t);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Customer View..", t);
			}
		});

	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		return this.view;
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newCustomer();
	}

	@Override
	public String getImageUrl() {
		return "/images/new_customer.png";
	}

	@Override
	public String getHistoryToken() {
		return "newCustomer";
	}
}
