package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientStockTransfer;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class WareHouseTransferAction extends Action<ClientStockTransfer> {
	public WareHouseTransferAction() {
		super();
		this.catagory = messages.inventory();
	}

	

	@Override
	public ImageResource getBigImage() {

		return null;
	}

	@Override
	public String getHistoryToken() {
		return "wareHouseTransfer";
	}

	@Override
	public ImageResource getSmallImage() {

		return null;
	}

	//
	// @Override
	// public ParentCanvas getView() {
	//
	// return null;
	// }

	@Override
	public void run() {
		try {
			WareHouseTransferView view = new WareHouseTransferView();
			MainFinanceWindow.getViewManager().showView(view, data,
					isDependent, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getHelpToken() {
		return "wareHouseTransfer";
	}

	@Override
	public String getText() {
		return messages.wareHouseTransfer();
	}

}
