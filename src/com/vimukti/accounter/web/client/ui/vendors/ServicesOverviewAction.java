package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.core.Action;

/**
 * 
 * @author Raj Vimal
 */

public class ServicesOverviewAction extends Action {

	public ServicesOverviewAction(String text) {
		super(text);
	}

	// its not using any where

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

	// @Override
	// public String getImageUrl() {
	// // NOTHING TO DO.
	// return "";
	// }

	@Override
	public String getHistoryToken() {
		return "ServicesIverview";
	}

	@Override
	public String getHelpToken() {
		return "service-overview";
	}
}
