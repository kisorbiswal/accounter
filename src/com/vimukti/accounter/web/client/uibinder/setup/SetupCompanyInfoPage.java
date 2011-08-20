/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HTML;

/**
 * @author Administrator
 * 
 */
public class SetupCompanyInfoPage extends AbstractSetupPage {

	private static SetupCompanyInfoPageUiBinder uiBinder = GWT
			.create(SetupCompanyInfoPageUiBinder.class);
	@UiField
	VerticalPanel viewPanel;
	@UiField
	Grid companyInfoField;
	@UiField
	TextBox companyName;
	@UiField
	TextBox legalName;
	@UiField
	TextBox taxId;
	@UiField
	TextBox streetAddress1;
	@UiField
	TextBox streetAdress2;
	@UiField
	ListBox country;
	@UiField
	HorizontalPanel phonePanel;
	@UiField
	TextBox phone;
	@UiField
	TextBox fax;
	@UiField
	TextBox emailAddress;
	@UiField
	TextBox webSite;
	@UiField
	TextBox zip;
	@UiField
	ListBox stateListBox;
	@UiField
	TextBox cityTextBox;
	@UiField
	Label displayNameLabel;
	@UiField
	Label legalNameLabel;
	@UiField
	Label taxIDLabel;
	@UiField
	Label streetAddress2Label;
	@UiField
	Label streetAdreess1Label;
	@UiField
	Label cityLabel;
	@UiField
	Label stateLabel;
	@UiField
	Label zipLabel;
	@UiField
	Label countryLabel;
	@UiField
	Label phoneLabel;
	@UiField
	Label faxLabel;
	@UiField
	Label emailAdressLabel;
	@UiField
	Label webSiteLabel;
	@UiField
	HTML useFormat;
	@UiField
	Label headerLabel;

	interface SetupCompanyInfoPageUiBinder extends
			UiBinder<Widget, SetupCompanyInfoPage> {
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
	public SetupCompanyInfoPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onSave() {

	}

	@Override
	protected void createControls() {
		headerLabel.setText(accounterConstants.enterYourCompanyInfo());
		
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
			taxIDLabel.setText(accounterConstants.taxId());
		} else if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			taxIDLabel.setText(accounterConstants.vatNo());
		} else if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_INDIA) {
			taxIDLabel.setText(accounterConstants.panNumber());
		}

		displayNameLabel.setText(accounterConstants.displayName());
		legalNameLabel.setText(accounterConstants.legalName());
		streetAddress2Label.setText(accounterConstants.streetAddress2());
		streetAdreess1Label.setText(accounterConstants.streetAddress1());
		cityLabel.setText(accounterConstants.city());
		stateLabel.setText(accounterConstants.state());
		zipLabel.setText(accounterConstants.zipCode());
		countryLabel.setText(accounterConstants.country());
		phoneLabel.setText(accounterConstants.phone());
		faxLabel.setText(accounterConstants.fax());
		emailAdressLabel.setText(accounterConstants.emailId());
		webSiteLabel.setText(accounterConstants.webSite());
		useFormat.setHTML("");
	}

	@Override
	public boolean doShow() {
		return true;
	}

}
