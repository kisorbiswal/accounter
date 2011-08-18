/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.Accounter;

/**
 * @author Administrator
 * 
 */
public class SetupStartPage extends AbstractSetupPage {

	private static SetupStartPageUiBinder uiBinder = GWT
			.create(SetupStartPageUiBinder.class);
	@UiField
	Button startButton, skipButton;
	HTML setupInfo, startSetupInfo, skipSetupInfo;

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
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	VerticalPanel viewPanel;

	@Override
	protected VerticalPanel getViewBody() {
		startButton.setText(accounterConstants.startSetup());
		skipButton.setText(accounterConstants.skipSetup());
		setupInfo.setText(accounterMessages.userGuidelinesMessage());
		startSetupInfo.setText(accounterMessages.startSetupInfo());
		skipSetupInfo.setHTML(accounterMessages.skipSetupInfo());

		return viewPanel;
	}

	@Override
	protected String getViewHeader() {
		return Accounter.constants().welcomeToStartup();
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onSave() {
		// TODO Auto-generated method stub

	}

}
