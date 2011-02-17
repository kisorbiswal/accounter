package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;
import com.vimukti.accounter.web.client.ui.forms.FormItem;

public class TaxDialogAction extends Action {
	public TaxDialogAction(String text) {
		super(text);
	}

	public TaxDialogAction(String text, String iconString) {
		super(text, iconString);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> void run(AsyncCallback<T> callBack,FormItem actionSource, Object data,
			Boolean isDependent) {
		new TaxDialog(callBack,actionSource).show();
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		new TaxDialog().show();

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getImageUrl() {
		// TODO Auto-generated method stub
		return "/images/record_expenses.png";
	}
}
