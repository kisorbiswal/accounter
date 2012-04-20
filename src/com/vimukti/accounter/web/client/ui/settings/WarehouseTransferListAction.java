package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientStockTransfer;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class WarehouseTransferListAction extends Action<ClientStockTransfer> {

	

	public WarehouseTransferListAction() {
		super();
		this.catagory = messages.inventory();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	private void runAsync(Object data, Boolean isDependent) {
		try {
			WarehouseTransferListView view = new WarehouseTransferListView();
			MainFinanceWindow.getViewManager().showView(view, data, false,
					WarehouseTransferListAction.this);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// NOTHING To Do
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "WarehouseTransferList";
	}

	@Override
	public String getHelpToken() {
		return "WarehouseTransferList";
	}

	@Override
	public String getText() {
		return messages.warehouseTransferList();
	}

}
