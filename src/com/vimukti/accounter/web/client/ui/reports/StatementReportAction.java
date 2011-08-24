package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class StatementReportAction extends Action {

	protected StatementReport report;

	public StatementReportAction(String text) {
		super(text);
		this.catagory = Accounter.constants().report();

	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {
				report = new StatementReport();
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, StatementReportAction.this);

			}

		});

	}

	// @Override
	// public ParentCanvas getView() {
	// return this.report;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reports();

	}

	// @Override
	// public String getImageUrl() {
	// return "/images/reports.png";
	// }

	@Override
	public String getHistoryToken() {
		return "customerStatement";
	}

	@Override
	public String getHelpToken() {
		return "statement-report";
	}

}
