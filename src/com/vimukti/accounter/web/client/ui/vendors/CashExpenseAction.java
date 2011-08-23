package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class CashExpenseAction extends Action {

	private CashExpenseView view;

	public CashExpenseAction(String text) {
		super(text);
		this.catagory = Global.get().Vendor();
	}

	public CashExpenseAction(String text, ClientVendor vendor,
			AccounterAsyncCallback<Object> callback) {
		super(text);
		this.catagory = Global.get().Vendor();
	}

	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				view = new CashExpenseView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, CashExpenseAction.this);
				// UIUtils.setCanvas(view, getViewConfiguration());

			}

		});

	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newCashPurchage();
	}

	// @Override
	// public ParentCanvas getView() {
	//
	// return this.view;
	// }

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/new_cash_purchase.png";
	// }

	@Override
	public String getHistoryToken() {
		return "cashExpense";
	}
}
