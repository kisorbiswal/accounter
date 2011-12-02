package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientSalesOrder;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class SalesOrderListAction extends Action {

	private SalesOrderListView view;

	public SalesOrderListAction(String text) {
		super(text);
		this.catagory = Global.get().Customer();
	}

	public SalesOrderListAction(String text, ClientSalesOrder salesOrder,
			AccounterAsyncCallback<Object> callback) {
		super(text);
		this.catagory = Global.get().Customer();
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
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = new SalesOrderListView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, SalesOrderListAction.this);

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
