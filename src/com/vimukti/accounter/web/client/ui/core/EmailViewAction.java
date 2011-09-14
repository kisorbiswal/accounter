package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.customers.EmailView;

public class EmailViewAction extends Action {

	public EmailViewAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	private void runAsync(Object invoice, Boolean boolean1) {
		EmailView emailView = new EmailView((ClientInvoice) data);

		MainFinanceWindow.getViewManager().showView(emailView, data,
				isDependent, EmailViewAction.this);
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

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return "email";
	}

	@Override
	public String getHelpToken() {
		return "email-view";
	}

}
