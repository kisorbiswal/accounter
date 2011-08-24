package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

/**
 * 
 * @author Uday Kumar
 * 
 */
@SuppressWarnings("unchecked")
public class ExpenseClaimsAction extends Action {

	ExpenseClaims view;
	int selectedTab;

	public ExpenseClaimsAction(String text, int selectedTab) {
		super(text);
		this.selectedTab = selectedTab;
		catagory = Accounter.getCompany().isUKAccounting() ? Accounter
				.constants().supplier() : Global.get().vendor();
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
	public void run() {
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

	@Override
	public String getHelpToken() {
		return "expense-claims";
	}
}
