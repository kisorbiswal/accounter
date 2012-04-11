package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class TaxItemDetailReportAction extends Action {

	private boolean isFromReports;

	public TaxItemDetailReportAction() {
		super();
		this.catagory = messages.report();
	}

	@Override
	public void run() {

		runAsync(data, id, isDependent);
	}

	public void runAsync(final Object data, final long id,
			final Boolean dependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				TaxItemDetailReportView report = new TaxItemDetailReportView();
				report.setTaxReturnId(id);
				report.setIsFromReports(isFromReports);
				MainFinanceWindow.getViewManager().showView(report, data,
						dependent, TaxItemDetailReportAction.this);
			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Report...", t);
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
		return "TaxItemDetail";
	}

	@Override
	public String getHelpToken() {
		return "Tax Item Detail";
	}

	@Override
	public String getText() {
		return messages.taxItemDetailReport();
	}

	public void setFromReports(boolean fromReports) {
		this.isFromReports = fromReports;
	}

}
