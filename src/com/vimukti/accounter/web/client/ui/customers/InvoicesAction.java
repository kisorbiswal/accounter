package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author Raj Vimal
 */

public class InvoicesAction extends Action {

	protected InvoiceListView view;
	public String viewType;

	public InvoicesAction(String text) {
		super(text);
		this.catagory = Global.get().Customer();
	}

	public InvoicesAction(String text, String viewType) {
		super(text);
		this.catagory = Global.get().Customer();
		this.viewType = viewType;
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void run(Object data, Boolean isDependent, String viewType) {
		this.viewType = viewType;
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				if (viewType == null)
					view = new InvoiceListView();
				else
					view = new InvoiceListView(viewType);
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, InvoicesAction.this);

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
		return Accounter.getFinanceMenuImages().invoices();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/invoices.png";
	// }

	@Override
	public String getHistoryToken() {
		if (viewType == null) {
			return "invoices";
		} else if (viewType.equals(InvoiceListView.OVER_DUE)) {
			return "overDueInvoices";
		}
		return "";
	}

	@Override
	public String getHelpToken() {
		return "about-invoice";
	}
}
