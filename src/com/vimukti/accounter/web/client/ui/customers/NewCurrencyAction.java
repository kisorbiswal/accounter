package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class NewCurrencyAction extends Action {
	private CurrencyView view;

	public NewCurrencyAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public void run() {
		runAsync(data, isDependent);

	}

	private void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				// TODO Auto-generated method stub
				view = new CurrencyView();
				try {
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, NewCurrencyAction.this);
				} catch (Throwable t) {
					onCreateFailed(t);
				}

			}

			@Override
			public void onCreateFailed(Throwable t) {
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

}
