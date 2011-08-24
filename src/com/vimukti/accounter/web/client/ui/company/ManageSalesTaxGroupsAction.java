package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.SalesTaxGroupListView;
import com.vimukti.accounter.web.client.ui.core.Action;

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
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				view = new SalesTaxGroupListView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, ManageSalesTaxGroupsAction.this);

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

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
