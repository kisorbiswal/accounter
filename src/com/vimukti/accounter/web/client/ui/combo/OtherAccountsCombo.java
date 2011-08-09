package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;

public class OtherAccountsCombo extends AccountCombo {

	private ArrayList<ClientAccount> filtrdAccounts;

	public OtherAccountsCombo(String title) {
		super(title);
	}

	public OtherAccountsCombo(String title, boolean isAddNewRequired) {
		super(title, isAddNewRequired);
	}

	@Override
	public List<ClientAccount> getAccounts() {
		return getCompany().getActiveAccounts();
	}

	public List<ClientAccount> getFilterdAccounts() {
		filtrdAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : getCompany().getActiveAccounts()) {
			if (account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
					&& account.getType() != ClientAccount.TYPE_INVENTORY_ASSET) {
				filtrdAccounts.add(account);
			}
		}

		return filtrdAccounts;
	}

}
