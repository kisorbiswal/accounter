package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * 
 * @author Raj Vimal
 */

public class RecordExpensesAction extends Action {

	public RecordExpensesAction(String text) {
		super(text);
	}

	public RecordExpensesAction(String text, String iconString) {
		super(text, iconString);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		new SelectExpenseType().show();

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return FinanceApplication.getFinanceMenuImages().recordExpenses();
	}
	
	@Override
	public String getImageUrl() {
		// TODO Auto-generated method stub
		return "/images/record_expenses.png";
	}

}
