package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class WarehouseTransferListAction extends Action {

	private WarehouseTransferListView view;

	public WarehouseTransferListAction(String text) {
		super(text);
		this.catagory = Accounter.constants().settings();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	private void runAsync(Object data, Boolean isDependent) {
		try {
			view = new WarehouseTransferListView();
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
		return "warehouse";
	}

}
