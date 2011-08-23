package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author Raj Vimal
 * @modified by Ravi kiran.G
 */

public class NewCashPurchaseAction extends Action {

	protected CashPurchaseView view;

	public NewCashPurchaseAction(String text) {
		super(text);
		this.catagory = Global.get().Vendor();
	}

	public NewCashPurchaseAction(String newCashPurchase,
			ClientCashPurchase cashPurchase,
			AccounterAsyncCallback<Object> callback) {
		super(newCashPurchase);
		this.catagory = Global.get().Vendor();
	}

	@Override
	public void run() {

		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				view = CashPurchaseView.getInstance();

				// UIUtils.setCanvas(view, getViewConfiguration());
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewCashPurchaseAction.this);

			}

		});

	}

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newCashPurchage();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/new_cash_purchase.png";
	// }

	@Override
	public String getHistoryToken() {
		return "newCashPurchase";
	}

}
