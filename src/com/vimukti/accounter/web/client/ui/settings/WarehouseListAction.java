package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class WarehouseListAction extends Action<ClientWarehouse> {

	
	public WarehouseListAction() {
		super();
		this.catagory = messages.inventory();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	private void runAsync(Object data, Boolean isDependent) {
		try {
			WarehouseListView view = new WarehouseListView();
			MainFinanceWindow.getViewManager().showView(view, data,
					isDependent, WarehouseListAction.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	// @Override
	// public ParentCanvas getView() {
	//
	// return null;
	// }

	@Override
	public String getHistoryToken() {
		return "WarehouseList";
	}

	@Override
	public String getHelpToken() {
		return "WarehouseList";
	}

	@Override
	public String getText() {
		return messages.warehouseList();
	}

}
