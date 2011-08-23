package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class ExpensesAction extends Action {

	protected ExpensesListView view;
	public String viewType;

	public ExpensesAction(String text) {
		super(text);
		this.catagory = Global.get().Vendor();
	}

	public ExpensesAction(String text, String viewType) {
		super(text);
		this.catagory = Global.get().Vendor();
		this.viewType = viewType;
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public ImageResource getBigImage() {
		return null;
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	@Override
	public void run() {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {
				if (viewType == null || viewType.equals(""))
					view = ExpensesListView.getInstance();
				else
					view = new ExpensesListView(viewType);

				// UIUtils.setCanvas(view, getViewConfiguration());
				MainFinanceWindow.getViewManager().showView(view, null, false,
						ExpensesAction.this);

			}

		});
	}

	// public void run(Object data, Boolean isDependent, String viewType) {
	// this.viewType = viewType;
	// run(data, isDependent);
	// }

	@Override
	public String getHistoryToken() {
		if (viewType == null) {
			return "expenses";
		} else if (viewType.equals(Accounter.constants().cash())) {
			return "cashExpenses";
		} else if (viewType.equals(Accounter.constants().creditCard())) {
			return "creditCardExpenses";
		} else if (viewType.equals(Accounter.constants().employee())) {
			return "employeeExpenses";
		}
		return "";
	}

}
