package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.ui.Accounter;

public class SetupWizard extends VerticalPanel {
	private ClientCompanyPreferences preferences;
	public static AbstractSetupPage setupPage;
	private VerticalPanel mainPanel;
	private HorizontalPanel buttonPanel;
	private Button skipButton, backButton, nextButton;

	public SetupWizard() {
		preferences = Accounter.getCompany().getPreferences();
		 setupPage = new SetupStartPage();
		createControls();
	}

	private void createControls() {
		mainPanel = new VerticalPanel();
		buttonPanel = new HorizontalPanel();
		skipButton = new Button(Accounter.constants().skip());
		backButton = new Button(Accounter.constants().back());
		nextButton = new Button(Accounter.constants().next());

		buttonPanel.add(skipButton);
		buttonPanel.add(backButton);
		buttonPanel.add(nextButton);

		mainPanel.add(setupPage);
		mainPanel.add(buttonPanel);

		add(buttonPanel);
	}

}
