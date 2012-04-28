package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.reports.PaySheet;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class PaySheetReportAction extends Action<PaySheet> {

	protected PaySheetReport report;

	public PaySheetReportAction() {
		super();
		this.catagory = messages.report();
	}

	@Override
	public String getText() {
		return messages.paySheet();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				report = new PaySheetReport();
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, PaySheetReportAction.this);

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
		return Accounter.getFinanceMenuImages().reports();
	}

	@Override
	public String getHistoryToken() {
		return HistoryTokens.PAYSHEET_REPORT;
	}

	@Override
	public String getHelpToken() {
		return "paysheet_report";
	}

}
