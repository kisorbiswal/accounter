package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.Lists.DummyDebitor;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class APAgingSummaryReportAction extends Action<DummyDebitor> {

	protected APAgingSummaryReport report;

	public APAgingSummaryReportAction() {
		super();
		this.catagory = messages.report();

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
	// return this.report;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				report = new APAgingSummaryReport();
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, APAgingSummaryReportAction.this);

			}

		});

	}

	// @Override
	// public String getImageUrl() {
	// return "/images/reports.png";
	// }

	@Override
	public String getHistoryToken() {
		return "apAgingSummary";
	}

	@Override
	public String getHelpToken() {
		return "ap-aging-summary";
	}

	@Override
	public String getText() {
		return messages.apAgeingSummary();
	}

}
