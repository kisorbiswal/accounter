package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.ui.core.Action;

public class ReconcileAccountAction extends Action {

	public ReconcileAccountAction(String text) {
		super(text);
	}

	/**
	 * Runs this action with call back.The default implementation of this method
	 * in <code>Action</code> does nothing.
	 */
	public void run(AccounterAsyncCallback<Object> AccounterAsyncCallback) {
	}

	// its not using any where

	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	// its not using any where
	@Override
	public void run() {

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "recouncileAccount";
	}

	@Override
	public String getHelpToken() {
		return "reconcile-account";
	}

}
