package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.ClientPayEmployee;
import com.vimukti.accounter.web.client.core.ClientPayStructureDestination;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionPayEmployee;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.BankAccountCombo;
import com.vimukti.accounter.web.client.ui.combo.EmployeesAndGroupsCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class PayEmployeeView extends
		AbstractTransactionBaseView<ClientPayEmployee> {

	private EmployeesAndGroupsCombo employeeCombo;
	private BankAccountCombo bankAccountCombo;
	private PayEmployeeTable table;
	private List<DynamicForm> listforms;
	public double totalOrginalAmt = 0.0D, totalDueAmt = 0.0D,
			totalPayment = 0.0D;

	public PayEmployeeView() {
		super(ClientTransaction.TYPE_PAY_EMPLOYEE);
		this.getElement().setId("pay-employee-view");
	}

	@Override
	public void deleteFailed(AccounterException caught) {
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
	}

	@Override
	protected void createControls() {
		listforms = new ArrayList<DynamicForm>();
		StyledPanel mainPanel = new StyledPanel("main-panel");

		Label lab1 = new Label(messages.payEmployee());
		lab1.setStyleName("label-title");
		DynamicForm dateNoForm = new DynamicForm("dateNoForm");

		transactionDateItem = createTransactionDateItem();
		transactionNumber = createTransactionNumberItem();

		transactionNumber.setTitle(messages.no());
		dateNoForm.setStyleName("datenumber-panel");

		if (!isTemplate) {
			dateNoForm.add(transactionDateItem, transactionNumber);
		}
		employeeCombo = new EmployeesAndGroupsCombo(messages.employeeOrGroup(),
				"employee-group") {
			@Override
			public void selectCombo() {
				super.selectCombo();
				employeeSelected(getSelectedValue());
			}
		};
		employeeCombo.setRequired(true);
		employeeCombo.setEnabled(!isInViewMode());
		employeeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPayStructureDestination>() {

					@Override
					public void selectedComboBoxItem(
							ClientPayStructureDestination selectItem) {
						employeeSelected(selectItem);
					}
				});

		bankAccountCombo = new BankAccountCombo(messages.payFrom());
		bankAccountCombo.setEnabled(!isInViewMode());
		bankAccountCombo.setRequired(true);

		table = new PayEmployeeTable(this) {

			protected void updateTransactionTotlas() {
				PayEmployeeView.this.updateNonEditableItems();
			}

			@Override
			protected boolean isInViewMode() {
				return PayEmployeeView.this.isInViewMode();
			}

		};
		table.setEnabled(!isInViewMode());
		DynamicForm mainForm = new DynamicForm("main-form");
		mainForm.add(employeeCombo, bankAccountCombo);
		mainPanel.add(dateNoForm);
		mainPanel.add(mainForm);
		mainPanel.add(table);

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setEnabled(!isInViewMode());

		DynamicForm memoForm = new DynamicForm("memoForm");

		transactionTotalBaseCurrencyText = new AmountLabel(
				messages.currencyTotal(getCompany().getPreferences()
						.getPrimaryCurrency().getFormalName()), getCompany()
						.getPreferences().getPrimaryCurrency());
		mainPanel.add(transactionTotalBaseCurrencyText);

		memoForm.add(memoTextAreaItem);

		mainPanel.add(memoForm);

		listforms.add(dateNoForm);
		listforms.add(mainForm);

		this.add(mainPanel);
	}

	protected void employeeSelected(ClientPayStructureDestination selectItem) {
		table.clear();
		if (transaction.isVoid()) {
			setRecordsToTable(new ArrayList<ClientTransactionPayEmployee>());
			return;
		}
		if (selectItem != null) {
			Accounter.createPayrollService().getTransactionPayEmployeeList(
					selectItem,
					new AsyncCallback<List<ClientTransactionPayEmployee>>() {

						@Override
						public void onSuccess(
								List<ClientTransactionPayEmployee> result) {
							setRecordsToTable(result);
						}

						@Override
						public void onFailure(Throwable caught) {
						}
					});
		}
	}

	protected void setRecordsToTable(List<ClientTransactionPayEmployee> result) {
		table.setAllRows(transaction.getTransactionPayEmployee());
		table.selectAllRows(true);
		if (result != null) {
			for (ClientTransactionPayEmployee clientTransactionPayEmployee : result) {
				table.add(clientTransactionPayEmployee);
			}
		}
		updateNonEditableItems();
	}

	@Override
	protected void initTransactionViewData() {
		if (transaction != null) {
			long employee = data.getEmployee();
			if (employee == 0) {
				employee = data.getEmployeeGroup();
			}
			table.setAllRows(transaction.getTransactionPayEmployee());
			table.selectAllRows(true);
			employeeCombo.setEmpGroup(employee, data.getEmployee() != 0);
			bankAccountCombo.setComboItem(getCompany().getAccount(
					transaction.getPayAccount()));
			transactionNumber.setValue(transaction.getNumber());
			transactionDateItem.setValue(transaction.getDate());
			memoTextAreaItem.setValue(transaction.getMemo());
			transactionTotalBaseCurrencyText.setAmount(transaction.getTotal());
		} else {
			transaction = new ClientPayEmployee();
		}
		initTransactionNumber();
	}

	@Override
	public void updateNonEditableItems() {
		List<ClientTransactionPayEmployee> selectedRecords = table
				.getSelectedRecords();
		Double total = 0.0D;
		for (ClientTransactionPayEmployee clientTransactionPayEmployee : selectedRecords) {
			total += clientTransactionPayEmployee.getPayment();
		}
		transactionTotalBaseCurrencyText.setAmount(total);
	}

	@Override
	protected void refreshTransactionGrid() {
	}

	@Override
	protected void updateDiscountValues() {
	}

	@Override
	public void updateAmountsFromGUI() {
		this.table.updateAmountsFromGUI();
	}

	@Override
	protected void classSelected(ClientAccounterClass clientAccounterClass) {
	}

	@Override
	protected String getViewTitle() {
		return messages.payEmployee();
	}

	@Override
	public List<DynamicForm> getForms() {
		return listforms;
	}

	@Override
	public void setFocus() {
		employeeCombo.setFocus();
	}

	@Override
	public void saveAndUpdateView() {
		super.saveAndUpdateView();
		transaction.setTransactionPayEmployee(table.getSelectedRecords());
		if (employeeCombo.getSelectedValue() instanceof ClientEmployee) {
			transaction.setEmployee(employeeCombo.getSelectedValue().getID());
		} else {
			transaction.setEmployeeGroup(employeeCombo.getSelectedValue()
					.getID());
		}
		transaction.setPayAccount(bankAccountCombo.getSelectedValue().getID());
		transaction.setDate(getTransactionDate().getDate());
		transaction.setNumber(transactionNumber.getValue());
		transaction.setCurrency(getCompany().getPrimaryCurrency().getID());
		transaction.setMemo(memoTextAreaItem.getValue());
		transaction.setTotal(transactionTotalBaseCurrencyText.getAmount());
		saveOrUpdate(transaction);
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		if (employeeCombo.getSelectedValue() == null
				|| employeeCombo.getSelectedValue().getName().trim().isEmpty()) {
			result.addError(employeeCombo,
					messages.pleaseSelect(messages.employeeGroup()));
		}
		if (bankAccountCombo.getSelectedValue() == null
				|| bankAccountCombo.getSelectedValue().getName().trim()
						.isEmpty()) {
			result.addError(bankAccountCombo,
					messages.pleaseSelect(messages.Account()));
		}
		if (table.getSelectedRecords().isEmpty()) {
			result.addError(table, messages.pleaseSelectAtLeastOneRecord());
		}
		return result;
	}

	@Override
	public void onEdit() {

		AsyncCallback<Boolean> editCallBack = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter.showMessage(messages.sessionExpired());
				} else {
					int errorCode = ((AccounterException) caught)
							.getErrorCode();
					Accounter.showError(AccounterExceptions
							.getErrorString(errorCode));

				}
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transaction
				.getType());
		this.rpcDoSerivce.canEdit(type, transaction.id, editCallBack);

	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		bankAccountCombo.setEnabled(!isInViewMode());
		table.setEnabled(!isInViewMode());
		memoTextAreaItem.setEnabled(!isInViewMode());
		transactionNumber.setEnabled(!isInViewMode());
		transactionDateItem.setEnabled(!isInViewMode());
	}

	@Override
	protected boolean canRecur() {
		return false;
	}

	@Override
	protected boolean canAddDraftButton() {
		return false;
	}
}
