package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.Accounter;

public class RevenueAccountCombo extends AccountCombo {

	private List<ClientAccount> revenueAccounts;

	public RevenueAccountCombo(String title) {
		super(title);
	}

	public RevenueAccountCombo(String title, boolean isAddNewRequired) {
		super(title, isAddNewRequired);
	}

	@Override
	protected List<ClientAccount> getAccounts() {
		revenueAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : Accounter.getCompany()
				.getActiveAccounts())
			if (account.getType() == ClientAccount.TYPE_INCOME) {
				revenueAccounts.add(account);
			}
		return revenueAccounts;
	}

	@Override
	public SelectItemType getSelectItemType() {
		return SelectItemType.ACCOUNT;
	}

}
