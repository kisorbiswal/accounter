/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.Accounter;

/**
 * @author vimukti36
 * 
 */
public class CompanyPhoneNumberOption extends AbstractPreferenceOption {

	private static CompanyPhoneNumberOptionUiBinder uiBinder = GWT
			.create(CompanyPhoneNumberOptionUiBinder.class);
	@UiField
	Label companyPhoneNumberLabel;
	@UiField
	TextBox companyPhoneNumberTextBox;

	interface CompanyPhoneNumberOptionUiBinder extends
			UiBinder<Widget, CompanyPhoneNumberOption> {
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
	public CompanyPhoneNumberOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	public void initData() {
		companyPhoneNumberTextBox.setText(getCompany().getPhone());
	}

	public void createControls() {
		companyPhoneNumberLabel.setText(Accounter.constants().phoneNumber());

	}

	@Override
	public String getTitle() {
		return Accounter.constants().phoneNumber();
	}

	@Override
	public void onSave() {
		getCompany().setPhone(companyPhoneNumberTextBox.getValue());
	}

	@Override
	public String getAnchor() {
		return Accounter.constants().phoneNumber();
	}

}
