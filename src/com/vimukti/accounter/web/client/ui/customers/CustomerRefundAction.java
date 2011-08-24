package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientCustomerRefund;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

/**
 * 
 * @author Raj Vimal
 */

public class CustomerRefundAction extends Action {

	protected CustomerRefundView view;

	public CustomerRefundAction(String text) {
		super(text);
		this.catagory = Accounter.constants().customer();
	}

	public CustomerRefundAction(String text,
			ClientCustomerRefund customerRefund,
			AccounterAsyncCallback<Object> callBack) {
		super(text);
		this.catagory = Accounter.constants().customer();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isEditable) {

		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				view = new CustomerRefundView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isEditable, CustomerRefundAction.this);

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

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
