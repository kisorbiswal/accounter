package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.user.client.ui.VerticalPanel;

public class SetupWizard extends VerticalPanel {

	private AbstractSetupPage setupPage;

	public SetupWizard() {
		setupPage = new SetupStartPage();
		add(setupPage);
	}

}
