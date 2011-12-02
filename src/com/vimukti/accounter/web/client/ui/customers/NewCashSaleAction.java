package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCashSales;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author kumar kasimala
 */

public class NewCashSaleAction extends Action {

	protected CashSalesView view;

	public NewCashSaleAction(String text) {
		super(text);
		this.catagory = Global.get().Customer();
	}

	public NewCashSaleAction(String text, ClientCashSales cashSales,
			AccounterAsyncCallback<Object> callback) {
		super(text);
		this.catagory = Global.get().Customer();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {
		// if (!isEdit)
		// MainFinanceWindow.addToTab(new CashSalesView(), getText());
		// else
		// MainFinanceWindow.addToTab(new CashSalesView(transaction),
		// getText());
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = new CashSalesView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewCashSaleAction.this);

			}

		});
	}

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newCashSale();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/new_cash_sale.png";
	// }

	@Override
	public String getHistoryToken() {
		return "newCashSale";
	}

	@Override
	public String getHelpToken() {
		return "new_cash-sale";
	}
}
