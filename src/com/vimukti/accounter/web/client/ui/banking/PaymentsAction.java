package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.company.PaymentListView;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class PaymentsAction extends Action {

	public PaymentsAction(String text) {
		super(text);
		this.catagory = Accounter.constants().banking();
	}

	@Override
	public void run() {

		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				PaymentListView view = PaymentListView.getInstance();

				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, PaymentsAction.this);

			}

		});
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().payments();
	}

	@Override
	public String getHistoryToken() {
		return "payments";
	}
}
