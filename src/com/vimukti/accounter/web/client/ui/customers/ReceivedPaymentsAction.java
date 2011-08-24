package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

/**
 * 
 * @author Raj Vimal
 */

public class ReceivedPaymentsAction extends Action {

	protected ReceivedPaymentListView view;

	public ReceivedPaymentsAction(String text) {
		super(text);
		this.catagory = Global.get().customer();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isEditable) {

		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				view = new ReceivedPaymentListView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isEditable, ReceivedPaymentsAction.this);

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
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().receivedPayments();
	}

	// @Override
	// public String getImageUrl() {
	//
	// return "/images/recived_payment_list.png";
	// }

	@Override
	public String getHistoryToken() {

		return "receivePayments";
	}

	@Override
	public String getHelpToken() {
		return "receive-payment";
	}
}
