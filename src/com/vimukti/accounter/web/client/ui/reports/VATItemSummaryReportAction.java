package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class VATItemSummaryReportAction extends Action {
	public VATItemSummaryReportAction(String text) {
		super(text);
		this.catagory = Accounter.constants().report();
	}

	@Override
	public ImageResource getBigImage() {

		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reports();
	}

	// @Override
	// public ParentCanvas getView() {
	//
	// return null;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				VATItemSummaryReport report = new VATItemSummaryReport();
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, VATItemSummaryReportAction.this);

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Report..", t);
			}
		});

	}

	// @Override
	// public String getImageUrl() {
	// return "/images/reports.png";
	// }

	@Override
	public String getHistoryToken() {

		return "vatItemSummary";
	}

	@Override
	public String getHelpToken() {
		return "vat-item-summary";
	}

}
