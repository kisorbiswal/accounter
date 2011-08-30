package com.vimukti.accounter.web.client.ui.companypreferences.panels;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.CurrencyCombo;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class BankingAndOtherFinancialDetailsPanel extends
		AbstractCompanyInfoPanel {
	private TextItem bankNameText, bankAccountNoText, sortCodeText,
			companyRegistrationNoText, vatRegistartionNoText, federalTaxIDText;
	private CurrencyCombo primaryCurrencyCombo;

	public BankingAndOtherFinancialDetailsPanel() {
		super();
		createControls();
	}

	private void createControls() {

		VerticalPanel mainPanel = new VerticalPanel();

		DynamicForm bankingForm = new DynamicForm();
		DynamicForm otherFinancialForm = new DynamicForm();

		bankNameText = new TextItem(Accounter.constants().bankName());
		bankNameText.setHelpInformation(true);
		bankNameText.setRequired(false);

		bankAccountNoText = new TextItem(Accounter.messages().bankAccountNo(
				Global.get().Account()));
		bankAccountNoText.setHelpInformation(true);
		bankAccountNoText.setRequired(false);

		sortCodeText = new TextItem(Accounter.constants().sortCode());
		sortCodeText.setHelpInformation(true);
		sortCodeText.setRequired(false);

		companyRegistrationNoText = new TextItem(Accounter.constants()
				.companyRegistrationNumber());
		companyRegistrationNoText.setHelpInformation(true);
		companyRegistrationNoText.setRequired(false);

		vatRegistartionNoText = new TextItem(Accounter.constants()
				.vatRegistrationNumber());
		vatRegistartionNoText.setHelpInformation(true);
		vatRegistartionNoText.setRequired(false);

		federalTaxIDText = new TextItem(Accounter.constants().federalTaxId());
		federalTaxIDText.setHelpInformation(true);
		federalTaxIDText.setRequired(false);

		primaryCurrencyCombo = new CurrencyCombo(Accounter.constants()
				.primaryCurrency());
		primaryCurrencyCombo.setHelpInformation(true);

		VerticalPanel bankingPanel = new VerticalPanel();
		VerticalPanel otherFinancialPanel = new VerticalPanel();

		Label bankingLabelItem = new Label(constants.bankingDetails());
		Label otherFinancialLabelItem = new Label(constants.otherDetails());

		bankingForm.setFields(bankNameText, bankAccountNoText, sortCodeText);
		otherFinancialForm.setFields(companyRegistrationNoText,
				vatRegistartionNoText, federalTaxIDText, primaryCurrencyCombo);

		bankingPanel.addStyleName("companyInfoPanel");
		otherFinancialPanel.addStyleName("companyInfoPanel");

		bankingPanel.add(bankingLabelItem);
		bankingPanel.add(bankingForm);
		bankingForm.getElement().getStyle().setPaddingLeft(100, Unit.PX);
		bankingLabelItem.addStyleName("header");

		otherFinancialPanel.add(otherFinancialLabelItem);
		otherFinancialPanel.add(otherFinancialForm);
		otherFinancialForm.getElement().getStyle().setPaddingLeft(35, Unit.PX);
		otherFinancialLabelItem.addStyleName("header");

		mainPanel.add(bankingPanel);
		mainPanel.add(otherFinancialPanel);

		mainPanel.setSize("100%", "100%");
		mainPanel.setSpacing(8);
		this.add(mainPanel);

	}

	@Override
	public void onLoad() {
		// bankNameText.setValue(company.getb)
		bankAccountNoText.setValue(company.getBankAccountNo());
		sortCodeText.setValue(company.getSortCode());
		companyRegistrationNoText.setValue(company.getRegistrationNumber());
		vatRegistartionNoText.setValue(companyPreferences
				.getVATregistrationNumber());
		federalTaxIDText.setValue(company.getTaxId());
		primaryCurrencyCombo.setComboItem(companyPreferences
				.getPrimaryCurrency());
	}

	@Override
	public void onSave() {
		company.setBankAccountNo(bankAccountNoText.getValue());
		company.setSortCode(sortCodeText.getValue());
		company.setRegistrationNumber(companyRegistrationNoText.getValue());
		companyPreferences.setVATregistrationNumber(vatRegistartionNoText
				.getValue());
		company.setTaxId(federalTaxIDText.getValue());
		companyPreferences.setPrimaryCurrency(primaryCurrencyCombo
				.getSelectedValue());
	}

}
