package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class NewCompanyAction extends Action {

	public NewCompanyAction(String text) {
		super(text);
	}

	public NewCompanyAction(String text, String icon) {
		super(text, icon);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		if (FinanceApplication.getUser() == null) {
			// SC.say(getText());
		} else {
			CompanySetupDialog comDialog = new CompanySetupDialog(null);
			comDialog.show();
		}
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return null;
	}

}
