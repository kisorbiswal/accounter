package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class PurchaseByVendorSummaryAction extends Action {

	protected PurchaseByVendorSummaryReport report;

	public PurchaseByVendorSummaryAction() {
		super();
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				report = new PurchaseByVendorSummaryReport();
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, PurchaseByVendorSummaryAction.this);

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Report..", t);
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
		return null;
	}

	@Override
	public String getCatagory() {
		return Global.get().Vendor();
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

		return "purchaseByVendorSummary";
	}

	@Override
	public String getHelpToken() {
		return "purchase-vendor-summary";
	}

	@Override
	public String getText() {
		return Global.get().messages()
				.purchaseByVendorSummary(Global.get().Vendor());
	}

}
