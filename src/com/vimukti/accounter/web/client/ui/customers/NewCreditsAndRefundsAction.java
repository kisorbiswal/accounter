package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientCustomerCreditMemo;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;

/**
 * 
 * @author kumar kasimala
 */

public class NewCreditsAndRefundsAction extends Action {

	protected CustomerCreditMemoView view;

	public NewCreditsAndRefundsAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.constants().customer();
	}

	public NewCreditsAndRefundsAction(String text, String iconString,
			ClientCustomerCreditMemo creditMemo, AsyncCallback<Object> callback) {
		super(text, iconString);
		this.catagory = Accounter.constants().customer();
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {
				try {

					view = new CustomerCreditMemoView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, NewCreditsAndRefundsAction.this);
					// UIUtils.setCanvas(view, getViewConfiguration());

				} catch (Throwable t) {
					onCreateFailed(t);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to load Customer Credit Memo..",
				// t);

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
}
