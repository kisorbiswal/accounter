/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;

/**
 * @author vimukti36
 * 
 */
public class CustomerTerminologyOption extends AbstractPreferenceOption {

	private static CustomerTerminologyOptionUiBinder uiBinder = GWT
			.create(CustomerTerminologyOptionUiBinder.class);
	@UiField
	Label terminologyforCustomerLabel;
	@UiField
	RadioButton tenantsLabelRadioButton;
	@UiField
	RadioButton clientsRadioButton;
	@UiField
	RadioButton custimersRadioBuitton;
	@UiField
	RadioButton DonorsRadioButton;
	@UiField
	RadioButton guestardioButton;
	@UiField
	RadioButton membersRadioButton;
	@UiField
	RadioButton PatientRadioButton;

	interface CustomerTerminologyOptionUiBinder extends
			UiBinder<Widget, CustomerTerminologyOption> {
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
	public CustomerTerminologyOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initdata();
	}

	private void createControls() {
		terminologyforCustomerLabel.setText(messages.useTerminologyFor(Global
				.get().Customer()));
		tenantsLabelRadioButton.setName(constants.group());
		tenantsLabelRadioButton.setHTML(constants.tenant());
		custimersRadioBuitton.setName(constants.group());
		custimersRadioBuitton.setHTML(constants.Customer());
		guestardioButton.setName(constants.group());
		guestardioButton.setHTML(constants.Guest());
		membersRadioButton.setName(constants.group());
		membersRadioButton.setHTML(constants.Member());
		PatientRadioButton.setName(constants.group());
		PatientRadioButton.setHTML(constants.Patitent());
		clientsRadioButton.setName(constants.group());
		clientsRadioButton.setHTML(constants.Client());
		DonorsRadioButton.setName(constants.group());
		DonorsRadioButton.setHTML(constants.Donar());

	}

	private void initdata() {
		// TODO Auto-generated method stub
	}

	public CustomerTerminologyOption(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getAnchor() {
		// TODO Auto-generated method stub
		return null;
	}

}
