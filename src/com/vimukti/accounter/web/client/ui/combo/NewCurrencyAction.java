package com.vimukti.accounter.web.client.ui.combo;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;
import com.vimukti.accounter.web.client.ui.customers.CurrencyGroupListDialog;
import com.vimukti.accounter.web.client.ui.customers.NewCurrencyListDialog;

public class NewCurrencyAction extends Action<ClientCurrency> {
	private AccounterConstants constants = Global.get().constants();

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

				// TODO

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
