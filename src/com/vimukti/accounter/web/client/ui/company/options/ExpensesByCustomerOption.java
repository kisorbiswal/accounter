package com.vimukti.accounter.web.client.ui.company.options;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;

public class ExpensesByCustomerOption extends AbstractPreferenceOption {

	CheckboxItem expenseandProductandServiceTrackingbyCustomer;

	CheckboxItem UseBillableExpenseCheckBox;

	StyledPanel mainPanel;

	public ExpensesByCustomerOption() {
		super("");
		createControls();
		initData();
	}

	@Override
	public String getTitle() {

		return messages.ExpensesbyCustomer(Global.get().Customer());
	}

	@Override
	public void onSave() {
		getCompanyPreferences().setProductandSerivesTrackingByCustomerEnabled(
				expenseandProductandServiceTrackingbyCustomer.getValue());
		getCompanyPreferences().setBillableExpsesEnbldForProductandServices(
				UseBillableExpenseCheckBox.getValue());
	}

	@Override
	public String getAnchor() {
		return messages.ExpensesbyCustomer(Global.get().Customer());
	}

	@Override
	public void createControls() {
		expenseandProductandServiceTrackingbyCustomer = new CheckboxItem(
				messages.Expenseandproductservicetrackingbycustomer(Global
						.get().customer()),
				"expenseandProductandServiceTrackingbyCustomer");

		UseBillableExpenseCheckBox = new CheckboxItem(
				messages.useBillabelExpenses(), "UseBillableExpenseCheckBox");
		mainPanel = new StyledPanel("expensesByCustomerOption");
		mainPanel.add(expenseandProductandServiceTrackingbyCustomer);
		mainPanel.add(UseBillableExpenseCheckBox);
		add(mainPanel);
	}

	@Override
	public void initData() {
		expenseandProductandServiceTrackingbyCustomer
				.setValue(getCompanyPreferences()
						.isProductandSerivesTrackingByCustomerEnabled());
		UseBillableExpenseCheckBox.setValue(getCompanyPreferences()
				.isBillableExpsesEnbldForProductandServices());
	}

}
