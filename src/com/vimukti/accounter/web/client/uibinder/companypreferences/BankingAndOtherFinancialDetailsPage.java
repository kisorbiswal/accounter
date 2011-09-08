package com.vimukti.accounter.web.client.uibinder.companypreferences;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.ui.CoreUtils;

public class BankingAndOtherFinancialDetailsPage extends
		AbstractCompanyInfoPanel {

	private static BankingAndOtherFinancialDetailsPageUiBinder uiBinder = GWT
			.create(BankingAndOtherFinancialDetailsPageUiBinder.class);
	@UiField
	Label bankNameTextLabel;
	@UiField
	TextBox bankNameTextBox;
	@UiField
	Label bankAccountNoTextLabel;
	@UiField
	TextBox bankAccountNoTextBox;
	@UiField
	Label sortCodeTextLabel;
	@UiField
	TextBox sortCodeTextBox;
	@UiField
	Label companyRegistrationNoTextLabel;
	@UiField
	TextBox companyRegistrationNoTextBox;
	@UiField
	Label vatRegistartionNoTextLabel;
	@UiField
	TextBox vatRegistartionNoTextBox;
	@UiField
	Label federalTaxIDTextLabel;
	@UiField
	TextBox federalTaxIDTextBox;
	@UiField
	Label primaryCurrencyComboLabel;
	@UiField
	ListBox primaryCurrencyCombo;
	@UiField
	Label bankingLabelItem;
	@UiField
	Label otherFinancialLabelItem;
	private List<ClientCurrency> currenciesList;

	interface BankingAndOtherFinancialDetailsPageUiBinder extends
			UiBinder<Widget, BankingAndOtherFinancialDetailsPage> {
	}

	public BankingAndOtherFinancialDetailsPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	private void initData() {
		bankAccountNoTextBox.setValue(company.getBankAccountNo());
		sortCodeTextBox.setValue(company.getSortCode());
		companyRegistrationNoTextBox.setValue(company.getRegistrationNumber());
		vatRegistartionNoTextBox.setValue(companyPreferences
				.getVATregistrationNumber());
		federalTaxIDTextBox.setValue(company.getTaxId());
		primaryCurrencyCombo.setSelectedIndex(currenciesList
				.indexOf(companyPreferences.getPrimaryCurrency()));
	}

	private void createControls() {
		bankNameTextLabel.setText(constants.bankName());
		bankAccountNoTextLabel.setText(messages.bankAccountNo(Global.get()
				.Account()));
		sortCodeTextLabel.setText(constants.sortCode());

		companyRegistrationNoTextLabel.setText(constants
				.companyRegistrationNumber());
		vatRegistartionNoTextLabel.setText(constants.vatRegistrationNumber());
		federalTaxIDTextLabel.setText(constants.federalTaxId());
		primaryCurrencyComboLabel.setText(constants.primaryCurrency());

		bankingLabelItem.setText(constants.bankingDetails());
		otherFinancialLabelItem.setText(constants.otherDetails());

		currenciesList = CoreUtils.getCurrencies();
		for (ClientCurrency currency : currenciesList) {
			primaryCurrencyCombo.addItem(currency.getFormalName() + "\t"
					+ currency.getDisplayName());
		}
	}


	@Override
	public void onSave() {
		company.setBankAccountNo(bankAccountNoTextBox.getValue());
		company.setSortCode(sortCodeTextBox.getValue());
		company.setRegistrationNumber(companyRegistrationNoTextBox.getValue());
		companyPreferences.setVATregistrationNumber(vatRegistartionNoTextBox
				.getValue());
		company.setTaxId(federalTaxIDTextBox.getValue());
		if (primaryCurrencyCombo.getSelectedIndex() > 0)
		companyPreferences.setPrimaryCurrency(currenciesList
				.get(primaryCurrencyCombo.getSelectedIndex()));
	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub

	}

}
