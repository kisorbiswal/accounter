package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class InventoryStockStatusByItemAction extends Action {

	protected InventoryStockStatusByItemReport report;

	public InventoryStockStatusByItemAction() {
		super();
		this.catagory = messages.report();
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {
				report = new InventoryStockStatusByItemReport();
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, InventoryStockStatusByItemAction.this);

			}

			public void onCreateFailed(Throwable t) {
				System.err.println("Failed to Load Report.." + t);
			}
		});

	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reports();
	}

	@Override
	public String getHistoryToken() {

		return "InventoryStockStatusByItemReport";
	}

	@Override
	public String getHelpToken() {
		return "inventory-stock-status-by-item-report";
	}

	@Override
	public String getText() {
		return messages.inventoryStockStatusByItem();
	}

}
