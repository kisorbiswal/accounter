package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author Raj Vimal
 */

public class ReceivedPaymentsAction extends Action {

	protected ReceivedPaymentListView view;

	public ReceivedPaymentsAction() {
		super();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isEditable) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = new ReceivedPaymentListView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isEditable, ReceivedPaymentsAction.this);

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
	public String getCatagory() {
		return Global.get().Customer();
	}

	@Override
	public String getHistoryToken() {
		return "receivePayments";
	}

	@Override
	public String getHelpToken() {
		return "receive-payment";
	}

	@Override
	public String getText() {
		return messages.receivedPayments();
	}
}
