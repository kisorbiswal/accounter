package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * 
 * @author Raj Vimal
 */

public class ServicesOverviewAction extends Action {

	public ServicesOverviewAction(String text) {
		super(text);
	}

	public ServicesOverviewAction(String text, String iconString) {
		super(text, iconString);
	}

	// its not using any where
	
	@Override
	public ParentCanvas getView() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {

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
	public String getImageUrl() {
		// NOTHING TO DO.
		return "";
	}

	@Override
	public String getHistoryToken() {
		return "ServicesIverview";
	}
}
