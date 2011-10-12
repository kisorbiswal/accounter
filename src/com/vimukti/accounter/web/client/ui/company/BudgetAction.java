package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.BudgetListView;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author Amrit Mishra
 * 
 */

public class BudgetAction extends Action {

	protected BudgetListView view;

	public BudgetAction(String text) {
		super(text);
		this.catagory = Accounter.constants().budget();
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = new BudgetListView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, BudgetAction.this);
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
		return Accounter.getFinanceMenuImages().customers();
	}

	@Override
	public String getHistoryToken() {
		return "Budget";
	}

	@Override
	public String getHelpToken() {
		return "Budget";
	}

}
