package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class InventoryItemReportAction extends Action {

	protected InventoryItemReport report;

	public InventoryItemReportAction() {
		super();
		this.catagory = messages.report();
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {
				report = new InventoryItemReport();
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, InventoryItemReportAction.this);

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

		return "InventoryItemReport";
	}

	@Override
	public String getHelpToken() {
		return "inventory-item";
	}

	@Override
	public String getText() {
		return messages.inventoryItem();
	}

}
