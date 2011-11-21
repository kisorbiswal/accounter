package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientStockAdjustment;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class StockAdjustmentAction extends Action<ClientStockAdjustment> {

	StockAdjustmentView view;

	public StockAdjustmentAction(String text) {
		super(text);
		this.catagory = messages.inventory();
	}

	@Override
	public void run() {
		try {
			view = new StockAdjustmentView();
			MainFinanceWindow.getViewManager().showView(view, data,
					isDependent, this);
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
		return "stockAdjustment";
	}

	@Override
	public String getHelpToken() {
		return "stockAdjustment";
	}

}
