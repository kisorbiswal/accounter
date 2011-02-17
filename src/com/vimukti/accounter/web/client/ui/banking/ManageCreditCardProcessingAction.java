package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class ManageCreditCardProcessingAction extends Action {

	public ManageCreditCardProcessingAction(String text) {
		super(text);
		this.catagory = FinanceApplication.getBankingsMessages().banking();
	}

	public ManageCreditCardProcessingAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = FinanceApplication.getBankingsMessages().banking();
	}

	/**
	 * Runs this action with call back.The default implementation of this method
	 * in <code>Action</code> does nothing.
	 */

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
