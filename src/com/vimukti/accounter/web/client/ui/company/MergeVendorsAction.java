package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.core.Action;

public class MergeVendorsAction extends Action {

	public MergeVendorsAction(String text) {
		super(text);
	}

	// @Override
	// public ParentCanvas getView() {
	// // NOTHING TO DO.
	// return null;
	// }

	@Override
	public void run() {
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
		return "mergeVendors";
	}

	@Override
	public String getHelpToken() {
		return "merge-vendors";
	}

}
