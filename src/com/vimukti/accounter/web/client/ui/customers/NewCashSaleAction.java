package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientCashSales;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * 
 * @author kumar kasimala
 */

public class NewCashSaleAction extends Action {

	protected CashSalesView view;

	public NewCashSaleAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = FinanceApplication.getCustomersMessages().customer();
	}

	public NewCashSaleAction(String text, String iconString,
			ClientCashSales cashSales, AsyncCallback<Object> callback) {
		super(text, iconString, cashSales, callback);
		this.catagory = FinanceApplication.getCustomersMessages().customer();
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {
		// if (!isEdit)
		// MainFinanceWindow.addToTab(new CashSalesView(), getText());
		// else
		// MainFinanceWindow.addToTab(new CashSalesView(transaction),
		// getText());
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to load CashPurchase", t);
			}

			public void onCreated() {

				try {

					view = new CashSalesView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, NewCashSaleAction.this);
					// UIUtils.setCanvas(view, getViewConfiguration());

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
		return FinanceApplication.getFinanceMenuImages().newCashSale();
	}
	@Override
	public String getImageUrl() {
		
		return "/images/new_cash_sale.png";
	}
}
