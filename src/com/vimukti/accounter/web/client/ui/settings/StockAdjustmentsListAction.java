package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientStockAdjustment;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class StockAdjustmentsListAction extends Action<ClientStockAdjustment> {

	public StockAdjustmentsListAction() {
		super();
		this.catagory = messages.inventory();
	}

	@Override
	public void run() {
		try {
			StockAdjustmentsListView view = new StockAdjustmentsListView();
			MainFinanceWindow.getViewManager().showView(view, data,
					isDependent, StockAdjustmentsListAction.this);
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
		return "stockAdjustments";
	}

	@Override
	public String getHelpToken() {
		return "stockAdjustments";
	}

	@Override
	public String getText() {
		return messages.stockAdjustments();
	}

}
