package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientVendorCreditMemo;
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

public class NewCreditMemoAction extends Action {

	protected VendorCreditMemoView view;

	public NewCreditMemoAction(String text) {
		super(text);
		this.catagory = Global.get().Vendor();
	}

	public NewCreditMemoAction(String newCreditMemo,
			ClientVendorCreditMemo vendorCreditMemo,
			AccounterAsyncCallback<Object> callBack) {
		super(newCreditMemo);
		this.catagory = Global.get().Vendor();
	}

	@Override
	public void run() {

		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				view = VendorCreditMemoView.getInstance();

				// UIUtils.setCanvas(view, getViewConfiguration());
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewCreditMemoAction.this);

			}

		});

	}

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newVendorCreditMemo();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/new_credit_memo.png";
	// }

	@Override
	public String getHistoryToken() {
		return "vendorCredit";
	}
}
