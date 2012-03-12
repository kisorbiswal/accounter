package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.VendorMergeDialog;
import com.vimukti.accounter.web.client.ui.core.Action;

public class MergeVendorAction extends Action {

	public MergeVendorAction() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		if (Accounter.hasPermission(Features.MERGING)) {
			VendorMergeDialog vendorMergeDialog = new VendorMergeDialog(
					messages.mergeVendors(Global.get().vendors()),
					messages.payeeMergeDescription(Global.get().vendor()));
			vendorMergeDialog.show();
		} else {
			if (!isCalledFromHistory) {
				Accounter.showSubscriptionWarning();
			}
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
		return "merge_vendor";
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText() {
		return messages.mergeVendors(Global.get().vendors());
	}

}
