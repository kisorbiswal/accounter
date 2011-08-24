package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;

/**
 * 
 * @author Raj Vimal
 */

public class RecordExpensesAction extends Action {

	public RecordExpensesAction(String text) {
		super(text);
	}

	// @Override
	// public ParentCanvas getView() {
	//
	// return null;
	// }

	@Override
	public void run() {
		new SelectExpenseType().show();

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().recordExpenses();
	}

	// @Override
	// public String getImageUrl() {
	//
	// return "/images/record_expenses.png";
	// }

	@Override
	public String getHistoryToken() {

		return "recordExpenses";
	}

	@Override
	public String getHelpToken() {
		return "add-expense";
	}

}
