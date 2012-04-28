package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.reports.PayHeadSummary;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class PayHeadSummaryReportAction extends Action<PayHeadSummary> {

	public PayHeadSummaryReportAction() {
		super();
		this.catagory = messages.report();
	}

	@Override
	public String getText() {
		return messages.payHeadSummaryReport();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final PayHeadSummary data, final boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				PayHeadSummaryReport payHeadSummaryReport = new PayHeadSummaryReport();
				MainFinanceWindow.getViewManager().showView(
						payHeadSummaryReport, data, isDependent,
						PayHeadSummaryReportAction.this);
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
		return HistoryTokens.PAY_HEAD_SUMMMARY_REPORT;
	}

	@Override
	public String getHelpToken() {
		return "payHeadSummaryReport";
	}

}
