package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class CheckDetailReportAction extends Action {

	private CheckDetailReportView report;

	public CheckDetailReportAction(String text, String string) {
		super(text);
		this.catagory = Accounter.constants().report();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reports();
	}

	// @Override
	// public ParentCanvas getView() {
	// return this.report;
	// }

	@Override
	public void run() {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {

				report = new CheckDetailReportView();
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, CheckDetailReportAction.this);

			}

		});

	}

	// @Override
	// public String getImageUrl() {
	// return "/images/reports.png";
	// }

	@Override
	public String getHistoryToken() {
		return "CheckDetailsReport";
	}

	@Override
	public String getHelpToken() {
		return "check-detail";
	}
}
