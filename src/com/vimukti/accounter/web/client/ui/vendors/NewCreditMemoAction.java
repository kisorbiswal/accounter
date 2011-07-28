package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientVendorCreditMemo;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;

/**
 * 
 * @author Raj Vimal
 * @modified by Ravi kiran.G
 */

public class NewCreditMemoAction extends Action {

	protected VendorCreditMemoView view;

	public NewCreditMemoAction(String text) {
		super(text);
		this.catagory = UIUtils.getVendorString(Accounter.constants()
				.supplier(), Accounter.constants().vendor());
	}

	public NewCreditMemoAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = UIUtils.getVendorString(Accounter.constants()
				.supplier(), Accounter.constants().vendor());
	}

	public NewCreditMemoAction(String newCreditMemo, String string,
			ClientVendorCreditMemo vendorCreditMemo,
			AsyncCallback<Object> callBack) {
		super(newCreditMemo, string);
		this.catagory = UIUtils.getVendorString(Accounter.constants()
				.supplier(), Accounter.constants().vendor());
	}

	@Override
	public void run(Object data, Boolean isDependent) {

		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {

				// //UIUtils.logError("Failed to load VendorCreditMemo...", t);

			}

			public void onCreated() {

				view = VendorCreditMemoView.getInstance();

				try {

					// UIUtils.setCanvas(view, getViewConfiguration());
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, NewCreditMemoAction.this);

				} catch (Throwable t) {

					onCreateFailed(t);

				}

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
		return UIUtils.getVendorString("supplierCredit", "vendorCredit");
	}

}
