/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.Accounter;

/**
 * @author Prasanna Kumar G
 * 
 */
public class CompanyNameOption extends AbstractPreferenceOption {

	private static CompanyNameOptionUiBinder uiBinder = GWT
			.create(CompanyNameOptionUiBinder.class);
	@UiField
	Label companyNameLabel;
	@UiField
	TextBox companyNameTextBox;
	@UiField
	TextBox legalNameTextBox;
	@UiField
	Label legalNameLabel;
	@UiField
	CheckBox isShowLegalName;
	@UiField
	HorizontalPanel legalNamePanel;

	interface CompanyNameOptionUiBinder extends
			UiBinder<Widget, CompanyNameOption> {
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
	public CompanyNameOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	public void createControls() {
		legalNameLabel.setText(Accounter.constants().legalName());
		companyNameLabel.setText(Accounter.constants().companyName());
		isShowLegalName.setText(constants.registeredAddressComment());
	}

	public void initData() {
		isShowLegalName.setValue(getCompanyPreferences().isShowLegalName());
		legalNameTextBox.setText(getCompanyPreferences().getLegalName());
		companyNameTextBox.setText(getCompany().getDisplayName());
		legalNamePanel.setVisible(getCompanyPreferences().isShowLegalName());
	}

	@Override
	public void onSave() {
		getCompany().setLegalName(legalNameTextBox.getValue());
		getCompany().setTradingName(companyNameTextBox.getValue());
		getCompanyPreferences().setShowLegalName(isShowLegalName.getValue());
	}

	@Override
	public String getTitle() {
		return Accounter.constants().name();
	}

	@Override
	public String getAnchor() {
		return Accounter.constants().name();
	}

	@UiHandler("isShowLegalName")
	void onIsShowLegalNameValueChange(ValueChangeEvent<Boolean> event) {
		legalNamePanel.setVisible(event.getValue());
	}
}
