package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class DepreciationSheduleAction extends Action {

	protected DepreciationSheduleReport report;

	public DepreciationSheduleAction(String text) {
		super(text);
		this.catagory = messages.report();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				report = new DepreciationSheduleReport();
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, DepreciationSheduleAction.this);

			}

			public void onCreateFailed(Throwable t) {
				/* UIUtils.logError */System.err
						.println("Failed to Load Report.." + t);
			}
		});

	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reports();
	}

	@Override
	public String getHistoryToken() {
		return "depreciationShedule";
	}

	@Override
	public String getHelpToken() {
		return "depreciation-shedule";
	}

}
