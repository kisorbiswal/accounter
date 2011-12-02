package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.CustomerMergeDialog;
import com.vimukti.accounter.web.client.ui.core.Action;

public class MergeCustomerAction extends Action{

	public MergeCustomerAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		
		CustomerMergeDialog customerMergeDialog = new CustomerMergeDialog(
				messages.mergeCustomers(Global.get().Customer()),
				messages.payeeMergeDescription(Global.get().customer()));

		customerMergeDialog.show();		
		

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

}
