package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.ui.Accounter;

public class SetupWizard extends VerticalPanel {
	private ClientCompanyPreferences preferences;
	public static AbstractSetupPage setupPage;
	private VerticalPanel mainPanel;
	private HorizontalPanel buttonPanel, setupMainPanel;
	private Button skipButton, backButton, nextButton;

	// private SetupProgressPanel progressPanel;

	public SetupWizard() {
		preferences = Accounter.getCompany().getPreferences();
		try {
			setupPage = new SetupStartPage();
		} catch (Exception e) {
			System.err.println(e);
		}
		createControls();
	}

	private void createControls() {
		mainPanel = new VerticalPanel();
		setupMainPanel = new HorizontalPanel();
		// progressPanel = new SetupProgressPanel(setupPage);

		buttonPanel = new HorizontalPanel();
		skipButton = new Button(Accounter.constants().skip());
		backButton = new Button(Accounter.constants().back());
		nextButton = new Button(Accounter.constants().next());

		buttonPanel.add(skipButton);
		buttonPanel.add(backButton);
		buttonPanel.add(nextButton);
		try {
			mainPanel.add(setupPage);
		} catch (Exception e) {
			System.err.println(e);
		}
		if ((setupPage instanceof SetupStartPage)
				|| (setupPage instanceof SetupComplitionView)) {

		} else {
			mainPanel.add(buttonPanel);
		}

		setupMainPanel.add(mainPanel);
		// setupMainPanel.add(progressPanel);
		setupMainPanel.setSize("100%", "100%");
		add(setupMainPanel);
		setSize("100%", "100%");
		skipButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				SetupSkipDialog skipDialog = new SetupSkipDialog();
				skipDialog.show();
				skipDialog.center();
			}
		});

		nextButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		backButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

}
