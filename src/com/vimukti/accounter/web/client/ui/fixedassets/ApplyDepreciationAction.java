package com.vimukti.accounter.web.client.ui.fixedassets;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;

public class ApplyDepreciationAction extends Action {

	public ApplyDepreciationAction(String text) {
		super(text);
		this.catagory = Accounter.constants().fixedAssets();
	}

	@Override
	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// NOTHING TO DO
		return null;
	}

	// @Override
	// public ParentCanvas getView() {
	// // NOTHING TO DO
	// return null;
	// }

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getHistoryToken() {
		return "ApplyDepreciation";
	}

	@Override
	public String getHelpToken() {
		return "depreciation";
	}

}
