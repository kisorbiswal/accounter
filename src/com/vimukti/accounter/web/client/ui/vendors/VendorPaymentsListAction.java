package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * 
 * @author Raj Vimal
 * @modified by Ravi kiran.G
 */

public class VendorPaymentsListAction extends Action {

	public VendorPaymentsListAction(String text) {
		super(text);
		this.catagory =  UIUtils.getVendorString("Supplier", "Vendor");
	}

	public VendorPaymentsListAction(String text, String iconString) {
		super(text, iconString);
		this.catagory =  UIUtils.getVendorString("Supplier", "Vendor");
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {

				// //UIUtils.logError("Failed to load VendorPaymentsList...",
				// t);
			}

			public void onCreated() {

				VendorPaymentsListView view = VendorPaymentsListView
						.getInstance();

				try {

					// UIUtils.setCanvas(view, getViewConfiguration());
					MainFinanceWindow.getViewManager().showView(view, null,
							false, VendorPaymentsListAction.this);

				} catch (Throwable t) {

					onCreateFailed(t);

				}

			}

		});

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return FinanceApplication.getFinanceMenuImages().vendorpayments();
	}

	@Override
	public String getImageUrl() {
		// TODO Auto-generated method stub
		return "/images/vendor_payments.png";
	}
}
