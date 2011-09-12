package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.reports.SalesByLocationSummary;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class SalesByLocationSummaryAction extends Action {

	private boolean isLocation;

	public SalesByLocationSummaryAction(String text, boolean isLocation) {
		super(text);
		this.isLocation = isLocation;
	}

	@Override
	public void run() {
		final boolean isLocation = this.isLocation;
		runAsync(data, isDependent, isLocation);
	}

	private void runAsync(final Object data, final boolean isDependent,
			final boolean isLocation) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				AbstractReportView<SalesByLocationSummary> report = new SalesByLocationsummaryReport(
						isLocation);
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, SalesByLocationSummaryAction.this);

			}

			public void onCreateFailed(Throwable t) {
				// UIUtils.logError("Failed to Load Report...", t);
			}
		});

	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHistoryToken() {
		if (!isLocation) {
			return "SalesByLocationSummary";
		}
		return Accounter.messages().getSalesByLocationDetails(
				Global.get().Location());
	}

	@Override
	public String getHelpToken() {
		if (!isLocation) {
			return "sales-by-class-summary";
		}
		return "sales-by-location-summary";
	}

}
