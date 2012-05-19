package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.SalesTaxGroupListView;
import com.vimukti.accounter.web.client.ui.core.Action;

public class ManageSalesTaxGroupsAction extends Action<ClientTAXGroup> {


	public ManageSalesTaxGroupsAction() {
		super();
		this.catagory = messages.company();
	}

	@Override
	public void run() {

		runAsync(data, isDependent);

	}

	private void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				SalesTaxGroupListView view = new SalesTaxGroupListView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, ManageSalesTaxGroupsAction.this);
				
			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
//		AccounterAsync.createAsync(new CreateViewAsyncCallback() {
//
//			@Override
//			public void onCreated() {
//				
//
//			}
//		});
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

	@Override
	public String getText() {
		String text;
		if (Accounter.getUser().canDoInvoiceTransactions())
			text = messages.manageSalesTaxGroups();
		else
			text = messages.salesTaxGroups();
		return text;
	}

}
