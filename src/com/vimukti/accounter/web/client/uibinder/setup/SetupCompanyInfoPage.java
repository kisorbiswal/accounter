/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CoreUtils;

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
	@UiField
	Label timezone;
	@UiField
	ListBox timezoneslistbox;
	private ClientCompanyPreferences preferences = Accounter.getCompany()
			.getPreferences();
	private ClientAddress address;
	private List<String> countries, states, timezones;

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
	protected void createControls() {
		headerLabel.setText(accounterConstants.enterYourCompanyInfo());

		// if (Accounter.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_US) {
		taxIDLabel.setText(accounterConstants.taxId());
		// } else if (Accounter.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_UK) {
		// taxIDLabel.setText(accounterConstants.vatNo());
		// } else if (Accounter.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_INDIA) {
		// taxIDLabel.setText(Accounter.messages().panNumber(
		// Global.get().Account()));
		// }

		displayNameLabel.setText(accounterConstants.companyName());
		legalNameLabel.setText(accounterConstants.legalName());
		streetAddress2Label.setText(accounterConstants.streetAddress2());
		streetAdreess1Label.setText(accounterConstants.streetAddress1());
		cityLabel.setText(accounterConstants.city());
		stateLabel.setText(accounterConstants.state());
		zipLabel.setText(accounterConstants.zipCode());
		countryLabel.setText(accounterConstants.country());
		phoneLabel.setText(accounterConstants.phone());
		phone.setTitle(Accounter.messages().phoneNumber(
				Accounter.constants().company()));
		faxLabel.setText(accounterConstants.fax());
		emailAdressLabel.setText(accounterConstants.emailId());
		webSiteLabel.setText(accounterConstants.webSite());
		useFormat.setHTML("");
		timezone.setText(accounterConstants.timezone());

		countries = CoreUtils.getCountriesAsList();
		for (int i = 0; i < countries.size(); i++) {
			country.addItem(countries.get(i));
		}

		country.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (stateListBox.getItemCount() != 0) {
					for (int i = 0; i < stateListBox.getItemCount(); i++) {
						stateListBox.removeItem(i);
					}
				}
				countryChanged();
			}
		});
		countryChanged();

		this.timezones = CoreUtils.getTimeZonesAsList();
		for (String tz : timezones) {
			timezoneslistbox.addItem(tz);
		}

		String defalutTzOffset = getDefaultTzOffsetStr();
		for (String tz : timezones) {
			if (tz.startsWith(defalutTzOffset)) {
				timezoneslistbox.setSelectedIndex(timezones.indexOf(tz));
				break;
			}
		}

	}

	/**
	 * 
	 */
	protected void countryChanged() {
		int selectedCountry = country.getSelectedIndex();
		if (selectedCountry < 0) {
			return;
		}
		List<String> states = CoreUtils.getStatesAsListForCountry(country
				.getItemText(selectedCountry));
		setStates(states);
	}

	private String getDefaultTzOffsetStr() {
		Date date = new Date();
		DateTimeFormat tzFormat = DateTimeFormat.getFormat("z");
		return tzFormat.format(date);
	}

	private void setStates(List<String> states) {
		this.states = states;
		stateListBox.clear();
		for (int i = 0; i < states.size(); i++) {
			stateListBox.addItem(states.get(i));
		}
	}

	public void onLoad() {

		if (this.preferences != null) {
			companyName.setValue(preferences.getFullName());
			legalName.setValue(preferences.getLegalName());
			this.taxId.setValue(preferences.getTaxId());
			this.fax.setValue(preferences.getFax());
			this.phone.setValue(preferences.getPhone());
			this.webSite.setValue(preferences.getWebSite());
			this.emailAddress.setValue(preferences.getCompanyEmail());
			address = preferences.getTradingAddress();
			if (address != null) {
				this.streetAddress1.setValue(address.getAddress1());
				this.streetAdress2.setValue(address.getStreet());
				this.cityTextBox.setValue(address.getCity());
				if (address.getStateOrProvinence() != ""
						&& address.getStateOrProvinence() != null
						&& address.getStateOrProvinence().length() != 0) {
					this.stateListBox.setSelectedIndex(states.indexOf(address
							.getStateOrProvinence()));
				}
				if (address.getCountryOrRegion() != ""
						&& address.getCountryOrRegion() != null
						&& address.getStateOrProvinence().length() != 0) {
					this.country.setSelectedIndex(countries.indexOf(address
							.getCountryOrRegion()));
				}
				if (preferences.getTimezone() != ""
						&& preferences.getTimezone() != null) {
					this.timezoneslistbox.setSelectedIndex(timezones
							.indexOf(preferences.getTimezone()));
				}
			}
		}
	}

	@Override
	public void onSave() {

		address = new ClientAddress();
		preferences.setFullName(companyName.getValue().toString());
		preferences.setLegalName(legalName.getValue().toString());
		preferences.setPhone(phone.getValue().toString());
		preferences.setCompanyEmail(emailAddress.getValue().toString());
		preferences.setTaxId(taxId.getValue().toString());
		preferences.setFax(fax.getValue().toString());
		preferences.setWebSite(webSite.getValue().toString());
		address.setAddress1(streetAddress1.getValue());
		address.setStreet(streetAdress2.getValue());
		address.setCity(cityTextBox.getValue());
		address.setZipOrPostalCode(zip.getValue());
		if (stateListBox.getSelectedIndex() != -1) {
			address.setStateOrProvinence(states.get(stateListBox
					.getSelectedIndex()));
		}
		if (country.getSelectedIndex() != -1)
			address.setCountryOrRegion(countries.get(country.getSelectedIndex()));
		preferences.setTradingAddress(address);

		if (timezoneslistbox.getSelectedIndex() != -1)
			preferences.setTimezone(timezones.get(timezoneslistbox
					.getSelectedIndex()));

	}

	// private String getTimezoneId(String selectedTimezone){
	// return selectedTimezone.substring(selectedTimezone.indexOf(' ')+1);
	// }

	@Override
	public boolean canShow() {
		return true;
	}

	public static boolean checkIfNotNumber(String in) {
		try {
			Integer.parseInt(in);

		} catch (NumberFormatException ex) {
			return true;
		}
		return false;
	}

	@Override
	protected boolean validate() {
		if (companyName.getText().trim() != null
				&& companyName.getText().trim() != ""
				&& companyName.getText().trim().length() != 0) {
			/*
			 * if (taxId.getText().trim() != null && taxId.getText().trim() !=
			 * "" && taxId.getText().trim().length() != 0) { if
			 * (Accounter.getCompany().getAccountingType() ==
			 * ClientCompany.ACCOUNTING_TYPE_US) { if (taxId.getText().length()
			 * == 10) { if (taxId.getText().indexOf(2) == '-' &&
			 * checkIfNotNumber(taxId.getText().substring( 0, 1)) &&
			 * checkIfNotNumber(taxId.getText().substring( 3, 9))) { return
			 * true; } else { Accounter.showError(accounterMessages
			 * .vatIDValidationDesc()); return false; } } else {
			 * Accounter.showError(accounterMessages .vatIDValidationDesc());
			 * return false; } } else if
			 * (Accounter.getCompany().getAccountingType() ==
			 * ClientCompany.ACCOUNTING_TYPE_UK) { if (taxId.getText().length()
			 * == 10) { if (taxId.getText().indexOf(2) == '-' &&
			 * checkIfNotNumber(taxId.getText().substring( 0, 1)) &&
			 * checkIfNotNumber(taxId.getText().substring( 3, 9))) { return
			 * true; } else { Accounter.showError(accounterMessages
			 * .vatIDValidationDesc()); return false; } } else {
			 * Accounter.showError(accounterMessages .vatIDValidationDesc());
			 * return false; } } else if
			 * (Accounter.getCompany().getAccountingType() ==
			 * ClientCompany.ACCOUNTING_TYPE_INDIA) { return true; } else {
			 * return true; } } else { return true; }
			 */
			return true;

		} else {
			return false;
		}

	}

}
