package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;

public class CompanyInfoAction extends Action {

	public CompanyInfoAction(String text) {
		super(text);
		this.catagory = Accounter.constants().company();
	}

	// @Override
	// public ParentCanvas<?> getView() {
	// // its not usiong any where
	// return null;
	// }

	@Override
	public void run() {
		new CompanyInfoDialog(Accounter.constants().companyInformation(), "")
				.show();
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceImages().companyInformation();
	}

	@Override
	public String getHistoryToken() {
		return "CompanyInfo";
	}

	@Override
	public String getHelpToken() {
		// Not using
		return "company-info";
	}
}
