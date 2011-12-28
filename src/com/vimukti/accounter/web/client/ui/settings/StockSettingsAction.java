package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class StockSettingsAction extends Action {

	StockSettingsView view;

	public StockSettingsAction() {
		super();
		this.catagory = messages.stockSettings();
	}

	@Override
	public void run() {
		runAysnc(data, isDependent);
	}

	private void runAysnc(final Object data, final boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = new StockSettingsView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, StockSettingsAction.this);

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
		return "stockSettings";
	}

	@Override
	public String getHelpToken() {
		return "stockSettings";
	}

	@Override
	public String getText() {
		return messages.stockSettings();
	}

}
