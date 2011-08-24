package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;

/**
 * 
 * @author Raj Vimal
 */

public class CreateStatementAction extends Action {

	private boolean isEdit;

	public CreateStatementAction(String text) {
		super(text);
	}

	//
	// @Override
	// public ParentCanvas getView() {
	// // its not using any where
	// return null;
	// }

	@Override
	public void run() {
		Accounter.showError(Accounter.constants().notyetimplemented());

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "CreateStatement";
	}

	@Override
	public String getHelpToken() {
		return "create-statement";
	}

}
