package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class StopOnlineSharingTransferAction extends Action {

	public StopOnlineSharingTransferAction(String text) {
		super(text);
	}

	public StopOnlineSharingTransferAction(String text, String iconString) {
		super(text, iconString);
	}

	
	@Override
	public ParentCanvas getView() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		// currently not required for this class

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
		return "StopOnlineSharingTransfer";
	}

}
