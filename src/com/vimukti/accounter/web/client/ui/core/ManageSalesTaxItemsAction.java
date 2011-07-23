package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.SalesTaxItemsView;

public class ManageSalesTaxItemsAction extends Action {
	protected SalesTaxItemsView view;

	public ManageSalesTaxItemsAction(String text) {
		super(text);
		this.catagory = Accounter.getCompanyMessages().company();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public ParentCanvas getView() {
		return this.view;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);

	}

	private void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {

			}

			public void onCreated() {
				try {
					view = new SalesTaxItemsView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, ManageSalesTaxItemsAction.this);
				} catch (Throwable e) {
					onCreateFailed(e);

				}

			}
		});

	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		if (Accounter.getUser().canDoInvoiceTransactions())
			return "manageSalesTaxItems";
		else
			return "salesTaxItems";

	}

}
