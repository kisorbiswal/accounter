package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
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
		return this.accounterConstants.howDoYouRefer();
	}

	@Override
	public VerticalPanel getPageBody() {

		VerticalPanel mainPanel = new VerticalPanel();

		referCustomerLabel = new Label(Accounter.constants()
				.howDoYouReferYourCustoemrs());
		referCustomerLabel.addStyleName("referComment");
		customerCombo = new SelectCombo(accounterConstants.customer());
		customerForm = UIUtils.form(accounterConstants.customer());
		customerCombo.addItem(accounterConstants.Customer());
		customerCombo.addItem(accounterConstants.Client());
		customerCombo.addItem(accounterConstants.Tenant());
		customerCombo.addItem(accounterConstants.Donar());
		customerCombo.addItem(accounterConstants.Guest());
		customerCombo.addItem(accounterConstants.Member());
		customerCombo.addItem(accounterConstants.Patitent());
		customerForm.setWidth("100%");
		customerForm.setFields(customerCombo);

		refersuppliersLabel = new Label(Accounter.constants()
				.howDoYouReferYourSuppliers());
		refersuppliersLabel.addStyleName("referComment");
		supplierCombo = new SelectCombo(accounterConstants.supplier());
		supplierCombo.addItem(accounterConstants.Supplier());
		supplierCombo.addItem(accounterConstants.Vendor());
		supplierCombo.setHelpInformation(true);
		supplierForm = UIUtils.form(accounterConstants.customer());
		supplierForm.setWidth("100%");
		supplierForm.setFields(supplierCombo);

		referaccountsLabel = new Label(Accounter.constants()
				.howDoYouReferYourAccounts());
		referaccountsLabel.addStyleName("referComment");
		accountCombo = new SelectCombo(accounterConstants.account());
		accountCombo.addItem(accounterConstants.Account());
		accountCombo.addItem(accounterConstants.Legand());
		accountForm = UIUtils.form(accounterConstants.customer());
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

		int referCustomers = preferences.getReferCustomers();
		int referSuplliers = preferences.getReferSuplliers();
		int referAccounts = preferences.getReferAccounts();
		customerCombo.setSelectedItem(referCustomers);
		accountCombo.setSelectedItem(referAccounts);
		supplierCombo.setSelectedItem(referSuplliers);
	}

	@Override
	public void onSave() {
		// String customer = customerCombo.getSelectedValue();
		// String suplier = supplierCombo.getSelectedValue();
		// String accounts = accountCombo.getSelectedValue();
		// if (customer != null)
		// preferences.setReferCustomers(customer);
		// if (suplier != null)
		// preferences.setReferSuplliers(suplier);
		// if (accounts != null)
		// preferences.setReferAccounts(accounts);
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
