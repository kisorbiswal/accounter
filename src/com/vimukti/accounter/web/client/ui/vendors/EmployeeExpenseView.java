package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ImageButton;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.EmployeeCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;

public class EmployeeExpenseView extends CashPurchaseView {

	private EmployeeCombo employee;
	// private List<String> hrEmployees = new ArrayList<String>();
	public int status;
	private ImageButton approveButton;
	private ImageButton submitForApprove;

	public EmployeeExpenseView() {
		super(ClientTransaction.TYPE_EMPLOYEE_EXPENSE);
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

		cashPurchase.setEmployee(employee.getSelectedValue());

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
					.getDate());

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

		titlelabel.setText(Accounter.constants().employeeExpense());
		// Accounter.createGETService().getHREmployees(
		// new AccounterAsyncCallback<List<HrEmployee>>() {
		//
		// @Override
		// public void onSuccess(List<HrEmployee> result) {
		// for (HrEmployee emp : result) {
		// employe.add(emp.getEmployeeName());
		// hrEmployees.add(emp.getEmployeeName());
		// }
		// }
		//
		// @Override
		// public void onException(AccounterException caught) {
		// Accounter
		// .showInformation("Error Showing  Employees List");
		// }
		// });

		employee = new EmployeeCombo(Accounter.constants().employee());
		employee.getMainWidget();
		employee.setHelpInformation(true);
		employee.setRequired(true);
		employee.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientEmployee>() {

			@Override
			public void selectedComboBoxItem(ClientEmployee selectItem) {

			}
		});
		if (!Accounter.getUser().isAdminUser()) {
			// employee.setValue(Accounter.getUser().getName());
			employee.setAdmin(false);
		}

		String listString[] = new String[] {
				Accounter.constants().cash(),
				UIUtils.getpaymentMethodCheckBy_CompanyType(Accounter
						.constants().check()),
				Accounter.constants().creditCard(),
				Accounter.constants().directDebit(),
				Accounter.constants().masterCard(),
				Accounter.constants().onlineBanking(),
				Accounter.constants().standingOrder(),
				Accounter.constants().switchMaestro() };
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
			employee.setComboItem(cashPurchase.getEmployee());
			employee.setDisabled(true);
			if (Accounter.getUser().isAdmin()) {
				employee.setAdmin(true);
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
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		result.add(vendorForm.validate());
		if (!AccounterValidator.validateTransactionDate(transactionDate)) {
			result.addError(transactionDateItem,
					AccounterErrorType.InvalidTransactionDate);
		}
		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDateItem, AccounterErrorType.InvalidDate);
		}
		if (Accounter.getUser().canApproveExpences())
			if (!payFromCombo.validate()) {
				result.addError(
						payFromCombo,
						Accounter.messages().pleaseEnter(
								payFromCombo.getTitle()));
			}
		if (!AccounterValidator.validate_dueOrDelivaryDates(
				deliveryDateItem.getEnteredDate(), this.transactionDate)) {
			result.addError(deliveryDateItem, Accounter.constants().the()
					+ " "
					+ Accounter.constants().deliveryDate()
					+ " "
					+ " "
					+ Accounter.constants()
							.cannotbeearlierthantransactiondate());

		}
		if (AccounterValidator.isBlankTransaction(vendorTransactionGrid)) {
			result.addError(vendorTransactionGrid,
					AccounterErrorType.blankTransaction);
		}
		result.add(vendorTransactionGrid.validateGrid());
		return result;
	}

	@Override
	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					ClientCashPurchase purchase = (ClientCashPurchase) transactionObject;
					if (purchase.getExpenseStatus() == ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_APPROVED) {
						Accounter.showError(Accounter.constants()
								.expenseisApproved());
					} else
						enableFormItems();
				}
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transactionObject
				.getType());
		this.rpcDoSerivce.canEdit(type, transactionObject.id, editCallBack);
	}

	@Override
	protected void enableFormItems() {
		super.enableFormItems();
		employee.setDisabled(isEdit);
		if (Accounter.getUser().isAdmin()) {
			employee.setAdmin(true);
		}
	}

	@Override
	protected void showMenu(AccounterButton button) {
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			setMenuItems(button, Accounter.constants().serviceItem());
		else
			setMenuItems(button, Accounter.constants().serviceItem());
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().employeeExpense();
	}

	@Override
	protected void createButtons(ButtonBar buttonBar) {
		super.createButtons(buttonBar);
		approveButton = new ImageButton(Accounter.constants().approve(),
				Accounter.getFinanceImages().approve());
		approveButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				approve();
			}
		});

		submitForApprove = new ImageButton(Accounter.constants()
				.submitForApproval(), Accounter.getFinanceImages()
				.submitForApproval());
		submitForApprove.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				onSubmitForApproval();
			}
		});
		buttonBar.add(approveButton);
		buttonBar.add(submitForApprove);
	}

	protected void onSubmitForApproval() {
		// TODO Auto-generated method stub

	}

	protected void approve() {
		// TODO Auto-generated method stub

	}
}
