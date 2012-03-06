package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CustomerMergeDialog;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.customers.InvoiceView;
import com.vimukti.accounter.web.client.ui.customers.NewInvoiceAction;

public class MergeCustomerAction extends Action {

	public MergeCustomerAction() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		if (Accounter.hasPermission(Features.MERGING)) {
			CustomerMergeDialog customerMergeDialog = new CustomerMergeDialog();
			MainFinanceWindow.getViewManager().showView(customerMergeDialog, data,
					isDependent, MergeCustomerAction.this);
		} else {
			Accounter.showSubscriptionWarning();
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
		// TODO Auto-generated method stub
		return "merge_customers";
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return messages.mergeCustomers(Global.get().customers());
	}

}
