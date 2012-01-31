package com.vimukti.accounter.web.client.ui;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientInventoryAssembly;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class InventoryAssemblyAction extends Action<ClientInventoryAssembly> {

	private String itemname;

	@Override
	public String getText() {
		return messages.inventoryAssembly();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final ClientInventoryAssembly data,
			final boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				InventoryAssemblyView view = new InventoryAssemblyView();
				view.setItemName(itemname);
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, InventoryAssemblyAction.this);
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
		return "inventoryassembly";
	}

	@Override
	public String getHelpToken() {
		return "inventoryassembly";
	}

	public void setItemText(String itemname) {
		this.itemname = itemname;
	}

}
