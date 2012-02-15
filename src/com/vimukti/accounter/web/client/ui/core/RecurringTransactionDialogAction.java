package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientRecurringTransaction;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.customers.RecurringTransactionDialog;

public class RecurringTransactionDialogAction extends Action {

	public RecurringTransactionDialogAction() {
		super();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	public void run() {

		runAsync(data, isDependent);

	}

	private void runAsync(Object data, Boolean isDependent) {

		AbstractView<?> existingView = MainFinanceWindow.getViewManager().existingView;
		RecurringTransactionDialog comboDialog = new RecurringTransactionDialog(
				null, (ClientRecurringTransaction) data);

		comboDialog.show();
		comboDialog.center();
	}

	@Override
	public String getHistoryToken() {
		return "recurringTransaction";
	}

	@Override
	public String getHelpToken() {
		return "recurring-Transaction";
	}

	@Override
	public String getText() {
		return messages.recurring();
	}

}
