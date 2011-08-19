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
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
			taxId.setText(accounterConstants.taxId());
		} else if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			taxId.setText(accounterConstants.vatNo());
		} else if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_INDIA) {
			taxId.setText(accounterConstants.panNumber());
		}
		companyName.setText(accounterConstants.displayName());
		legalName.setText(accounterConstants.legalName());

		streetAddress1.setText(accounterConstants.streetAddress1());
		streetAdress2.setText(accounterConstants.streetAddress2());
		cityTextBox.setText(accounterConstants.city());
		country.setTitle(accounterConstants.country());
		phone.setText(accounterConstants.phone());
		fax.setText(accounterConstants.fax());
		emailAddress.setText(accounterConstants.emailId());
		webSite.setText(accounterConstants.webSite());
	}

}
