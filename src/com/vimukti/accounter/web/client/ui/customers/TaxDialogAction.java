package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.ui.core.Action;

public class TaxDialogAction extends Action<ClientTAXCode> {
	public TaxDialogAction(String text) {
		super(text);
	}

	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	@Override
	public void run() {
		TaxDialog dialog = new TaxDialog();
		dialog.setCallback(getCallback());
		dialog.show();

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return null;
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/record_expenses.png";
	// }

	@Override
	public String getHistoryToken() {
		return "TaxDialog";
	}
}
