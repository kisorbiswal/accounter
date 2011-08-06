package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

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
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {

				try {
					CountryRegionDialog dialog = new CountryRegionDialog("", "");
					dialog.show();
				} catch (Exception e) {
					onCreateFailed(e);
				}

			}

			@Override
			public void onCreateFailed(Throwable t) {
				// UIUtils.logError("Failed To Load Credit rating", t);
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

}
