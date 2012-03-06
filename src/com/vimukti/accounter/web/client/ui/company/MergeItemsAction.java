package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CustomerMergeDialog;
import com.vimukti.accounter.web.client.ui.ItemMergeDialog;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.banking.MergeCustomerAction;
import com.vimukti.accounter.web.client.ui.core.Action;

public class MergeItemsAction extends Action {

	public MergeItemsAction() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		if (Accounter.hasPermission(Features.MERGING)) {
			
			ItemMergeDialog customerMergeDialog = new ItemMergeDialog();
			MainFinanceWindow.getViewManager().showView(customerMergeDialog, data,
					isDependent, MergeItemsAction.this);
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
		return "merge_item";
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText() {
		return messages.mergeItems();
	}

}
