/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author vimukti36
 * 
 */
public class EmailAlertsOption extends AbstractPreferenceOption {

	private static EmailAlertsOptionUiBinder uiBinder = GWT
			.create(EmailAlertsOptionUiBinder.class);
	@UiField
	CheckBox emailAlertCheckBox;

	interface EmailAlertsOptionUiBinder extends
			UiBinder<Widget, EmailAlertsOption> {
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
	public EmailAlertsOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	public void createControls() {
		emailAlertCheckBox.setText(constants.emailAlerts());
		emailAlertCheckBox.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			}
		});

	}

	public EmailAlertsOption(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

	}

	@Override
	public String getTitle() {
		return "Email Alerts";
	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public String getAnchor() {
		return constants.company();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

}
