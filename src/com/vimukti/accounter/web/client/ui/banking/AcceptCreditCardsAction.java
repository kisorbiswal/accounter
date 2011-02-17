package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class AcceptCreditCardsAction extends Action {

	public AcceptCreditCardsAction(String text) {
		super(text);
		this.catagory = FinanceApplication.getFinanceUIConstants().banking();
	}

	public AcceptCreditCardsAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = FinanceApplication.getFinanceUIConstants().banking();
	}

	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	//
	// }

	/**
	 * Runs this action with call back.The default implementation of this method
	 * in <code>Action</code> does nothing.
	 */
	public void run(AsyncCallback<Object> asyncCallback) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		// TODO Auto-generated method stub

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return null;
	}

	
}
