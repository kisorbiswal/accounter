package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.reports.ReconciliationDiscrepancy;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class ReconciliationDiscrepancyReportAction extends
		Action<ReconciliationDiscrepancy> {


	public ReconciliationDiscrepancyReportAction() {
		super();
		this.catagory = messages.report();
	}

	@Override
	public String getText() {
		return messages.reconcilationDiscrepancyReport();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final ReconciliationDiscrepancy data,
			final boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				ReconciliationDiscrepancyReport report = new ReconciliationDiscrepancyReport();
				MainFinanceWindow.getViewManager()
						.showView(report, data, isDependent,
								ReconciliationDiscrepancyReportAction.this);
			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
//		AccounterAsync.createAsync(new CreateViewAsyncCallback() {
//
//			@Override
//			public void onCreated() {
//				
//			}
//		});
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
		return HistoryTokens.RECONCILIATION_DISCREPANCY;
	}

	@Override
	public String getHelpToken() {
		return "reconciliation-discrepancy";
	}

}
