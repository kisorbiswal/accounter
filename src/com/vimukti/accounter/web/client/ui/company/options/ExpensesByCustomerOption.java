package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;

public class ExpensesByCustomerOption extends AbstractPreferenceOption {

	private static ExpensesByCustomerOptionUiBinder uiBinder = GWT
			.create(ExpensesByCustomerOptionUiBinder.class);
	@UiField
	CheckBox expenseandProductandServiceTrackingbyCustomer;
	@UiField
	CheckBox UseBillableExpenseCheckBox;

	interface ExpensesByCustomerOptionUiBinder extends
			UiBinder<Widget, ExpensesByCustomerOption> {
	}

	public ExpensesByCustomerOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	@Override
	public String getTitle() {

		return Accounter.messages().ExpensesbyCustomer(Global.get().Customer());
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
		return Accounter.messages().ExpensesbyCustomer(Global.get().Customer());
	}

	@Override
	public void createControls() {
		expenseandProductandServiceTrackingbyCustomer.setText(Accounter
				.messages().Expenseandproductservicetrackingbycustomer(
						Global.get().customer()));
		expenseandProductandServiceTrackingbyCustomer.setStyleName("bold");
		UseBillableExpenseCheckBox.setText(Accounter.messages()
				.Usebillableexpensesandproductsservices());
		UseBillableExpenseCheckBox.setStyleName("bold");
		;
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
