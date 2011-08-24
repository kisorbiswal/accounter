package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author Mandeep Singh
 */

public class ProfitAndLossAction extends Action {

	protected ProfitAndLossReport report;

	public ProfitAndLossAction(String text) {
		super(text);
		this.catagory = Accounter.constants().report();
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {
				if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
					report = new ProfitAndLossUKReport();
				else
					report = new ProfitAndLossReport();
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, ProfitAndLossAction.this);

			}

			public void onCreateFailed(Throwable t) {
				/* UIUtils.logError */System.err
						.println("Failed to Load Report.." + t);
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
		// return FinanceApplication.getFinanceMenuImages().profitAndLose();
		return Accounter.getFinanceMenuImages().reports();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/reports.png";
	// }

	@Override
	public String getHistoryToken() {

		return "profitAndLoss";
	}

	@Override
	public String getHelpToken() {
		return "profit-loss";
	}

}
