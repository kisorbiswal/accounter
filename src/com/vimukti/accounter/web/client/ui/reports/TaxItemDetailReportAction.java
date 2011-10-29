package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class TaxItemDetailReportAction extends Action {

	public TaxItemDetailReportAction(String text) {
		super(text);
		this.catagory = Accounter.constants().report();
	}

	@Override
	public void run() {

		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean dependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				TaxItemDetailReportView report = new TaxItemDetailReportView();
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

}
