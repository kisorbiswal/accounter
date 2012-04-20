package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientItemStatus;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class WareHouseItemsListAction extends Action<ClientItemStatus> {

	
	long wareHouse;

	public WareHouseItemsListAction(long wareHouse) {
		super();
		this.wareHouse = wareHouse;
		this.catagory = messages.wareHouse();
	}

	@Override
	public void run() {
		try {
			WareHouseItemsListView view = new WareHouseItemsListView(wareHouse);
			MainFinanceWindow.getViewManager().showView(view, data,
					isDependent, WareHouseItemsListAction.this);
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

	@Override
	public String getHistoryToken() {
		return "wareHouseItems";
	}

	@Override
	public String getHelpToken() {
		return "wareHouseItems";
	}

	@Override
	public String getText() {
		return messages.wareHouseItems();
	}

}
