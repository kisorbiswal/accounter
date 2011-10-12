package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.reports.DepositDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.reports.AbstractReportView;
import com.vimukti.accounter.web.client.ui.reports.DepositDetailReport;

public class DepositDetailAction extends Action {

	public DepositDetailAction(String text) {
		super(text);
		this.catagory = Accounter.constants().report();
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				AbstractReportView<DepositDetail> report = new DepositDetailReport();
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, DepositDetailAction.this);

			}
		});
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reports();

	}

	@Override
	public String getHistoryToken() {
		return "depositDetails";
	}

	@Override
	public String getHelpToken() {
		return "deposit-detail";
	}

}
