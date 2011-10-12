package com.vimukti.accounter.web.client.ui.combo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class NewCurrencyAction extends Action<ClientCurrency> {

	public NewCurrencyAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public void run() {
		runAsync(data, isDependent);

	}

	private void runAsync(Object data, Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				// TODO Auto-generated method stub

			}

		});
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
		return "newCurrency";
	}

	@Override
	public String getHelpToken() {
		return "currency";
	}

}
