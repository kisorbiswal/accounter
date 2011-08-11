package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.Accounter;

public class SetupStartPage extends AbstractSetupPage {
	private VerticalPanel mainVerticalPanel;
	private HTML description;
	private Button startButton;
	private Image titleimage;
	private SetupWizard setupWizard;

	public SetupStartPage() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SetupStartPage(SetupWizard setupWizard) {
		this.setupWizard = setupWizard;
	}

	@Override
	public String getHeader() {
		return this.accounterConstants.welcomeToStartup();
	}

	@Override
	public VerticalPanel getPageBody() {
		creatControls();
		return mainVerticalPanel;
	}

	private void creatControls() {
		mainVerticalPanel = new VerticalPanel();
		description = new HTML(Accounter.messages().userGuidelinesMessage());
		startButton = new Button(accounterConstants.startSetup());

		mainVerticalPanel.add(description);
		mainVerticalPanel.add(startButton);
		startButton.getElement().getParentElement()
				.addClassName("start_button");

		startButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				setupWizard.currentViewIndex++;
				setupWizard.showView();
			}
		});

	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		// not required for this page

	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub
		// not required for this page

	}

	@Override
	public boolean doShow() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean validate() {
		return false;
	}

}
