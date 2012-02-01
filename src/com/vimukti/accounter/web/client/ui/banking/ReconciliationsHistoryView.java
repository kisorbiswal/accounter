/**
 * 
 */
package com.vimukti.accounter.web.client.ui.banking;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientReconciliation;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.ReconciliationAccountCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.customers.InvoicePrintDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

/**
 * @author Prasanna Kumar G
 * 
 */

public class ReconciliationsHistoryView extends BaseView<ClientReconciliation> {

	private ReconciliationAccountCombo bankAccountsCombo;
	private ReconciliationsTable grid;
	private ClientAccount selectedAccount;
	private List<ClientReconciliation> listOfReconcilation;

	private void createControls() {

		this.bankAccountsCombo = new ReconciliationAccountCombo(
				messages.selectBankAccount());
		this.bankAccountsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						bankAccountChanged(selectItem);
					}
				});
		this.grid = new ReconciliationsTable();
		grid.setWidth("100%");

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		DynamicForm form = new DynamicForm();
		form.setItems(bankAccountsCombo);
		Label label = new Label(messages.ReconciliationsList());
		label.setStyleName("bold");

		mainPanel.add(form);
		mainPanel.add(label);
		mainPanel.add(grid);
		// mainPanel.setCellHeight(grid, "200px");
		grid.getElement().getParentElement()
				.addClassName("recounciliation_grid");
		this.add(mainPanel);
		if (saveAndCloseButton != null)
			saveAndCloseButton.setVisible(false);
		if (saveAndNewButton != null)
			saveAndNewButton.setVisible(false);
	}

	/**
	 * @param selectItem
	 */
	protected void bankAccountChanged(ClientAccount clientAccount) {
		this.selectedAccount = clientAccount;
		rpcGetService.getReconciliationsByBankAccountID(
				selectedAccount.getID(),
				new AccounterAsyncCallback<List<ClientReconciliation>>() {

					@Override
					public void onException(AccounterException exception) {

					}

					@Override
					public void onResultSuccess(
							List<ClientReconciliation> result) {
						listOfReconcilation = result;
						grid.setData(result);

					}
				});
	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	@Override
	public void initData() {
		ArrayList<ClientAccount> bankAccounts = Accounter.getCompany()
				.getAccounts(ClientAccount.TYPE_BANK);
		if (bankAccounts == null || bankAccounts.isEmpty()) {
			grid.setData(new ArrayList<ClientReconciliation>());
			return;
		}
		for (ClientAccount account : bankAccounts) {
			bankAccountsCombo.addItem(account);
		}
		if (bankAccountsCombo.getSelectedValue() == null) {
			bankAccountsCombo.setSelectedItem(0);
		}
		bankAccountChanged(bankAccountsCombo.getSelectedValue());
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	protected String getViewTitle() {
		return null;
	}

	@Override
	public List<DynamicForm> getForms() {
		return null;
	}

	@Override
	public void setFocus() {
		this.bankAccountsCombo.setFocus();

	}

	@Override
	protected boolean canVoid() {
		return false;
	}

	@Override
	protected boolean canDelete() {
		return false;
	}

	@Override
	public void print() {
	}

	@Override
	public void printPreview() {

	}

	public void showDialogBox(String description) {
		InvoicePrintDialog printDialog = new InvoicePrintDialog(
				messages.selectReports(), "", description);
		printDialog.show();
		printDialog.center();
	}

	@Override
	public ClientReconciliation saveView() {
		ClientReconciliation reconciliation = new ClientReconciliation();
		reconciliation.setAccount(bankAccountsCombo.getSelectedValue());
		return reconciliation;
	}

	@Override
	public void restoreView(ClientReconciliation reconciliation) {
		if (reconciliation == null) {
			return;
		}
		bankAccountsCombo.setSelected(reconciliation.getAccount().getName());
		bankAccountChanged(bankAccountsCombo.getSelectedValue());
	}
}
