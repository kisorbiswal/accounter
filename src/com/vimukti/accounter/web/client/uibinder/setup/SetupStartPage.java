/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Administrator
 * 
 */
public class SetupStartPage extends AbstractSetupPage {

	private SetupWizard setupWizard;
	private static SetupStartPageUiBinder uiBinder = GWT
			.create(SetupStartPageUiBinder.class);
	@UiField
	HTML startSetupInfo, skipSetupInfo, setupInfo;
	@UiField
	VerticalPanel viewPanel;
	@UiField
	Label headerLabel;
	@UiField
	Button cancelBtn;
	@UiField
	Button skipButton;
	@UiField
	Button startButton;

	interface SetupStartPageUiBinder extends UiBinder<Widget, SetupStartPage> {
	}

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 */
	public SetupStartPage() {

	}

	public SetupStartPage(SetupWizard setupWizard) {
		initWidget(uiBinder.createAndBindUi(this));
		this.setupWizard = setupWizard;
		createControls();
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createControls() {
		this.headerLabel.setText(accounterConstants.welcomeToStartup());
		this.startButton.setText(accounterConstants.startSetup());
		this.skipButton.setText(accounterConstants.skipSetup());
		this.cancelBtn.setText(accounterConstants.cancel());
		this.setupInfo.setHTML(accounterMessages.userGuidelinesMessage());
		this.startSetupInfo.setHTML(accounterMessages.startSetupInfo());
		this.skipSetupInfo.setHTML(accounterMessages.skipSetupInfo());

		startButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setupWizard.initInterview(false);
			}
		});

		this.skipButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setupWizard.initInterview(true);
			}
		});

		cancelBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setupWizard.cancel();
			}
		});

	}

	@Override
	public boolean canShow() {
		return true;
	}

	@Override
	protected boolean validate() {
		return true;
	}

}
