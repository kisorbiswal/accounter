package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;

public class SetupStartPage extends AbstractSetupPage {
	private VerticalPanel mainVerticalPanel, question1Panel, question2Panel;
	private HTML description, clickSkipDesc, clickStartDesc;
	private HorizontalPanel hpanel;
	private Label desclable, question1Lable, ClickSkipLabel, question2Lable,
			ClickSetupLabel;
	private Button skipButton, StartButton;
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
		description = new HTML(this.accounterConstants.userGuidelinesMessage());
		mainVerticalPanel.add(description);
		question1Panel = new VerticalPanel();
		question1Lable = new Label(this.accounterConstants.expertInAccounter());
		question1Panel.add(question1Lable);
		clickSkipDesc = new HTML(this.accounterConstants.clickSkipButtonMsg());
		question1Panel.add(clickSkipDesc);
		skipButton = new Button(this.accounterConstants.skipSetup());
		question1Panel.add(skipButton);
		hpanel = new HorizontalPanel();
		hpanel.add(question1Panel);

		question2Panel = new VerticalPanel();
		question1Lable = new Label(this.accounterConstants
				.areYouaNewAccounter());
		question2Panel.add(question1Lable);
		clickStartDesc = new HTML(this.accounterConstants.clickStartButtonMsg());
		question2Panel.add(clickStartDesc);
		StartButton = new Button(this.accounterConstants.startSetup());
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
				setupWizard.setView(new SetupCompanyInfoPage());
				setupWizard.creteControls();
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

	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteFailed(Throwable caught) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub
		
	}

}
