package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.PaymentTermListDialog;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class PaymentTermListAction extends Action {
	public PaymentTermListAction(String text) {
		super(text);
	}

	@Override
	public void run() {

		runAsync(data, isDependent);
	}

	private void runAsync(Object data, Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				PaymentTermListDialog dialog = new PaymentTermListDialog();
				dialog.show();

			}

		});
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().paymentTermsList();
	}

	@Override
	public String getHistoryToken() {
		return "paymentTerms";
	}

	@Override
	public String getHelpToken() {
		return "payment_tem-list";
	}
}
