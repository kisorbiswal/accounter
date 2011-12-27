package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class ReconcilationsAction extends Action {
	private ReconcilationsReport reconcilationReport;

	public ReconcilationsAction() {
		super();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				reconcilationReport = new ReconcilationsReport();
				MainFinanceWindow.getViewManager().showView(
						reconcilationReport, data, isDependent,
						ReconcilationsAction.this);
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
		return "ReconciliationReports";
	}

	@Override
	public String getHelpToken() {
		return "Reconciliation-Reports";
	}

	@Override
	public String getText() {
		return messages.reconciliationsReport();
	}

}
