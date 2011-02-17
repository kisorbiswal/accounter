package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.FinanceApplication;

public class TaxAgencyAccountsCombo extends AccountCombo {

	private List<ClientAccount> taxAgencyAccounts;

	public TaxAgencyAccountsCombo(String title) {
		super(title);
	}

	@Override
	public List<ClientAccount> getAccounts() {
		taxAgencyAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : FinanceApplication.getCompany()
				.getActiveAccounts())
			if (account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
					&& account.getType() != ClientAccount.TYPE_INCOME
					&& account.getType() != ClientAccount.TYPE_EXPENSE
					&& account.getType() != ClientAccount.TYPE_COST_OF_GOODS_SOLD)
				taxAgencyAccounts.add(account);
		return taxAgencyAccounts;
	}

	@Override
	public SelectItemType getSelectItemType() {
		return SelectItemType.ACCOUNT;
	}

}
