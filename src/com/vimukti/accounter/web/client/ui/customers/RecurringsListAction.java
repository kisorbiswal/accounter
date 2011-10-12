package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.RecurringTransactionsListView;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * Not completed.
 * 
 * @author vimukti3
 * 
 */
public class RecurringsListAction extends Action {

	public RecurringsListAction(String text) {
		super(text);

	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				RecurringTransactionsListView view = new RecurringTransactionsListView();

				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, RecurringsListAction.this);

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "RecurringTransactions";
	}

	@Override
	public String getHelpToken() {
		return "recurring-list";
	}
}
