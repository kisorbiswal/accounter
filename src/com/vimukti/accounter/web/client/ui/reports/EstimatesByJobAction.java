package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class EstimatesByJobAction extends Action {
	EstimatesByJobReport estimatesByJobReport;

	@Override
	public String getText() {
		return "Estimates by Job";
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				estimatesByJobReport = new EstimatesByJobReport();
				MainFinanceWindow.getViewManager().showView(
						estimatesByJobReport, data, isDependent,
						EstimatesByJobAction.this);
			}
		});
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "Estimatebyjob";
	}

	@Override
	public String getHelpToken() {
		return "Estimate-by-job";
	}

}
