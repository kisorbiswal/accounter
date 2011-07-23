package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.HrEmployee;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.forms.SuggestionItem;

public class EmployeeExpenseView extends CashPurchaseView {

	private SuggestionItem employee;
	private List<String> hrEmployees = new ArrayList<String>();
	public int status;

	public EmployeeExpenseView() {
		super(ClientTransaction.TYPE_EMPLOYEE_EXPENSE);
		this.validationCount = 6;
	}

	@Override
	protected ClientCashPurchase prepareObject() {
		ClientCashPurchase cashPurchase = transactionObject != null ? (ClientCashPurchase) transactionObject
				: new ClientCashPurchase();

		if (status == ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_APPROVED)
			cashPurchase.setExpenseStatus(status);
		else
			cashPurchase
					.setExpenseStatus(ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_SAVE);

		// Setting Type
		cashPurchase.setType(ClientTransaction.TYPE_EMPLOYEE_EXPENSE);

		cashPurchase.setEmployee(employee.getValue());

		// Setting Contact
		if (contact != null)
			cashPurchase.setContact(this.contact);

		// Setting Address
		if (billingAddress != null)
			cashPurchase.setVendorAddress((billingAddress));

		// Setting Phone
		if (phoneNo != null)
			cashPurchase.setPhone(phoneNo);

		// Setting Payment Methods
		cashPurchase.setPaymentMethod(paymentMethod);

		// Setting Pay From Account
		if (Accounter.getUser().canApproveExpences())
			cashPurchase.setPayFrom(payFromAccount.getID());

		// Setting Check number
		cashPurchase.setCheckNumber(checkNo.getValue().toString());
		// cashPurchase
		// .setCheckNumber(getCheckNoValue() ==
		// ClientWriteCheck.IS_TO_BE_PRINTED ? "0"
		// : getCheckNoValue() + "");

		// Setting Delivery date
		if (deliveryDateItem.getEnteredDate() != null)
			cashPurchase.setDeliveryDate(deliveryDateItem.getEnteredDate()
					.getTime());

		// Setting Total
		cashPurchase.setTotal(vendorTransactionGrid.getTotal());

		// Setting Memo
		cashPurchase.setMemo(getMemoTextAreaItem());
		// Setting Reference
		// cashPurchase.setReference(getRefText());
		return cashPurchase;
	}

	@Override
	protected void initViewType() {

		vendorForm.clear();
		termsForm.clear();

		final MultiWordSuggestOracle employe = new MultiWordSuggestOracle();

		titlelabel.setText(Accounter.getVendorsMessages()
				.employeeExpense());
		Accounter.createGETService().getHREmployees(
				new AsyncCallback<List<HrEmployee>>() {

					@Override
					public void onSuccess(List<HrEmployee> result) {
						for (HrEmployee emp : result) {
							employe.add(emp.getEmployeeName());
							hrEmployees.add(emp.getEmployeeName());
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						Accounter
								.showInformation("Error Showing  Employees List");
					}
				});

		employee = new SuggestionItem(employe, Accounter
				.getVendorsMessages().employEe());
		employee.getMainWidget();
		employee.setHelpInformation(true);
		employee.setRequired(true);
		if (!Accounter.getUser().isAdminUser()) {
			employee.setValue(Accounter.getUser().getName());
			employee.setDisabledForSuggBox(true);
		}

		String listString[] = new String[] {
				Accounter.getVendorsMessages().cash(),
				UIUtils.getpaymentMethodCheckBy_CompanyType(Accounter
						.getCustomersMessages().check()),
				Accounter.getVendorsMessages().creditCard(),
				Accounter.getVendorsMessages().directDebit(),
				Accounter.getVendorsMessages().masterCard(),
				Accounter.getVendorsMessages().onlineBanking(),
				Accounter.getVendorsMessages().standingOrder(),
				Accounter.getVendorsMessages().switchMaestro() };
		selectedComboList = new ArrayList<String>();
		for (int i = 0; i < listString.length; i++) {
			selectedComboList.add(listString[i]);
		}

		if (!(Accounter.getUser().canApproveExpences())) {
			termsForm.setVisible(false);
		}
		paymentMethodCombo.initCombo(selectedComboList);

		vendorForm.setFields(employee);
		termsForm.setFields(paymentMethodCombo, payFromCombo, checkNo);
		termsForm.getCellFormatter().setWidth(0, 0, "203px");

		VerticalPanel verticalPanel = (VerticalPanel) vendorForm.getParent();
		vendorForm.removeFromParent();
		vendorForm.setWidth("100%");
		verticalPanel.add(vendorForm);

		VerticalPanel vPanel = (VerticalPanel) termsForm.getParent();
		termsForm.removeFromParent();
		termsForm.setWidth("100%");
		vPanel.add(termsForm);

		if (transactionObject != null) {
			ClientCashPurchase cashPurchase = (ClientCashPurchase) transactionObject;
			employee.setValue(cashPurchase.getEmployee());
			if (Accounter.getUser().isAdmin()) {
				employee.setDisabledForSuggBox(true);
			}
			deliveryDateItem.setValue(new ClientFinanceDate(cashPurchase
					.getDeliveryDate()));

			if (cashPurchase.getExpenseStatus() == ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_APPROVED) {
				if (Accounter.getUser().canApproveExpences())
					approveButton.setEnabled(false);
				else
					submitForApprove.setEnabled(false);
			}

		}

	}

	@Override
	public boolean validate() throws InvalidEntryException,
			InvalidTransactionEntryException {

		switch (this.validationCount) {
		case 6:
			if (Accounter.getUser().isAdmin()) {
				if (!hrEmployees.contains(employee.getValue()))
					throw new InvalidTransactionEntryException(
							"Please Select An Employee.The Employee must be in  Accounter HR.");
				if (!vendorForm.validate(false))
					// throw new InvalidTransactionEntryException(
					// AccounterErrorType.REQUIRED_FIELDS);
					return true;
			}
		case 5:
			return AccounterValidator.validateTransactionDate(transactionDate);
		case 4:
			if (Accounter.getUser().canApproveExpences())
				return AccounterValidator.validateFormItem(payFromCombo, false);
			else
				return true;
		case 3:
			return AccounterValidator.validate_dueOrDelivaryDates(
					deliveryDateItem.getEnteredDate(), this.transactionDate,
					Accounter.getVendorsMessages().deliverydate());
		case 2:
			return AccounterValidator.isBlankTransaction(vendorTransactionGrid);
		case 1:
			return vendorTransactionGrid.validateGrid();
		default:
			return true;
		}
	}

	@Override
	public void onEdit() {
		AsyncCallback<Boolean> editCallBack = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter
							.showMessage("Your session expired, Please login again to continue");
				} else {
					Accounter.showError(((InvalidOperationException) (caught))
							.getDetailedMessage());
				}
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					ClientCashPurchase purchase = (ClientCashPurchase) transactionObject;
					if (purchase.getExpenseStatus() == ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_APPROVED) {
						Accounter.showError("Expense is Approved");
					} else
						enableFormItems();
				}
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transactionObject
				.getType());
		this.rpcDoSerivce.canEdit(type, transactionObject.id,
				editCallBack);
	}

	@Override
	protected void enableFormItems() {
		super.enableFormItems();
		if (Accounter.getUser().isAdmin()) {
			employee.setDisabledForSuggBox(false);
		}
	}

	@Override
	protected void showMenu(AccounterButton button) {
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			setMenuItems(button, Accounter.getVendorsMessages()
					.service());
		else
			setMenuItems(button, Accounter.getVendorsMessages()
					.service());
	}

	@Override
	protected String getViewTitle() {
		return Accounter.getVendorsMessages().employeeExpense();
	}
}
