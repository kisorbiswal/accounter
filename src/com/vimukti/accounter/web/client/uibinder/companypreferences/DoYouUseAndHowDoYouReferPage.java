package com.vimukti.accounter.web.client.uibinder.companypreferences;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;

public class DoYouUseAndHowDoYouReferPage extends AbstractCompanyInfoPanel {

	private static DoYouUseAndHowDoYouReferPageUiBinder uiBinder = GWT
			.create(DoYouUseAndHowDoYouReferPageUiBinder.class);
	@UiField
	Label doYouLabelItem;
	@UiField
	Label howDoYouLabelItem;
	@UiField
	CheckBox useCustomerNo;
	@UiField
	CheckBox useVendorNo;
	@UiField
	CheckBox useAccountNo;
	@UiField
	Label customerComboLabel;
	@UiField
	ListBox customerCombo;
	@UiField
	Label supplierComboLabel;
	@UiField
	ListBox supplierCombo;
	@UiField
	Label accountComboLabel;
	@UiField
	ListBox accountCombo;

	interface DoYouUseAndHowDoYouReferPageUiBinder extends
			UiBinder<Widget, DoYouUseAndHowDoYouReferPage> {
	}

	public DoYouUseAndHowDoYouReferPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	private void initData() {
		useCustomerNo.setValue(companyPreferences.getUseCustomerId());
		useVendorNo.setValue(companyPreferences.getUseVendorId());
		useAccountNo.setValue(companyPreferences.getUseAccountNumbers());
		customerCombo.setSelectedIndex(companyPreferences.getReferCustomers());
		supplierCombo.setSelectedIndex(companyPreferences.getReferVendors());
		accountCombo.setSelectedIndex(companyPreferences.getReferAccounts());
	}

	private void createControls() {

		doYouLabelItem.setText(constants.doYouUse());

		useCustomerNo.setText(messages.useCustomerId(Global.get().customer()));
		useVendorNo.setText(messages.useVendorId(Global.get().vendor()));
		useAccountNo.setText(messages.useAccountNos(Global.get().account()));

		howDoYouLabelItem.setText(constants.terminology());

		customerComboLabel.setText(constants.Customer());
		supplierComboLabel.setText(constants.Supplier());
		accountComboLabel.setText(constants.Account());

		String customer[] = new String[] { constants.Customer(),
				constants.Client(), constants.Tenant(), constants.Donar(),
				constants.Guest(), constants.Member(), constants.patitent() };
		String supplier[] = new String[] { constants.Vendor(),
				constants.Supplier() };
		String account[] = new String[] { constants.Account(),
				constants.Ledger() };
		for (int i = 0; i < account.length; i++) {
			accountCombo.addItem(account[i]);
			supplierCombo.addItem(supplier[i]);
		}

		for (int i = 0; i < customer.length; i++) {
			customerCombo.addItem(customer[i]);
		}

	}

	@Override
	public void onSave() {
		companyPreferences.setUseCustomerId(useCustomerNo.getValue());
		companyPreferences.setUseVendorId(useVendorNo.getValue());
		companyPreferences.setUseAccountNumbers(useAccountNo.getValue());

		companyPreferences.setReferCustomers(customerCombo.getSelectedIndex());
		companyPreferences.setReferVendors(customerCombo.getSelectedIndex());
		companyPreferences.setReferAccounts(accountCombo.getSelectedIndex());
	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub

	}

}