package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.ui.core.Action;

public class SignupforCreditcardprocessingAction extends Action {

	public SignupforCreditcardprocessingAction(String text) {
		super(text);
	}

	public SignupforCreditcardprocessingAction(String text, String iconString) {
		super(text, iconString);
	}

	/**
	 * Runs this action with call back.The default implementation of this method
	 * in <code>Action</code> does nothing.
	 */
	public void run(AsyncCallback<Object> asyncCallback) {
	}

	// its not using any where

	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	@Override
	public void run(Object data, Boolean isDependent) {

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "SignupforCreditcard";
	}

}
