package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class CustomerPaymentsAction extends Action {

	public CustomerPaymentsAction(String text) {
		super(text);
		this.catagory = Global.get().Customer();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				NewCustomerPaymentView view = NewCustomerPaymentView
						.getInstance();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, CustomerPaymentsAction.this);

			}

		});
	}

	// @Override
	// public ParentCanvas getView() {
	// // NOTHING TO DO
	// return null;
	// }

	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newVendorPayment();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/vendor_payments.png";
	// }

	@Override
	public String getHistoryToken() {
		return "customerPrepayment";
	}

	@Override
	public String getHelpToken() {
		return "customer-prepayments";
	}
}
