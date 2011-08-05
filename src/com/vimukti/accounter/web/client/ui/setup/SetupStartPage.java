package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SetupStartPage extends AbstractSetupPage {
	VerticalPanel mainVerticalPanel, question1Panel, question2Panel;
	HTML description, clickSkipDesc, clickStartDesc;
	HorizontalPanel hpanel;
	Label desclable, question1Lable, ClickSkipLabel, question2Lable,
			ClickSetupLabel;
	Button skipButton, StartButton;

	public SetupStartPage() {
		// TODO Auto-generated constructor stub
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
		description = new HTML("Description"); // TODO need to get description
		mainVerticalPanel.add(description);
		question1Panel = new VerticalPanel();
		question1Lable = new Label(
				"Are you an Accountant or Expert ni Accounter?");
		question1Panel.add(question1Lable);
		clickSkipDesc = new HTML("Description");
		question1Panel.add(clickSkipDesc);
		skipButton = new Button("Skip Setup");
		question1Panel.add(skipButton);
		hpanel = new HorizontalPanel();
		hpanel.add(question1Panel);

		question2Panel = new VerticalPanel();
		question1Lable = new Label(
				"Are you new to Accounting or new to Accounter?");
		question2Panel.add(question1Lable);
		clickStartDesc = new HTML("Description");
		question2Panel.add(clickStartDesc);
		StartButton = new Button("Start Setup");
		question2Panel.add(StartButton);
		hpanel.add(question2Panel);
		mainVerticalPanel.add(hpanel);

		skipButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {

			}
		});

		StartButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {

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
	protected void onBack() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onNext() {
		// TODO Auto-generated method stub
		
	}


}
