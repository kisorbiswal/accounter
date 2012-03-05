package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class JobProfitabilityDetailReportAction extends Action {

	protected JobProfitabilityDetailReport report;

	public JobProfitabilityDetailReportAction() {
		super();
		this.catagory = messages.report();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {
				report = new JobProfitabilityDetailReport();
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, JobProfitabilityDetailReportAction.this);

			}

			public void onCreateFailed(Throwable t) {
				System.err.println("Failed to Load Report.." + t);
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
		return "jobProfitabilityDetail";
	}

	@Override
	public String getHelpToken() {
		return "Job-Profitability-Detail";
	}

	@Override
	public String getText() {
		return "Job Profitability Detail";
	}

}
