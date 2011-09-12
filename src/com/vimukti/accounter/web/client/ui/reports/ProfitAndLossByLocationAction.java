package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class ProfitAndLossByLocationAction extends Action {
	protected ProfitAndLossByLocationReport locationReport;
	private boolean isLocation;

	public ProfitAndLossByLocationAction(String text, boolean isLocation) {
		super(text);
		this.isLocation = isLocation;
	}

	@Override
	public void run() {
		runAsync(data, isDependent, isLocation);
	}

	public void runAsync(final Object data, final Boolean isDependent,
			final boolean isLocation) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				locationReport = new ProfitAndLossByLocationReport(isLocation);
				MainFinanceWindow.getViewManager().showView(locationReport,
						data, isDependent, ProfitAndLossByLocationAction.this);
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
		return "ProfitAndLossByLocation";
	}

	@Override
	public String getHelpToken() {
		return " profit-loss By Location";
	}

}
