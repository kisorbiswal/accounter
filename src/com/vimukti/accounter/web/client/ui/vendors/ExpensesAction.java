package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class ExpensesAction extends Action {

	protected ExpensesListView view;
	public String viewType;

	public ExpensesAction(String text) {
		super(text);
		this.catagory = UIUtils.getVendorString(Accounter
				.getVendorsMessages().supplier(), Accounter
				.getVendorsMessages().vendor());
	}

	public ExpensesAction(String text, String iconString, String viewType) {
		super(text, iconString);
		this.catagory = UIUtils.getVendorString(Accounter
				.getVendorsMessages().supplier(), Accounter
				.getVendorsMessages().vendor());
		this.viewType = viewType;
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
	public ParentCanvas getView() {
		// TODO Auto-generated method stub
		return this.view;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {

				// //UIUtils.logError("Failed to load EnterBillsList", t);

			}

			public void onCreated() {
				if (viewType == null || viewType.equals(""))
					view = ExpensesListView.getInstance();
				else
					view = new ExpensesListView(viewType);

				try {

					// UIUtils.setCanvas(view, getViewConfiguration());
					MainFinanceWindow.getViewManager().showView(view, null,
							false, ExpensesAction.this);

				} catch (Throwable t) {

					onCreateFailed(t);
				}

			}

		});
	}

	// public void run(Object data, Boolean isDependent, String viewType) {
	// this.viewType = viewType;
	// run(data, isDependent);
	// }

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		if (viewType == null) {
			return "expenses";
		} else if (viewType.equals(Accounter.getVendorsMessages()
				.cash())) {
			return "cashExpenses";
		} else if (viewType.equals(Accounter.getVendorsMessages()
				.creditCard())) {
			return "creditCardExpenses";
		} else if (viewType.equals(Accounter.getVendorsMessages()
				.employee())) {
			return "employeeExpenses";
		}
		return "";
	}

}
