package com.vimukti.accounter.web.client.ui.companypreferences.panels;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.CurrencyCombo;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;

public class BankingAndOtherFinancialDetailsPanel extends VerticalPanel {
	private TextItem bankNameText, bankAccountNoText, sortCodeText,
			companyRegistrationNoText, vatRegistartionNoText, federalTaxIDText;
	private CurrencyCombo primaryCurrencyCombo;

	private DynamicForm bankAndFinanceForm;

	public BankingAndOtherFinancialDetailsPanel(
			ClientCompanyPreferences companyPreferences) {
		createControls();
	}

	private void createControls() {

		bankNameText = new TextItem(Accounter.constants().bankName());
		bankNameText.setHelpInformation(true);
		bankNameText.setRequired(false);
		bankNameText.setWidth(100);

		bankAccountNoText = new TextItem(Accounter.messages().bankAccountNo(
				Global.get().Account()));
		bankAccountNoText.setHelpInformation(true);
		bankAccountNoText.setRequired(false);
		bankAccountNoText.setWidth(100);

		sortCodeText = new TextItem(Accounter.constants().sortCode());
		sortCodeText.setHelpInformation(true);
		sortCodeText.setRequired(false);
		sortCodeText.setWidth(100);

		companyRegistrationNoText = new TextItem(Accounter.constants()
				.companyRegistrationNumber());
		companyRegistrationNoText.setHelpInformation(true);
		companyRegistrationNoText.setRequired(false);
		companyRegistrationNoText.setWidth(100);

		vatRegistartionNoText = new TextItem(Accounter.constants()
				.vatRegistrationNumber());
		vatRegistartionNoText.setHelpInformation(true);
		vatRegistartionNoText.setRequired(false);
		vatRegistartionNoText.setWidth(100);

		federalTaxIDText = new TextItem(Accounter.constants().federalTaxId());
		federalTaxIDText.setHelpInformation(true);
		federalTaxIDText.setRequired(false);
		federalTaxIDText.setWidth(100);

		primaryCurrencyCombo = new CurrencyCombo(Accounter.constants()
				.primaryCurrency());
		primaryCurrencyCombo.setHelpInformation(true);

		bankAndFinanceForm = new DynamicForm();
		bankAndFinanceForm.setWidth("80%");
		bankAndFinanceForm.getCellFormatter().setWidth(0, 0, "225px");
		bankAndFinanceForm.getCellFormatter().addStyleName(1, 0,
				"memoFormAlign");
		bankAndFinanceForm.getCellFormatter().addStyleName(2, 0,
				"memoFormAlign");

		bankAndFinanceForm.setFields(bankNameText, bankAccountNoText,
				sortCodeText, companyRegistrationNoText, vatRegistartionNoText,
				federalTaxIDText, primaryCurrencyCombo);
		this.add(bankAndFinanceForm);

	}

}
