package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class PreviousClaimAction extends Action {

	PreviousClaimsView view;

	public PreviousClaimAction(String text) {
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
	//
	// return null;
	// }

	@Override
	public void run() {

		view = new PreviousClaimsView();
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
		return "previous-claim";
	}

}
