package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
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
		this.catagory = FinanceApplication.getCustomersMessages().customer();
	}

	public InvoicesAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = FinanceApplication.getCustomersMessages().customer();
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
		return FinanceApplication.getFinanceMenuImages().invoices();
	}

	@Override
	public String getImageUrl() {

		return "/images/invoices.png";
	}
}
