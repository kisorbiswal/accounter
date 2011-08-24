package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;

/**
 * 
 * @author Raj Vimal
 */

public class CountryRegionListAction extends Action {

	public CountryRegionListAction(String text) {
		super(text);
	}

	// @Override
	// public ParentCanvas<?> getView() {
	// return null;
	// }

	@Override
	public void run() {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				CountryRegionDialog dialog = new CountryRegionDialog("", "");
				dialog.show();

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

			}
		});
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "CountryRegionList";
	}

	@Override
	public String getHelpToken() {
		return "country-region-list";
	}

}
