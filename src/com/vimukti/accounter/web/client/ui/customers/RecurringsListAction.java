package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.RecurringTransactionsListView;
import com.vimukti.accounter.web.client.ui.core.Action;

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
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				RecurringTransactionsListView view = new RecurringTransactionsListView();

				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, RecurringsListAction.this);

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

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
