package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
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

		referCustomerLabel = new Label("How to you refer your Customers ?");
		customerCombo = createCusomercombo();
		customerForm = UIUtils.form(accounterConstants.customer());
		customerForm.setNumCols(3);
		customerForm.setWidth("100%");
		customerForm.setFields(customerCombo);

		refersuppliersLabel = new Label("How to you refer your Suppliers ?");
		supplierCombo = createSupplierCombo();
		supplierForm = UIUtils.form(accounterConstants.customer());
		supplierForm.setNumCols(3);
		supplierForm.setWidth("100%");
		supplierForm.setFields(supplierCombo);

		referaccountsLabel = new Label("How to you refer your Accounts ?");
		accountCombo = createAccountCombo();
		accountForm = UIUtils.form(accounterConstants.customer());
		accountForm.setNumCols(3);
		accountForm.setWidth("100%");
		accountForm.setFields(accountCombo);

		mainPanel.add(referCustomerLabel);
		mainPanel.add(customerForm);
		mainPanel.add(refersuppliersLabel);
		mainPanel.add(supplierForm);
		mainPanel.add(referaccountsLabel);
		mainPanel.add(accountForm);

		return mainPanel;

	}

	private SelectCombo createAccountCombo() {
		accountCombo = new SelectCombo(Accounter.constants().account());
		accountCombo.setHelpInformation(true);
		accountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {
					@Override
					public void selectedComboBoxItem(String selectItem) {
						// TODO
					}
				});
		accountCombo.setRequired(true);
		return accountCombo;
	}

	private SelectCombo createSupplierCombo() {
		supplierCombo = new SelectCombo(Accounter.constants().supplier());
		supplierCombo.setHelpInformation(true);
		supplierCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {
					@Override
					public void selectedComboBoxItem(String selectItem) {
						// TODO
					}
				});
		supplierCombo.setRequired(true);
		return supplierCombo;
	}

	private SelectCombo createCusomercombo() {
		customerCombo = new SelectCombo(Accounter.constants().customer());
		customerCombo.setHelpInformation(true);
		customerCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {
					@Override
					public void selectedComboBoxItem(String selectItem) {
						// TODO
					}
				});
		customerCombo.setRequired(true);
		return customerCombo;
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean doShow() {
		// TODO Auto-generated method stub
		return false;
	}

}
