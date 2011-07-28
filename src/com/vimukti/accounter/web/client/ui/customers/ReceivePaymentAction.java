package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientReceivePayment;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * 
 * @author kumar kasimala
 */

public class ReceivePaymentAction extends Action {

	protected ReceivePaymentView view;

	public ReceivePaymentAction(String text, String iconString) {
		super(Accounter.constants().newReceivePayment(), iconString);
		this.catagory = Accounter.constants().customer();
	}

	public ReceivePaymentAction(String text, String iconString,
			ClientReceivePayment receivePayment, AsyncCallback<Object> callback) {
		super(text, iconString, receivePayment, callback);
		this.catagory = Accounter.constants().customer();
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {
				try {

					view = new ReceivePaymentView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, ReceivePaymentAction.this);

				} catch (Throwable e) {
					onCreateFailed(e);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Recieve Payment View...",
				// t);

			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		return this.view;
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newReceivePayment();
	}

	@Override
	public String getImageUrl() {
		return "/images/recive_payments.png";
	}

	@Override
	public String getHistoryToken() {
		return "receivePayment";
	}
}
