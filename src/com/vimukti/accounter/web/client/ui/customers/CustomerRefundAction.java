package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientCustomerRefund;
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

public class CustomerRefundAction extends Action {

	protected CustomerRefundView view;

	public CustomerRefundAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.constants().customer();
	}

	public CustomerRefundAction(String text, String iconString,
			ClientCustomerRefund customerRefund, AsyncCallback<Object> callBack) {
		super(text, iconString);
		this.catagory = Accounter.constants().customer();
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isEditable) {

		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {
				try {

					view = new CustomerRefundView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isEditable, CustomerRefundAction.this);

				} catch (Throwable e) {
					onCreateFailed(e);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Customer Refund View", t);
			}
		});
	}

	
//	@Override
//	public ParentCanvas getView() {
//		return this.view;
//	}

	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newCustomerRefund();
	}

//	@Override
//	public String getImageUrl() {
//		return "/images/customer_refunds.png";
//	}

	@Override
	public String getHistoryToken() {
		return "customerRefund";
	}
}
