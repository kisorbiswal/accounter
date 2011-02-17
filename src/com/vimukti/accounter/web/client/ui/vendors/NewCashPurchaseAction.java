package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * 
 * @author Raj Vimal
 * @modified by Ravi kiran.G
 */

public class NewCashPurchaseAction extends Action {

	protected CashPurchaseView view;

	public NewCashPurchaseAction(String text) {
		super(text);
		this.catagory = UIUtils.getVendorString(FinanceApplication
				.getVendorsMessages().supplier(), FinanceApplication
				.getVendorsMessages().vendor());
	}

	public NewCashPurchaseAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = UIUtils.getVendorString(FinanceApplication
				.getVendorsMessages().supplier(), FinanceApplication
				.getVendorsMessages().vendor());
	}

	public NewCashPurchaseAction(String newCashPurchase, String string,
			ClientCashPurchase cashPurchase, AsyncCallback<Object> callback) {
		super(newCashPurchase, string, cashPurchase, callback);
		this.catagory = UIUtils.getVendorString(FinanceApplication
				.getVendorsMessages().supplier(), FinanceApplication
				.getVendorsMessages().vendor());
	}

	@Override
	public void run(Object data, Boolean isDependent) {

		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {

				// //UIUtils.logError("Failed to load CashPurchase...", t);
			}

			public void onCreated() {

				view = CashPurchaseView.getInstance();

				try {

					// UIUtils.setCanvas(view, getViewConfiguration());
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, NewCashPurchaseAction.this);

				} catch (Throwable t) {

					onCreateFailed(t);

				}

			}

		});

	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		return this.view;
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return FinanceApplication.getFinanceMenuImages().newCashPurchage();
	}

	@Override
	public String getImageUrl() {
		// TODO Auto-generated method stub
		return "/images/new_cash_purchase.png";
	}

}
