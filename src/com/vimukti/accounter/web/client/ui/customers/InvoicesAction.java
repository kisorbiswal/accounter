package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * 
 * @author Raj Vimal
 */

public class InvoicesAction extends Action {

	protected InvoiceListView view;
	public String viewType;

	public InvoicesAction(String text) {
		super(text);
		this.catagory = Accounter.constants().customer();
	}

	public InvoicesAction(String text, String iconString, String viewType) {
		super(text, iconString);
		this.catagory = Accounter.constants().customer();
		this.viewType = viewType;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);

	}

	public void run(Object data, Boolean isDependent, String viewType) {
		this.viewType = viewType;
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {
					if (viewType == null)
						view = new InvoiceListView();
					else
						view = new InvoiceListView(viewType);
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, InvoicesAction.this);
					// UIUtils.setCanvas(view, getViewConfiguration());

				} catch (Throwable e) {
					onCreateFailed(e);

				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load the InvoiceList", t);

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
		return Accounter.getFinanceMenuImages().invoices();
	}

	@Override
	public String getImageUrl() {
		return "/images/invoices.png";
	}

	@Override
	public String getHistoryToken() {
		if (viewType == null) {
			return "invoices";
		} else if (viewType.equals(InvoiceListView.OVER_DUE)) {
			return "overDueInvoices";
		}
		return "";
	}
}
