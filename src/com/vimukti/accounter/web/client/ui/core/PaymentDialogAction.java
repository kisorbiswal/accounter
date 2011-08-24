package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.resources.client.ImageResource;

public class PaymentDialogAction extends Action {

	public PaymentDialogAction(String text) {
		super(text);

	}

	@Override
	public ImageResource getBigImage() {

		return null;
	}

	@Override
	public ImageResource getSmallImage() {

		return null;
	}

	// @Override
	// public ParentCanvas getView() {
	//
	// return null;
	// }

	@Override
	public void run() {
		PaymentDialog diaog = new PaymentDialog();
		diaog.show();
	}

	@Override
	public String getHistoryToken() {

		return null;
	}

	@Override
	public String getHelpToken() {
		return "payment-list";
	}

}
