package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * @modified by Ravi kiran.G
 * 
 */
public class VendorPaymentsAction extends Action {

	public VendorPaymentsAction(String text) {
		super(text);
		this.catagory = Global.get().Vendor();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreateFailed(Throwable t) {

				// //UIUtils.logError("Failed to load the VendorPayment..", t);

			}

			public void onCreated() {

				NewVendorPaymentView view = NewVendorPaymentView.getInstance();

				// UIUtils.setCanvas(view, getViewConfiguration());
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, VendorPaymentsAction.this);

			}

		});

	}

	// @Override
	// public ParentCanvas getView() {
	// // currently not using
	// return null;
	// }

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newVendorPayment();
	}

	// @Override
	// public String getImageUrl() {
	//
	// return "/images/vendor_payments.png";
	// }

	@Override
	public String getHistoryToken() {

		return "vendorPrePayment";
	}

	@Override
	public String getHelpToken() {
		return "issue-payment";
	}

}
