package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class ProfitAndLossByLocationAction extends Action {
	private static final int CLASS = 1;
	private static final int LOCATION = 2;
	private static final int JOB = 3;
	protected ProfitAndLossByLocationReport locationReport;
	private int category_type = 0;

	public ProfitAndLossByLocationAction(int category) {
		super();
		this.catagory = messages.report();
		this.category_type = category;
	}

	@Override
	public void run() {
		runAsync(data, isDependent, category_type);
	}

	public void runAsync(final Object data, final Boolean isDependent,
			final int category) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				locationReport = new ProfitAndLossByLocationReport(
						category_type);
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
		if (category_type == LOCATION) {
			return "profitAndLossByLocation";
		} else if (category_type == JOB) {
			return "Profit&LossbyJob";
		} else {
			return "profitAndLossByClass";
		}

	}

	@Override
	public String getHelpToken() {

		if (category_type == LOCATION) {
			return "profit-loss-by-location";
		} else if (category_type == JOB) {
			return "Profit&LossbyJob";
		} else {
			return "profit-loss-by-class";
		}

	}

	@Override
	public String getText() {
		String actionstring = null;
		if (category_type == LOCATION) {
			actionstring = messages.profitAndLossByLocation(Global.get()
					.Location());
		} else if (category_type == JOB) {
			return "Profit & Loss by Job";
		} else {
			actionstring = messages.profitAndLossbyClass();
		}

		return actionstring;
	}
}
