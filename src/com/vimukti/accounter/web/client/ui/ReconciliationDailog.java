package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientBankAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientReconciliation;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class ReconciliationDailog extends BaseDialog<ClientReconciliation>
		implements WidgetWithErrors {

	private SelectCombo bankAccountsField;
	private DateField startDate;
	private DateField endDate;
	private AmountField closingBalance;
	private ClientReconciliation reconcilition;
	private String account;

	public ReconciliationDailog(String reconciliation,
			ClientReconciliation reconcilition) {

		super(reconciliation, "");
		this.reconcilition = reconcilition;
		setWidth("400px");
		createControls();
		center();
	}

	private void createControls() {

		VerticalPanel mainpanel = new VerticalPanel();

		bankAccountsField = new SelectCombo(messages.bankAccount(Global.get()
				.Account()));

		bankAccountsField
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {
					@Override
					public void selectedComboBoxItem(String selectItem) {
						accountNameSelected(selectItem);
					}
				});

		accountNameSelected(reconcilition.getAccount().getName());

		ArrayList<ClientAccount> accounts = Accounter.getCompany().getAccounts(
				ClientAccount.TYPE_BANK);

		for (ClientAccount account : accounts) {
			bankAccountsField.addItem(account.getName());
		}

		startDate = new DateField(constants.startDate());
		endDate = new DateField(constants.endDate());
		// set the month start date and end date
		// Calendar calendar = Calendar.getInstance();
		// ClientFinanceDate clientFinanceDate = new ClientFinanceDate();
		// clientFinanceDate.setDay(1);
		startDate.setValue(new ClientFinanceDate());
		// clientFinanceDate.setDay(calendar
		// .getActualMaximum(Calendar.DAY_OF_MONTH));
		endDate.setValue(new ClientFinanceDate());

		closingBalance = new AmountField(constants.ClosingBalance(), this);
		if (reconcilition.getStartDate() != null)
			startDate.setValue(reconcilition.getStartDate().getDateAsObject());
		if (reconcilition.getEndDate() != null)
			endDate.setValue(reconcilition.getEndDate().getDateAsObject());
		closingBalance.setValue(String.valueOf(reconcilition
				.getClosingBalance()));

		DynamicForm form = new DynamicForm();
		form.setWidth("100%");
		form.setFields(bankAccountsField, closingBalance, startDate, endDate);

		// DynamicForm datesForm = new DynamicForm();
		// form.setWidth("100%");
		// datesForm.setFields(startDate, endDate);

		// HorizontalPanel hPanel = new HorizontalPanel();
		// hPanel.setWidth("100%");
		// hPanel.add(form);
		// hPanel.add(datesForm);

		mainpanel.add(form);
		setBodyLayout(mainpanel);
	}

	/**
	 * Create the Reconciliation Object.
	 * 
	 * @return {@link ClientReconciliation}
	 */
	private ClientReconciliation createReconciliation() {
		ClientReconciliation reconciliation = new ClientReconciliation();
		reconciliation.setClosingBalance(closingBalance.getAmount());
		reconciliation.setStartDate(startDate.getDate());
		reconciliation.setEndDate(endDate.getDate());
		reconciliation.setReconcilationDate(new ClientFinanceDate());
		ArrayList<ClientAccount> accounts = Accounter.getCompany().getAccounts(
				ClientAccount.TYPE_BANK);
		for (ClientAccount account : accounts) {
			if (account.getName().equalsIgnoreCase(
					bankAccountsField.getValue().toString())) {
				reconciliation.setAccount((ClientBankAccount) account);
				reconciliation.setOpeningBalance(account.getCurrentBalance());
			}
		}
		return reconciliation;
	}

	protected void accountNameSelected(String selectItem) {
		this.account = selectItem;
		if (account != null)
			bankAccountsField.setComboItem(this.account);
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		ClientFinanceDate sDate = startDate.getDate();
		ClientFinanceDate eDate = endDate.getDate();
		if (eDate.before(sDate)) {
			result.addError(endDate, constants.validateEndAndStartDate());
		}
		if (bankAccountsField.getSelectedIndex() < 0) {
			result.addError(bankAccountsField, "BankAccount");
		}
		return result;
	}

	@Override
	protected boolean onOK() {
		ActionFactory.getNewReconciliationAction().run(createReconciliation(),
				false);
		return true;
	}
}
