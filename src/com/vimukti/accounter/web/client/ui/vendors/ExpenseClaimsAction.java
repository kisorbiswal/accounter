package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

/**
 * 
 * @author Uday Kumar
 * 
 */
public class ExpenseClaimsAction extends Action {

	ExpenseClaims view;
	int selectedTab;

	public ExpenseClaimsAction(String text, int selectedTab) {
		super(text);
		this.selectedTab = selectedTab;
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	//
	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	@Override
	public void run(Object data, Boolean isDependent) {
		view = new ExpenseClaims(selectedTab);
		try {
			MainFinanceWindow.getViewManager().showView(view, data,
					isDependent, this);
		} catch (Exception e) {
		}

	}

	@Override
	public String getHistoryToken() {
		return "expenseClaims";
	}
}
