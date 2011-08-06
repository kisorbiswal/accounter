package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.forms.FormItem;

public class TaxDialogAction extends Action {
	public TaxDialogAction(String text) {
		super(text);
	}

	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	public <T> void run(AccounterAsyncCallback<T> callBack, FormItem actionSource,
			Object data, Boolean isDependent) {
		new TaxDialog(callBack, actionSource).show();
	}

	@Override
	public void run() {
		new TaxDialog().show();

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
