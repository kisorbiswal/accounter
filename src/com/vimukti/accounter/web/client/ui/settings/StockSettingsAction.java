package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class StockSettingsAction extends Action {

	StockSettingsView view;

	public StockSettingsAction(String text) {
		super(text);
		this.catagory = Accounter.constants().settings();
	}

	@Override
	public void run() {
		runAysnc(data, isDependent);
	}

	private void runAysnc(final Object data, final boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				view = new StockSettingsView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, StockSettingsAction.this);

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

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
		return null;
	}

	@Override
	public String getHelpToken() {
		return null;
	}

}
