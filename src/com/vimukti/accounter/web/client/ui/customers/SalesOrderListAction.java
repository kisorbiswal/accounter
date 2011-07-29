package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientSalesOrder;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;

public class SalesOrderListAction extends Action {

	private SalesOrderListView view;

	public SalesOrderListAction(String text) {
		super(text);
		this.catagory = Accounter.constants().customer();
	}

	public SalesOrderListAction(String text, ClientSalesOrder salesOrder,
			AsyncCallback<Object> callback) {
		super(text);
		this.catagory = Accounter.constants().customer();
	}

	// @Override
	// public ParentCanvas<?> getView() {
	// return this.view;
	// }

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {

					view = new SalesOrderListView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, SalesOrderListAction.this);
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
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().salesOrderList();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/Sales-order-list.png";
	// }

	@Override
	public String getHistoryToken() {
		return "salesOrderList";
	}

}
