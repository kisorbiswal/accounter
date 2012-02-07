package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class InventoryStockStatusByVendorAction extends Action {

	protected InventoryStockStatusByVendorReport report;
	private long id;

	public InventoryStockStatusByVendorAction() {
		super();
		this.catagory = messages.report();
	}

	public InventoryStockStatusByVendorAction(Long id) {
		super();
		this.catagory = messages.report();
		this.id = id;
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				report = new InventoryStockStatusByVendorReport(id);
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, InventoryStockStatusByVendorAction.this);

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

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reports();
	}

	@Override
	public String getHistoryToken() {

		return "InventoryStockStatusByVendorReport";
	}

	@Override
	public String getHelpToken() {
		return "inventory-stock-status-by-vendor-report";
	}

	@Override
	public String getText() {
		return messages.inventoryStockStatusByVendor();
	}

}
