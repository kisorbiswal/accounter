package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientSalesOrder;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class SalesOrderAction extends Action {

	private SalesOrderView view;

	public SalesOrderAction(String text) {
		super(text);
		this.catagory = Accounter.constants().sales();
	}

	public SalesOrderAction(String text, ClientSalesOrder salesOrder,
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

		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				view = new SalesOrderView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, SalesOrderAction.this);

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

			}
		});
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().salesOrder();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/Sales-order.png";
	// }

	@Override
	public String getHistoryToken() {
		return "salesOrder";
	}

	@Override
	public String getHelpToken() {
		return "new-sales";
	}
}
