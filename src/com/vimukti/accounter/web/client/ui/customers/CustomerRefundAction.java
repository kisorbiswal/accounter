package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCustomerRefund;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author Raj Vimal
 */

public class CustomerRefundAction extends Action {

	protected CustomerRefundView view;

	public CustomerRefundAction(String text) {
		super(text);
		this.catagory = Global.get().Customer();
	}

	public CustomerRefundAction(String text,
			ClientCustomerRefund customerRefund,
			AccounterAsyncCallback<Object> callBack) {
		super(text);
		this.catagory = Global.get().Customer();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isEditable) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = new CustomerRefundView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isEditable, CustomerRefundAction.this);

			}

		});
	}

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newCustomerRefund();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/customer_refunds.png";
	// }

	@Override
	public String getHistoryToken() {
		return "customerRefund";
	}

	@Override
	public String getHelpToken() {
		return "customer-refund";
	}
}
