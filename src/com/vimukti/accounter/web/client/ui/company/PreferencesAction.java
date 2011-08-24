package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class PreferencesAction extends Action {
	private CompanyPreferencesView view;

	public PreferencesAction(String text) {
		super(text);
		this.catagory = Accounter.constants().company();
	}

	@Override
	public void run() {

		try {
			view = new CompanyPreferencesView();
			MainFinanceWindow.getViewManager().showView(view, null, true,
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
