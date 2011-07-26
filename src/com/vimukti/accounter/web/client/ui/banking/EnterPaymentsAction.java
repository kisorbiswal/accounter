package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class EnterPaymentsAction extends Action {

	public EnterPaymentsAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.getBankingsMessages().banking();
	}

	/**
	 * Runs this action with call back.The default implementation of this method
	 * in <code>Action</code> does nothing.
	 */
	public void run(AsyncCallback<Object> asyncCallback) {
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		// TODO Auto-generated method stub

	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	public ImageResource getBigImage() {
		return null;
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return null;
	}

}
