package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class PreferencesAction extends Action {
	private CompanyPreferencesView view;

	public PreferencesAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.getCompanyMessages().company();
	}

	@Override
	public void run(Object data, Boolean isDependent) {

		try {
			view = new CompanyPreferencesView();
			MainFinanceWindow.getViewManager().showView(view, null, true,
					PreferencesAction.this);
		} catch (Exception e) {
			Accounter.showError(Accounter.getCompanyMessages()
					.failedToLoadCompanyPreferences());
		}

	}

	@Override
	public ParentCanvas<?> getView() {
		return this.view;
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().preferences();
	}
	@Override
	public String getImageUrl() {
		// TODO Auto-generated method stub
		return "/images/preferences.png";
	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return "companyPreferences";
	}

}
