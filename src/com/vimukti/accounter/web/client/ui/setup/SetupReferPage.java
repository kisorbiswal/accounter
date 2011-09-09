package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class SetupReferPage extends AbstractSetupPage {
	private Label referCustomerLabel, referaccountsLabel, refersuppliersLabel;
	private SelectCombo customerCombo, supplierCombo, accountCombo;
	private DynamicForm customerForm, supplierForm, accountForm;

	public SetupReferPage() {
		super();
	}

	@Override
	public String getHeader() {
		return this.accounterConstants.terminology();
	}

	@Override
	public VerticalPanel getPageBody() {

		VerticalPanel mainPanel = new VerticalPanel();

		referCustomerLabel = new Label(Accounter.messages()
				.howDoYouReferYourCustoemrs());
		referCustomerLabel.addStyleName("referComment");
		customerCombo = new SelectCombo(Global.get().customer());
		customerForm = UIUtils.form(Global.get().customer());
		customerCombo.addItem("Customers");
		customerCombo.addItem("Clients");
		customerCombo.addItem("Tenants");
		customerForm.setWidth("100%");
		customerForm.setFields(customerCombo);

		refersuppliersLabel = new Label(Accounter.messages()
				.howDoYouReferYourVendors());
		refersuppliersLabel.addStyleName("referComment");
		supplierCombo = new SelectCombo(Global.get().customer());
		supplierCombo.addItem("Suppliers");
		supplierCombo.addItem("Vendors");
		supplierCombo.setHelpInformation(true);
		supplierForm = UIUtils.form(Global.get().customer());
		supplierForm.setWidth("100%");
		supplierForm.setFields(supplierCombo);

		referaccountsLabel = new Label(Accounter.messages()
				.howDoYouReferYourAccounts());
		referaccountsLabel.addStyleName("referComment");
		accountCombo = new SelectCombo(Accounter.messages().account(
				Global.get().account()));
		accountCombo.addItem("Accounts");
		accountCombo.addItem("Legands");
		accountForm = UIUtils.form(Global.get().customer());
		accountForm.setWidth("100%");
		accountForm.setFields(accountCombo);

		customerCombo.textBox.getElement().getParentElement()
				.getParentElement().getParentElement().getParentElement()
				.addClassName("customerReferCombo");
		mainPanel.add(customerForm);
		mainPanel.add(referCustomerLabel);

		supplierCombo.textBox.getElement().getParentElement()
				.getParentElement().getParentElement().getParentElement()
				.addClassName("supplierReferCombo");
		mainPanel.add(supplierForm);
		mainPanel.add(refersuppliersLabel);

		accountCombo.textBox.getElement().getParentElement().getParentElement()
				.getParentElement().getParentElement()
				.addClassName("accountReferCombo");
		mainPanel.add(accountForm);
		mainPanel.add(referaccountsLabel);

		mainPanel.addStyleName("setuppage_body");
		return mainPanel;

	}

	@Override
	public void onLoad() {

		// String referCustomers = preferences.getReferCustomers();
		// String referSuplliers = preferences.getReferSuplliers();
		// String referAccounts = preferences.getReferAccounts();

		// if (referCustomers != null)
		// customerCombo.setSelected(referCustomers);
		// if (referAccounts != null)
		// accountCombo.setSelected(referAccounts);
		// if (referSuplliers != null)
		// supplierCombo.setSelected(referSuplliers);
	}

	@Override
	public void onSave() {
		String customer = customerCombo.getSelectedValue();
		String suplier = supplierCombo.getSelectedValue();
		String accounts = accountCombo.getSelectedValue();
		// if (customer != null)
		// preferences.setReferCustomers(customer);
		// if (suplier != null)
		// preferences.setReferSuplliers(suplier);
		// if (accounts != null)
		// preferences.setReferAccounts(accounts);
	}

	@Override
	public boolean doShow() {
		return true;
	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

}
