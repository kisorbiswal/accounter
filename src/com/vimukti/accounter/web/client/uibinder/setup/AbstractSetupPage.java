package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.user.client.ui.Composite;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;

public abstract class AbstractSetupPage extends Composite {

	protected static ClientCompanyPreferences preferences;
	protected AccounterConstants accounterConstants = Accounter.constants();
	protected AccounterMessages accounterMessages = Accounter.messages();
	private static String country;

	public AbstractSetupPage(ClientCompanyPreferences preferences) {
		AbstractSetupPage.preferences = preferences;
	}

	public static void setPreferences(ClientCompanyPreferences preferences) {
		AbstractSetupPage.preferences = preferences;
	}

	public AbstractSetupPage() {
		if (preferences == null)
			preferences = new ClientCompanyPreferences();
	}

	protected abstract void createControls();

	protected abstract void onSave();

	protected abstract void onLoad();

	protected abstract boolean validate();

	public abstract boolean canShow();

	protected void setCountry(String country) {
		AbstractSetupPage.country = country;
	}

	public static String getCountry() {
		return country;
	}

}
