package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class BudgetvsActualsAction extends Action {

	protected BudgetVsActualsReport report;

	public BudgetvsActualsAction() {
		super();
		this.catagory = messages.report();
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				report = new BudgetVsActualsReport();
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, BudgetvsActualsAction.this);

			}

		});

	}

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

	@Override
	public String getHistoryToken() {
		return "budgetVSactuals";
	}

	@Override
	public String getHelpToken() {
		return "budget-report";
	}

	@Override
	public String getText() {
		return messages.budgetvsActuals();
	}

}
