package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class InventoryValuationDetailsAction extends Action {

	protected InventoryValuationDetailsReport report;
	private long id;

	public InventoryValuationDetailsAction() {
		super();
		this.catagory = messages.report();
	}

	public InventoryValuationDetailsAction(Long id) {
		super();
		this.catagory = messages.report();
		this.id = id;
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				report = new InventoryValuationDetailsReport(id);
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, InventoryValuationDetailsAction.this);

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

		return "InventoryValuationDetailReport";
	}

	@Override
	public String getHelpToken() {
		return "inventory-valuation-report";
	}

	@Override
	public String getText() {
		return messages.inventoryValuationDetails();
	}

}
