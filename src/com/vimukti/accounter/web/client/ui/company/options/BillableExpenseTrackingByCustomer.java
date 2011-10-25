package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;

public class BillableExpenseTrackingByCustomer extends AbstractPreferenceOption {

	private static BillableExpenseTrackingByCustomerUiBinder uiBinder = GWT
			.create(BillableExpenseTrackingByCustomerUiBinder.class);
	@UiField
	CheckBox expenseandProductandServiceTrackingbyCustomer;
	@UiField
	CheckBox UseBillableExpenseCheckBox;

	interface BillableExpenseTrackingByCustomerUiBinder extends
			UiBinder<Widget, BillableExpenseTrackingByCustomer> {
	}

	public BillableExpenseTrackingByCustomer() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	@Override
	public String getTitle() {

		return "Expenses by Customer";
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
		return "Expenses by Customer";
	}

	@Override
	public void createControls() {
		expenseandProductandServiceTrackingbyCustomer
				.setText("Expense and product/service tracking by customer");
		expenseandProductandServiceTrackingbyCustomer.setStyleName("bold");
		UseBillableExpenseCheckBox
				.setText("Use billable expenses and products/services");
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
