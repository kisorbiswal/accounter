package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.SalesTaxGroupListView;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class ManageSalesTaxGroupsAction extends Action<ClientTAXGroup> {

	SalesTaxGroupListView view;

	public ManageSalesTaxGroupsAction(String text) {
		super(text);
		this.catagory = Accounter.constants().company();
	}

	@Override
	public void run() {

		runAsync(data, isDependent);

	}

	private void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = new SalesTaxGroupListView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, ManageSalesTaxGroupsAction.this);

			}
		});
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().manageSalesTaxGroup();
	}

	@Override
	public String getHistoryToken() {
		if (Accounter.getUser().canDoInvoiceTransactions())
			return "manageSalesTaxGroups";
		else
			return "salesTaxGroups";

	}

	@Override
	public String getHelpToken() {
		return "sales_tax-group";
	}

}
