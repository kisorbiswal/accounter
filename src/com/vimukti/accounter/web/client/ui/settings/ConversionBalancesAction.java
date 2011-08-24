package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class ConversionBalancesAction extends Action {
	private ConversionBalancesView view;

	public ConversionBalancesAction(String text) {
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
	// return null;
	// }

	public void run(Object data, Boolean isDependent, String endingDate,
			String year) {
		runAsync(data, isDependent, endingDate, year);
	}

	@Override
	public void run() {
		runAsync(data, isDependent, null, null);
	}

	private void runAsync(final Object data, final Boolean isDependent,
			final String endingDate, final String year) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				view = new ConversionBalancesView(endingDate, year);
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, ConversionBalancesAction.this);

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

			}
		});
	}

	@Override
	public String getHistoryToken() {
		return null;
	}

	@Override
	public String getHelpToken() {
		return "conversion-balance";
	}

}
