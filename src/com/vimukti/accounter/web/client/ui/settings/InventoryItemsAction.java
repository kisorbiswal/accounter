package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class InventoryItemsAction extends Action<ClientItem> {

	public InventoryItemsAction(String text) {
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

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				InventoryItemsListView listview = new InventoryItemsListView();
				MainFinanceWindow.getViewManager().showView(listview, data,
						isDependent, InventoryItemsAction.this);
			}
		});
	}

	@Override
	public String getHistoryToken() {

		return null;
	}

	@Override
	public String getHelpToken() {
		return "inventory-item";
	}

}
