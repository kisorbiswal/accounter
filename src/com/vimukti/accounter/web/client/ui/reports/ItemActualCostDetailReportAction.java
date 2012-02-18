package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.reports.ItemActualCostDetail;
import com.vimukti.accounter.web.client.core.reports.JobProfitabilityDetailByJob;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class ItemActualCostDetailReportAction extends Action{

	protected ItemActualCostDetailReport report;

	public ItemActualCostDetailReportAction() {
		super();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {
				JobProfitabilityDetailByJob obj = (JobProfitabilityDetailByJob)data;
				
				report = new ItemActualCostDetailReport(obj.isCost(), obj.getItemId());
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, ItemActualCostDetailReportAction.this);

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
		return "ItemActualCostDetailReport";
	}

	@Override
	public String getHelpToken() {
		return "ItemActualCostDetailReport";
	}

	@Override
	public String getText() {
		return "Item Actual Cost Detail Report";
	}

}
