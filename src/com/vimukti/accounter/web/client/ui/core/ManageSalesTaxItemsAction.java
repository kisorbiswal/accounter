package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.SalesTaxItemsView;

public class ManageSalesTaxItemsAction extends Action {

	public ManageSalesTaxItemsAction() {
		super();
		this.catagory = messages.company();
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
//		runAsync(data, isDependent);

	}

	private void runAsync(final Object data, final Boolean isDependent) {
//		GWT.runAsync(new RunAsyncCallback() {
//
//			public void onSuccess() {
				SalesTaxItemsView view = new SalesTaxItemsView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, ManageSalesTaxItemsAction.this);
//			}
//
//			public void onFailure(Throwable e) {
//				Accounter.showError(Global.get().messages()
//						.unableToshowtheview());
//			}
//		});
//		AccounterAsync.createAsync(new CreateViewAsyncCallback() {
//
//			@Override
//			public void onCreated() {
//				
//
//			}
//
//		});
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

	@Override
	public String getText() {
		String constant;
		if (Accounter.getUser().canDoInvoiceTransactions())
			constant = messages.manageSalesItems();
		else
			constant = messages.salesTaxItems();
		return constant;
	}

}
