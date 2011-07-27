package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class CustomerPaymentsAction extends Action {
	public CustomerPaymentsAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.getCustomersMessages().customer();
	}

	public CustomerPaymentsAction(String text) {
		super(text);
		this.catagory = Accounter.getCustomersMessages().customer();
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {

				// //UIUtils.logError("Failed to load the VendorPayment..", t);

			}

			public void onCreated() {

				NewCustomerPaymentView view = NewCustomerPaymentView
						.getInstance();

				try {

					// UIUtils.setCanvas(view, getViewConfiguration());
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, CustomerPaymentsAction.this);

				} catch (Throwable t) {

					onCreateFailed(t);

				}
			}

		});

	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		// NOTHING TO DO
		return null;
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newVendorPayment();
	}

	@Override
	public String getImageUrl() {
		return "/images/vendor_payments.png";
	}

	@Override
	public String getHistoryToken() {
		return "customerPrepayment";
	}
}
