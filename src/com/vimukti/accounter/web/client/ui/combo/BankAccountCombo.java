package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.HistoryTokenUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.banking.NewBankAccountAction;
import com.vimukti.accounter.web.client.ui.core.BankingActionFactory;

public class BankAccountCombo extends AccountCombo {

	private ArrayList<ClientAccount> bankAccounts;

	public BankAccountCombo(String title) {
		super(title);
	}

	@Override
	public String getDefaultAddNewCaption() {
		return comboConstants.newAccount();
	}

	@Override
	protected String getDisplayName(ClientAccount object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		NewBankAccountAction action = BankingActionFactory
				.getNewBankAccountAction();
		action.setActionSource(this);
		action.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.BANK_ACCOUNTS_COMBO));
		HistoryTokenUtils.setPresentToken(action, null);
		action.run(null, true);

	}

	@Override
	public List<ClientAccount> getAccounts() {
		bankAccounts = new ArrayList<ClientAccount>();

		for (ClientAccount account : FinanceApplication.getCompany()
				.getActiveAccounts()) {
			if (account.getType() == ClientAccount.TYPE_BANK)
				bankAccounts.add(account);
		}
		return bankAccounts;
	}

	@Override
	public SelectItemType getSelectItemType() {

		return SelectItemType.BANK_ACCOUNT;
	}

}
