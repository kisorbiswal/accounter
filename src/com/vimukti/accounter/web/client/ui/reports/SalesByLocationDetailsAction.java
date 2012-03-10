package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.reports.SalesByLocationDetails;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class SalesByLocationDetailsAction extends Action {

	private boolean isLocation;
	private boolean isCustomer;

	public SalesByLocationDetailsAction(boolean isLocation, boolean isCustomer) {
		super();
		this.catagory = messages.report();
		this.isLocation = isLocation;
		this.isCustomer = isCustomer;
	}

	@Override
	public void run() {
		final boolean isLoction = this.isLocation;
		runAsync(data, isDependent, isLoction);

	}

	private void runAsync(final Object data, final boolean isDependent,
			final boolean isLoction) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				AbstractReportView<SalesByLocationDetails> report = new SalesByLocationDetailsReport(
						isLoction, isCustomer);
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, SalesByLocationDetailsAction.this);

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
		if (isCustomer) {
			if (isLocation) {
				return "salesByLocationDetails";
			} else {
				return "salesByClassDetails";
			}
		} else {
			if (!isLocation) {
				return "PurchasesByLocationDetails";
			} else {
				return "PurchasesbyClassDetail";
			}
		}
	}

	@Override
	public String getHelpToken() {
		if (isCustomer) {
			if (isLocation) {
				return "sales-by-class";
			} else {
				return "sales-by-location";
			}
		} else {
			if (!isLocation) {
				return "Purchases-by-class";
			} else {
				return "Purchases-by-location";
			}
		}
	}

	@Override
	public String getText() {
		String actionsting = null;
		if (isCustomer) {
			if (!isLocation) {
				actionsting = messages.salesByClassDetails();
			} else {
				actionsting = messages.getSalesByLocationDetails(Global.get()
						.Location());
			}
		} else {
			if (!isLocation) {
				actionsting = "Purchases by Location Detail";
			} else {
				actionsting = "Purchases by Class Detail";
			}
		}

		return actionsting;
	}
}
