/**
 * 
 */
package com.vimukti.accounter.web.client.ui.banking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientBankAccount;
import com.vimukti.accounter.web.client.core.ClientReconciliation;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.BankAccountCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ReconciliationsHistoryView extends BaseView<ClientReconciliation> {

	private BankAccountCombo bankAccountsCombo;
	private ReconciliationsTable grid;
	private ClientBankAccount selectedBankAccount;
	private Map<String, ClientBankAccount> bankAccounts = new HashMap<String, ClientBankAccount>();

	private void createControls() {

		this.bankAccountsCombo = new BankAccountCombo(
				constants.selectBankAccount());
		this.bankAccountsCombo.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				bankAccountChanged(bankAccountsCombo.getSelectedValue());
			}
		});

		this.grid = new ReconciliationsTable();
		grid.setWidth("100%");

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		DynamicForm form = new DynamicForm();
		form.setItems(bankAccountsCombo);
		Label label = new Label("Reconciliation List");
		label.setStyleName("bold");

		mainPanel.add(form);
		mainPanel.add(label);
		mainPanel.add(grid);
		mainPanel.setCellHeight(grid, "200px");
		grid.getElement().getParentElement()
				.addClassName("recounciliation_grid");
		this.add(mainPanel);
	}

	/**
	 * @param selectItem
	 */
	protected void bankAccountChanged(ClientAccount clientAccount) {
		this.selectedBankAccount = bankAccounts.get(clientAccount.getName());
		rpcGetService.getReconciliationsByBankAccountID(
				selectedBankAccount.getID(),
				new AccounterAsyncCallback<List<ClientReconciliation>>() {

					@Override
					public void onException(AccounterException exception) {

					}

					@Override
					public void onResultSuccess(
							List<ClientReconciliation> result) {
						grid.setData(result);
						grid.getLoadingIndicator().removeFromParent();						
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
			return;
		}
		for (ClientAccount account : bankAccounts) {
			bankAccountsCombo.addItem(account);
			this.bankAccounts.put(account.getName(),
					(ClientBankAccount) account);
		}
		bankAccountsCombo.setSelectedItem(0);
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

}
