package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCustomerCreditMemo;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author kumar kasimala
 */

public class NewCreditsAndRefundsAction extends Action {

	protected CustomerCreditMemoView view;

	public NewCreditsAndRefundsAction(String text) {
		super(text);
		this.catagory = Global.get().Customer();
	}

	public NewCreditsAndRefundsAction(String text,
			ClientCustomerCreditMemo creditMemo,
			AccounterAsyncCallback<Object> callback) {
		super(text);
		this.catagory = Global.get().Customer();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = new CustomerCreditMemoView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewCreditsAndRefundsAction.this);

			}

		});
	}

	// @Override
	// public ParentCanvas getView() {
	// // NOTHING TO DO.
	// return null;
	// }

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newCreditsAndRefunds();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/new_credit_and_refunds.png";
	// }

	@Override
	public String getHistoryToken() {
		return "newCredit";
	}

	@Override
	public String getHelpToken() {
		return "new-credit_refund";
	}
}
