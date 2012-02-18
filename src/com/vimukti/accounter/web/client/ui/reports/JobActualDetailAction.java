package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.reports.JobActualCostDetail;
import com.vimukti.accounter.web.client.core.reports.JobProfitability;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class JobActualDetailAction extends Action {

	
	protected JobActualCostDetailReport report;

	public JobActualDetailAction() {
		super();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {
				if(data instanceof JobProfitability){
					JobProfitability obj=(JobProfitability)data;
				report = new JobActualCostDetailReport(obj.isCost(),obj.getCustomerId(), obj.getJobId());
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, JobActualDetailAction.this);
				}

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
		return "jobActualDetail";
	}

	@Override
	public String getHelpToken() {
		return "job-Actual-Detail";
	}

	@Override
	public String getText() {
		return "job Actual Detail";
	}

}
