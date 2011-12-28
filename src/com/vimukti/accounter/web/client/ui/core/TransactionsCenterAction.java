package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.company.TransactionsCenterView;

public class TransactionsCenterAction extends Action<Object> {

	public TransactionsCenterAction() {
		super();
		this.catagory = messages.company();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				TransactionsCenterView<?> view = new TransactionsCenterView<Object>();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, TransactionsCenterAction.this);

			}

		});
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "transactionscenter";
	}

	@Override
	public String getHelpToken() {
		return "transactions center";
	}

	@Override
	public String getText() {
		return messages.transactionscenter();
	}

}
