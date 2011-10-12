package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.SalesTaxItemsView;

public class ManageSalesTaxItemsAction extends Action {
	protected SalesTaxItemsView view;

	public ManageSalesTaxItemsAction(String text) {
		super(text);
		this.catagory = Accounter.constants().company();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	private void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = new SalesTaxItemsView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, ManageSalesTaxItemsAction.this);

			}

		});
	}

	@Override
	public String getHistoryToken() {
		if (Accounter.getUser().canDoInvoiceTransactions())
			return "manageSalesTaxItems";
		else
			return "salesTaxItems";

	}

	@Override
	public String getHelpToken() {
		return "pay_sales-tax";
	}

}
