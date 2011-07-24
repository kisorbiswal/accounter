package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.company.NewAccountAction;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;

public class PurchaseItemCombo extends AccountCombo {
	private ArrayList<ClientAccount> filtrdAccounts;

	public PurchaseItemCombo(String title) {
		super(title);
	}

	@Override
	public List<ClientAccount> getAccounts() {
		return getCompany().getActiveAccounts();
	}

	public List<ClientAccount> getFilterdAccounts() {
		filtrdAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : getCompany()
				.getActiveAccounts()) {
			if (account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
					&& account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
					&& account.getType() != ClientAccount.TYPE_INCOME
					&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_ASSET
					&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
					&& account.getType() != ClientAccount.TYPE_FIXED_ASSET
					&& account.getType() != ClientAccount.TYPE_CASH
					&& account.getType() != ClientAccount.TYPE_LONG_TERM_LIABILITY
					&& account.getType() != ClientAccount.TYPE_OTHER_ASSET
					&& account.getType() != ClientAccount.TYPE_EQUITY) {
				filtrdAccounts.add(account);
			}
		}

		return filtrdAccounts;
	}

	@Override
	public void onAddNew() {
		NewAccountAction action = CompanyActionFactory.getNewAccountAction();
		action.setActionSource(this);
		action.setAccountTypes(Arrays.asList(
				ClientAccount.TYPE_COST_OF_GOODS_SOLD,
				ClientAccount.TYPE_OTHER_EXPENSE, ClientAccount.TYPE_EXPENSE));
		
		action.run(null, true);

	}

	@Override
	public SelectItemType getSelectItemType() {
		return SelectItemType.ACCOUNT;
	}

}
