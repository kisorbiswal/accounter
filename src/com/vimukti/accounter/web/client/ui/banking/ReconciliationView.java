/**
 * 
 */
package com.vimukti.accounter.web.client.ui.banking;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientBankAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientReconciliation;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.core.ValidationResult.Error;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.SelectionChangedHandler;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ReconciliationView extends BaseView<ClientReconciliation> {

	private VerticalPanel mainPanel;
	private SelectCombo bankAccountsField;
	private DateField startDate;
	private DateField endDate;
	private TextItem closingBalance;
	private ReconciliationTransactionsGrid grid;
	private ClientBankAccount bankAccount;
	private Set<ClientTransaction> clearedTransactions;

	/**
	 * Creates new Instance
	 */
	public ReconciliationView(ClientBankAccount data) {
		this.bankAccount = data;
	}

	public void createControls() {

		// Creating Title for View
		Label label = new Label();
		label.removeStyleName("gwt-Label");
		label.addStyleName(Accounter.constants().labelTitle());
		label.setText(constants.Reconciliation());

		// Creating Input Fields to Start Reconciliation of an Account
		bankAccountsField = new SelectCombo(messages.bankAccount(Global.get()
				.Account()));

		bankAccountsField
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						// TODO Auto-generated method stub

					}
				});

		startDate = new DateField(constants.startDate());
		endDate = new DateField(constants.endDate());
		closingBalance = new TextItem(constants.ClosingBalance());

		DynamicForm form = new DynamicForm();
		form.setWidth("40%");
		form.setFields(bankAccountsField, closingBalance);

		DynamicForm datesForm = new DynamicForm();
		form.setWidth("60%");
		datesForm.setFields(startDate, endDate);

		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setWidth("100%");
		hPanel.add(form);
		hPanel.add(datesForm);

		Button startBtn = new Button(constants.Reconcile());
		startBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				startReconcile();
			}
		});

		HorizontalPanel btnPanel = new HorizontalPanel();
		btnPanel.setWidth("100%");
		btnPanel.add(startBtn);
		btnPanel.setCellHorizontalAlignment(startBtn,
				HasHorizontalAlignment.ALIGN_RIGHT);

		// Creating Amount Fields
		VerticalPanel amountsPanel = new VerticalPanel();
		amountsPanel.setWidth("100%");
		AmountLabel openingBalance = new AmountLabel(constants.openingBalance());
		openingBalance.setHelpInformation(true);
		openingBalance.setDisabled(true);

		AmountLabel closingBalance = new AmountLabel(constants.ClosingBalance());
		openingBalance.setHelpInformation(true);
		openingBalance.setDisabled(true);

		AmountLabel clearedAmount = new AmountLabel(constants.ClearedAmount());
		openingBalance.setHelpInformation(true);
		openingBalance.setDisabled(true);

		AmountLabel difference = new AmountLabel(constants.Difference());
		openingBalance.setHelpInformation(true);
		openingBalance.setDisabled(true);
		DynamicForm amountsForm = new DynamicForm();
		amountsForm.setWidth("50%");
		amountsForm.setItems(openingBalance, closingBalance, clearedAmount,
				difference);
		amountsPanel.add(amountsForm);
		amountsPanel.setCellHorizontalAlignment(amountsForm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		this.grid = new ReconciliationTransactionsGrid(
				new SelectionChangedHandler<ClientTransaction>() {

					@Override
					public void selectionChanged(ClientTransaction obj,
							boolean isSelected) {
						clearTransaction(obj, isSelected);
					}
				});
		grid.setWidth("100%");
		grid.setHeight("100px");

		this.mainPanel = new VerticalPanel();
		mainPanel.setSize("100%", "300px");
		mainPanel.add(label);
		mainPanel.add(hPanel);
		mainPanel.add(btnPanel);
		mainPanel.add(grid);
		mainPanel.add(amountsPanel);

		this.add(mainPanel);
	}

	/**
	 * @param value
	 */
	protected void clearTransaction(ClientTransaction value, boolean isClear) {
		if (isClear) {
			clearedTransactions.add(value);
		} else {
			clearedTransactions.remove(value);
		}

		// TODO Update Amounts
	}

	/**
	 * 
	 */
	protected void startReconcile() {
		ValidationResult validationResult = validateInputs();
		if (validationResult.haveErrors()) {
			for (Error error : validationResult.getErrors()) {
				addError(error.getSource(), error.getMessage());
			}
			return;
		}

		getTransactions();
	}

	/**
	 * Gets All the Transaction in between StartDate and EndDate
	 */
	private void getTransactions() {
		rpcGetService.getAllTransactionsOfAccount(bankAccount.getID(),
				startDate.getDate(), endDate.getDate(),
				new AccounterAsyncCallback<List<ClientTransaction>>() {

					@Override
					public void onException(AccounterException exception) {
						Accounter.showError(messages.unableToGet(constants
								.transaction()));
					}

					@Override
					public void onResultSuccess(List<ClientTransaction> result) {
						grid.setData(result);
					}
				});
	}

	/**
	 * Validates the Inputs
	 */
	private ValidationResult validateInputs() {
		ValidationResult result = new ValidationResult();
		ClientFinanceDate sDate = startDate.getDate();
		ClientFinanceDate eDate = endDate.getDate();
		if (eDate.before(sDate)) {
			result.addError(endDate, "EndDate");
		}

		if (closingBalance.getValue().isEmpty()) {
			result.addError(closingBalance, "ClosingBalance");
		}
		if (bankAccountsField.getSelectedIndex() < 0) {
			result.addError(bankAccountsField, "BankAccount");
		}
		return result;
	}

	@Override
	public void init() {
		super.init();
		setMode(EditMode.CREATE);
		createControls();
	}

	@Override
	public void initData() {
		super.initData();

		ArrayList<ClientAccount> accounts = Accounter.getCompany().getAccounts(
				ClientAccount.TYPE_BANK);
		for (ClientAccount account : accounts) {
			bankAccountsField.addItem(account.getName());
		}
		bankAccountsField.setSelected(bankAccount.getName());
	}

	@Override
	public ValidationResult validate() {
		validateInputs();
		return super.validate();
	}

	@Override
	public void saveAndUpdateView() {
		ClientReconciliation reconciliation = new ClientReconciliation();
		reconciliation.setClosingBalance(Double.parseDouble(closingBalance
				.getValue()));
		reconciliation.setOpeningBalance(bankAccount.getOpeningBalance());
		reconciliation.setStartDate(startDate.getDate());
		reconciliation.setEndDate(endDate.getDate());
		reconciliation.setReconcilationDate(new ClientFinanceDate());
		reconciliation.setTransactions(this.clearedTransactions);
		reconciliation.setAccount(bankAccount);

		saveOrUpdate(reconciliation);
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

}
