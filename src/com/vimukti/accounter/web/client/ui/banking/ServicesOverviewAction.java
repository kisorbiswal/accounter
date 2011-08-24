package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.ui.core.Action;

public class ServicesOverviewAction extends Action {

	public ServicesOverviewAction(String text) {
		super(text);
	}

	/**
	 * Runs this action with call back.The default implementation of this method
	 * in <code>Action</code> does nothing.
	 */
	public void run(AccounterAsyncCallback<Object> AccounterAsyncCallback) {
	}

	// @Override
	// public ParentCanvas getView() {
	// // NOTHING TO DO.
	// return null;
	// }

	@Override
	public void run() {

	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "servicesOverview";
	}

	@Override
	public String getHelpToken() {
		return "customer-service";
	}

}
