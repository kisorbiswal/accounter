package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.uibinder.companypreferences.CompanyInfoPage;

public class PreferencesAction extends Action {
	private CompanyInfoPage page;
	public static int CATEGORY;
	public static final int COMPANY = 1;
	public static final int SETTINGS = 2;

	@SuppressWarnings("unchecked")
	public PreferencesAction(String text) {
		super(text);
		if (CATEGORY == COMPANY)
			this.catagory = Accounter.constants().company();
		else if (CATEGORY == SETTINGS)
			this.catagory = Accounter.constants().settings();
	}

	@Override
	public void run() {

		try {
			page = new CompanyInfoPage();
			MainFinanceWindow.getViewManager().showView(page, null, true,
					PreferencesAction.this);
		} catch (Exception e) {
			Accounter.showError(Accounter.constants()
					.failedToLoadCompanyPreferences());
		}

	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().preferences();
	}

	@Override
	public String getHistoryToken() {
		return "companyPreferences";
	}

	@Override
	public String getHelpToken() {
		return "company-preferences";
	}

}
