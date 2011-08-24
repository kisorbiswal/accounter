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

public class CreditCardExpenseAction extends Action {

	CreditCardExpenseView view;

	public CreditCardExpenseAction(String text) {
		super(text);
		this.catagory = Global.get().Vendor();
	}

	public CreditCardExpenseAction(String text, ClientVendor vendor,
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

				view = new CreditCardExpenseView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, CreditCardExpenseAction.this);
				// UIUtils.setCanvas(view, getViewConfiguration());

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Customer View..", t);
			}
		});

	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newCreditCardCharge();
	}

	// @Override
	// public ParentCanvas getView() {
	//
	// return this.view;
	// }

	@Override
	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/credit_card_charge.png";
	// }

	@Override
	public String getHistoryToken() {
		return "creditCardExpense";
	}

	@Override
	public String getHelpToken() {
		return "creditcard-expense";
	}
}
