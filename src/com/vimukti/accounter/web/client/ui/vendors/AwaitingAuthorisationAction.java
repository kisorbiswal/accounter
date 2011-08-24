package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class AwaitingAuthorisationAction extends Action {

	AwaitingAuthorisationView view;

	public AwaitingAuthorisationAction(String text) {
		super(text);
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	@Override
	public void run() {
		view = new AwaitingAuthorisationView();
		try {
			MainFinanceWindow.getViewManager().showView(view, data,
					isDependent, this);
		} catch (Exception e) {
		}
	}

	@Override
	public String getHistoryToken() {
		return null;
	}

	@Override
	public String getHelpToken() {
		return "awaiting-authorization";
	}
}
