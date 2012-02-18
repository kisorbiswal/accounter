package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByAccount;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class MissingChecksReportAction extends
		Action<TransactionDetailByAccount> {

	private MissingChecksReport report;

	public MissingChecksReportAction() {
		super();
		this.catagory = messages.report();
	}

	@Override
	public String getText() {
		return "Missing Checks";
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final TransactionDetailByAccount data, final boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				report = new MissingChecksReport();
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, MissingChecksReportAction.this);
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
		return HistoryTokens.MISSION_CHECKS;
	}

	@Override
	public String getHelpToken() {
		return "missing-checks";
	}

}
