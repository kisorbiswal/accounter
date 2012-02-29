package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.ui.Accounter;

public class ExpensesByCustomerOption extends AbstractPreferenceOption {

	private static ExpensesByCustomerOptionUiBinder uiBinder = GWT
			.create(ExpensesByCustomerOptionUiBinder.class);
	@UiField
	CheckBox expenseandProductandServiceTrackingbyCustomer;
	@UiField
	CheckBox UseBillableExpenseCheckBox;
	@UiField
	CheckBox PurchaseOrderCheckBox;

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

		return messages.ExpensesbyCustomer(Global.get().Customer());
	}

	@Override
	public void onSave() {
		getCompanyPreferences().setProductandSerivesTrackingByCustomerEnabled(
				expenseandProductandServiceTrackingbyCustomer.getValue());
		getCompanyPreferences().setBillableExpsesEnbldForProductandServices(
				UseBillableExpenseCheckBox.getValue());
		getCompanyPreferences().setPurchaseOrderEnabled(
				PurchaseOrderCheckBox.getValue());
	}

	@Override
	public String getAnchor() {
		return messages.ExpensesbyCustomer(Global.get().Customer());
	}

	@Override
	public void createControls() {
		expenseandProductandServiceTrackingbyCustomer.setText(messages
				.Expenseandproductservicetrackingbycustomer(Global.get()
						.customer()));
		expenseandProductandServiceTrackingbyCustomer.setStyleName("bold");
		UseBillableExpenseCheckBox.setText(messages.useBillabelExpenses());
		UseBillableExpenseCheckBox.setStyleName("bold");
		PurchaseOrderCheckBox.setText(messages.enablePreference(messages
				.purchaseOrder()));
		PurchaseOrderCheckBox.setStyleName("bold");
	}

	@Override
	public void initData() {
		expenseandProductandServiceTrackingbyCustomer
				.setValue(getCompanyPreferences()
						.isProductandSerivesTrackingByCustomerEnabled());
		UseBillableExpenseCheckBox.setValue(getCompanyPreferences()
				.isBillableExpsesEnbldForProductandServices());
		PurchaseOrderCheckBox.setValue(getCompanyPreferences()
				.isPurchaseOrderEnabled());

	}

}
