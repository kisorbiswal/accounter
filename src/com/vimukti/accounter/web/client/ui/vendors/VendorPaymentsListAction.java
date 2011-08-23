package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author Raj Vimal
 * @modified by Ravi kiran.G
 */

public class VendorPaymentsListAction extends Action {

	public VendorPaymentsListAction(String text) {
		super(text);
		this.catagory = Global.get().Vendor();
	}

	/**
	 * THIS METHOD NOT USING ANY WHERE IN THE PROJECT.
	 */

	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	@Override
	public void run() {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreateFailed(Throwable t) {

				// //UIUtils.logError("Failed to load VendorPaymentsList...",
				// t);
			}

			public void onCreated() {

				VendorPaymentsListView view = VendorPaymentsListView
						.getInstance();

				// UIUtils.setCanvas(view, getViewConfiguration());
				MainFinanceWindow.getViewManager().showView(view, null, false,
						VendorPaymentsListAction.this);

			}

		});

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().vendorpayments();
	}

	// @Override
	// public String getImageUrl() {
	//
	// return "/images/vendor_payments.png";
	// }

	@Override
	public String getHistoryToken() {

		return "vendorPayments";
	}
}
