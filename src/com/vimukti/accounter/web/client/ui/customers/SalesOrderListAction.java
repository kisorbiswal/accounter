package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientSalesOrder;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class SalesOrderListAction extends Action {

	private SalesOrderListView view;

	public SalesOrderListAction(String text) {
		super(text);
		this.catagory = Accounter.constants().customer();
	}

	public SalesOrderListAction(String text, ClientSalesOrder salesOrder,
			AccounterAsyncCallback<Object> callback) {
		super(text);
		this.catagory = Accounter.constants().customer();
	}

	// @Override
	// public ParentCanvas<?> getView() {
	// return this.view;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {

		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				view = new SalesOrderListView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, SalesOrderListAction.this);

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

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

	@Override
	public String getHelpToken() {
		return "sales_order-list";
	}

}
