package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.resources.client.ImageResource;

public class PaymentDialogAction extends Action {

	public PaymentDialogAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		PaymentDialog diaog = new PaymentDialog();
		diaog.show();
	}

}
