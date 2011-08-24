package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class PurchaseByVendorDetailsAction extends Action {

	protected PurchaseByVendorDetailReport report;

	public PurchaseByVendorDetailsAction(String text) {
		super(text);
		this.catagory = Accounter.constants().report();
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				report = new PurchaseByVendorDetailReport();
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, PurchaseByVendorDetailsAction.this);

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
	// return this.report;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reports();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/reports.png";
	// }

	@Override
	public String getHistoryToken() {
		return "purchaseByVendorDetail";
	}

	@Override
	public String getHelpToken() {
		return "purchased-vendor";
	}

}
