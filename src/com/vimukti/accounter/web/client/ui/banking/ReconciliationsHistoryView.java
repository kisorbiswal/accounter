/**
 * 
 */
package com.vimukti.accounter.web.client.ui.banking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientBankAccount;
import com.vimukti.accounter.web.client.core.ClientReconciliation;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ReconciliationsHistoryView extends BaseView<ClientReconciliation> {

	private SelectCombo bankAccountsCombo;
	private ReconciliationsTable grid;
	private ClientBankAccount selectedBankAccount;
	private Map<String, ClientBankAccount> bankAccounts = new HashMap<String, ClientBankAccount>();

	private void createControls() {

		this.bankAccountsCombo = new SelectCombo(constants.selectBankAccount());
		this.bankAccountsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						bankAccountChanged(selectItem);
					}
				});

		this.grid = new ReconciliationsTable();
		grid.setWidth("100%");

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		DynamicForm form = new DynamicForm();
		form.setItems(bankAccountsCombo);
		mainPanel.add(form);
		mainPanel.add(grid);
		grid.setStyleName("recounciliation_grid");
		this.add(mainPanel);
	}

	/**
	 * @param selectItem
	 */
	protected void bankAccountChanged(String accountName) {
		this.selectedBankAccount = bankAccounts.get(accountName);
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
		for (ClientAccount account : bankAccounts) {
			bankAccountsCombo.addItem(account.getName());
			this.bankAccounts.put(account.getName(),
					(ClientBankAccount) account);
		}
		bankAccountsCombo.setSelectedItem(0);
		bankAccountChanged(bankAccountsCombo.getSelectedValue());

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
