package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class WareHouseTransferAction extends Action {
	public WareHouseTransferAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	private WareHouseTransferView view;

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

	@Override
	public ParentCanvas getView() {

		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		try {
			view = new WareHouseTransferView();
			MainFinanceWindow.getViewManager().showView(view, data,
					isDependent, this);
		} catch (Exception e) {
			System.err
					.println("Unable to load WareHouse Transfer View. Because "
							+ e + " ocuured");
		}
	}

}
