package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientSalesOrder;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class SalesOrderAction extends Action {

	private SalesOrderView view;

	public SalesOrderAction(String text, String icon) {
		super(text, icon);
		this.catagory = FinanceApplication.getCustomersMessages().customer();
	}

	public SalesOrderAction(String text, String icon,
			ClientSalesOrder salesOrder, AsyncCallback<Object> callback) {
		super(text, icon, salesOrder, callback);
		this.catagory = FinanceApplication.getCustomersMessages().customer();
	}

	@Override
	public ParentCanvas<?> getView() {
		return this.view;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			private SalesOrderView view;

			public void onCreated() {

				try {

					view = new SalesOrderView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, SalesOrderAction.this);
					// UIUtils.setCanvas(view, getViewConfiguration());

				} catch (Throwable t) {
					onCreateFailed(t);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Customer View..", t);
			}
		});

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return null;
	}

}
