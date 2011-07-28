package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class ImportBankFilesAction extends Action {

	public ImportBankFilesAction(String text) {
		super(text);
		this.catagory = Accounter.constants().banking();
	}

	public ImportBankFilesAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.constants().banking();
	}

	/**
	 * Runs this action with call back.The default implementation of this method
	 * in <code>Action</code> does nothing.
	 */
	public void run(AsyncCallback<Object> asyncCallback) {
	}

	
	@Override
	public ParentCanvas getView() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		// TODO Auto-generated method stub
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
		return "importBankFiles";
	}

}
