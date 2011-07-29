package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class NewCustomerAction extends Action {

	private CustomerView view;

	public NewCustomerAction(String text) {
		super(text);
		this.catagory = Accounter.constants().customer();
		super.setToolTip(Accounter.constants().customer());
	}

	public NewCustomerAction(String text, ClientCustomer customer,
			AccounterAsyncCallback<Object> callback) {
		super(text);
		this.catagory = Accounter.constants().customer();
	}

	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

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

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newCustomer();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/new_customer.png";
	// }

	@Override
	public String getHistoryToken() {
		return "newCustomer";
	}
}
