package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;

public class NewCompanyAction extends Action {

	public NewCompanyAction(String text) {
		super(text);
	}

	public NewCompanyAction(String text, String icon) {
		super(text, icon);
	}

	// @Override
	// public ParentCanvas getView() {
	// // NOTHING TO DO.
	// return null;
	// }

	@Override
	public void run(Object data, Boolean isDependent) {
		if (Accounter.getUser() == null) {
			// SC.say(getText());
		} else {
			CompanySetupDialog comDialog = new CompanySetupDialog(null);
			comDialog.show();
		}
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	public ImageResource getSmallImage() {
		// NOTHING TO DO
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "newCompany";
	}

}
