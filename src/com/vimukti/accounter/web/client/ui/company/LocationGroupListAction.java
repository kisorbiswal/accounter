package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.LocationGroupListDialog;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class LocationGroupListAction extends Action {

	public LocationGroupListAction(String text) {
		super(text);
		this.catagory = Accounter.constants().company();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(Object data, boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {

				LocationGroupListDialog dialog = new LocationGroupListDialog(
						Accounter.constants().manageLocationGroup(), Accounter
								.constants().toAddLocation());
				dialog.show();

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
		return "location-group-list";
	}

	@Override
	public String getHelpToken() {
		return "location-group-list";
	}

}
