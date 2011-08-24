package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.PaymentTermListDialog;
import com.vimukti.accounter.web.client.ui.core.Action;

public class PaymentTermListAction extends Action {
	public PaymentTermListAction(String text) {
		super(text);
	}

	@Override
	public void run() {

		runAsync(data, isDependent);
	}

	private void runAsync(Object data, Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				PaymentTermListDialog dialog = new PaymentTermListDialog();
				dialog.show();

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
