package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class WareHouseViewAction extends Action<ClientWarehouse> {

	private WareHouseView view;

	public WareHouseViewAction(String text) {
		super(text);
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(Object data, Boolean isDependent) {
		view = new WareHouseView();
		try {
			MainFinanceWindow.getViewManager().showView(view, data, false,
					WareHouseViewAction.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	//
	// return null;
	// }

	@Override
	public String getHistoryToken() {
		return "wareHouse";
	}

	@Override
	public String getHelpToken() {
		return "warehouse";
	}

}
