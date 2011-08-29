package com.vimukti.accounter.web.client.ui.companypreferences.panels;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;

public class DoYouUseAndHowDoYouReferPanel extends AbstractCompanyInfoPanel {
	private CheckboxItem useCustomerNo, useVendorNo, useAccountNo;
	private SelectCombo customerCombo, supplierCombo, accountCombo;
	private ClientCompanyPreferences companyPreferences;
	private List<String> customerList, supplierList, accountList;

	public DoYouUseAndHowDoYouReferPanel() {
		super();
		createControls();
	}

	private void createControls() {

		VerticalPanel mainPanel = new VerticalPanel();
		AccounterConstants constants = Accounter.constants();
		AccounterMessages messages = Accounter.messages();

		DynamicForm doYouUseForm = new DynamicForm();
		DynamicForm howDoYouForm = new DynamicForm();

		LabelItem doYouLabelItem = new LabelItem();
		doYouLabelItem.setValue(constants.doYouUse());

		useCustomerNo = new CheckboxItem(messages.useCustomerId(Global.get()
				.customer()));
		useVendorNo = new CheckboxItem(messages.useVendorId(Global.get()
				.vendor()));
		useAccountNo = new CheckboxItem(messages.useAccountNos(Global.get()
				.account()));

		LabelItem howDoYouLabelItem = new LabelItem();
		howDoYouLabelItem.setValue(constants.howDoYouRefer());

		customerCombo = new SelectCombo(constants.Customer());
		supplierCombo = new SelectCombo(constants.Supplier());
		accountCombo = new SelectCombo(constants.Account());
		String customer[] = new String[] { constants.Customer(),
				constants.Client(), constants.Tenant(), constants.Donar(),
				constants.Guest(), constants.Member(), constants.patitent() };
		String supplier[] = new String[] { constants.Vendor(),
				constants.Supplier() };
		String account[] = new String[] { constants.Account(),
				constants.Ledger() };
		customerList = new ArrayList<String>();
		supplierList = new ArrayList<String>();
		accountList = new ArrayList<String>();
		for (int i = 0; i < account.length; i++) {
			accountList.add(account[i]);
			accountCombo.addItem(account[i]);
			supplierList.add(supplier[i]);
			supplierCombo.addItem(supplier[i]);
		}

		for (int i = 0; i < customer.length; i++) {
			customerList.add(customer[i]);
			customerCombo.addItem(customer[i]);
		}

		doYouUseForm.setFields(doYouLabelItem, useCustomerNo, useVendorNo,
				useAccountNo);
		howDoYouForm.setFields(howDoYouLabelItem, customerCombo, supplierCombo,
				accountCombo);

		mainPanel.add(doYouUseForm);
		mainPanel.add(howDoYouForm);

		add(mainPanel);
	}

	@Override
	public void onLoad() {
		try {
			useCustomerNo.setValue(companyPreferences.getUseCustomerId());
			useVendorNo.setValue(companyPreferences.getUseVendorId());
			useAccountNo.setValue(companyPreferences.getUseAccountNumbers());
		} catch (Exception e) {
			// TODO: handle exception
		}
		customerCombo.setSelected(customerList.get(companyPreferences
				.getReferCustomers()));
		customerCombo.setSelected(supplierList.get(companyPreferences
				.getReferVendors()));
		accountCombo.setSelected(accountList.get(companyPreferences
				.getReferAccounts()));
	}

	@Override
	public void onSave() {
		companyPreferences.setUseCustomerId(useCustomerNo.getValue());
		companyPreferences.setUseVendorId(useVendorNo.getValue());
		companyPreferences.setUseAccountNumbers(useAccountNo.getValue());

		companyPreferences.setReferCustomers(customerList.indexOf(customerCombo
				.getSelectedValue()));
		companyPreferences.setReferVendors(supplierList.indexOf(customerCombo
				.getSelectedValue()));
		companyPreferences.setReferAccounts(accountList.indexOf(accountCombo
				.getSelectedValue()));
	}

}
