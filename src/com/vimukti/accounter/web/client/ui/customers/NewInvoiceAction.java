package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author Raj Vimal
 */

public class NewInvoiceAction extends Action<ClientInvoice> {

	protected InvoiceView view;

	public NewInvoiceAction() {
		super();
	}

	public NewInvoiceAction(ClientInvoice invoice,
			AccounterAsyncCallback<Object> callback) {
		super();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = InvoiceView.getInstance();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewInvoiceAction.this);
			}
		});
	}

	// @Override
	// public ParentCanvas<?> getView() {
	// // NOTHING TO DO.
	// return null;
	// }
	@Override
	public String getCatagory() {
		return Global.get().Customer();
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newInvoice();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/new_invoice.png";
	// }

	@Override
	public String getHistoryToken() {
		return "newInvoice";
	}

	@Override
	public String getHelpToken() {
		return "customer-invoice";
	}

	@Override
	public String getText() {
		return messages.newInvoice();
	}
}
