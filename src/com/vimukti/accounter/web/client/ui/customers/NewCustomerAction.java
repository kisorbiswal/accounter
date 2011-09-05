package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class NewCustomerAction extends Action<ClientCustomer> {

	private CustomerView view;
	private String quickAddText;

	public NewCustomerAction(String text) {
		super(text);
		this.catagory = Global.get().customer();
		super.setToolTip(Global.get().customer());
	}

	public NewCustomerAction(String text, String quickAddText) {
		super(text);
		this.catagory = Global.get().customer();
		super.setToolTip(Global.get().customer());
		this.quickAddText = quickAddText;

	}

	public NewCustomerAction(String text, ClientCustomer customer,
			AccounterAsyncCallback<Object> callback) {
		super(text);
		this.catagory = Global.get().customer();
	}

	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				view = new CustomerView();
				if (quickAddText != null) {
					view.prepareForQuickAdd(quickAddText);
				}

				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewCustomerAction.this);

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

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

	@Override
	public String getHelpToken() {
		return "add-customer";
	}
}
