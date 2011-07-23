package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.HistoryTokenUtils;
import com.vimukti.accounter.web.client.ui.company.NewAccountAction;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;

public class PurchaseAccountsCombo extends GridAccountsCombo {
	private List<ClientAccount> gridAccounts;

	public PurchaseAccountsCombo(String title) {
		super(title);
		initCombo(getAccounts());
	}

	public PurchaseAccountsCombo(String title, boolean isAddNewRequired) {
		super(title, isAddNewRequired);
		initCombo(getAccounts());
	}

	public List<ClientAccount> getAccounts() {
		gridAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : Accounter.getCompany()
				.getActiveAccounts()) {
			if (account.getType() != ClientAccount.TYPE_CASH
					&& account.getType() != ClientAccount.TYPE_BANK
					&& account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
					&& account.getType() != ClientAccount.TYPE_INCOME
					&& account.getType() != ClientAccount.TYPE_OTHER_INCOME
					&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_ASSET
					&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
					&& account.getType() != ClientAccount.TYPE_OTHER_ASSET
					&& account.getType() != ClientAccount.TYPE_EQUITY
					&& account.getType() != ClientAccount.TYPE_LONG_TERM_LIABILITY)
				gridAccounts.add(account);
		}
		return gridAccounts;
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
		NewAccountAction action = CompanyActionFactory.getNewAccountAction();
		action.setActionSource(this);
		// action.setAccountTypes(UIUtils
		// .getOptionsByType(AccountCombo.GRID_ACCOUNTS_COMBO));
		action.setAccountTypes(Arrays.asList(
				ClientAccount.TYPE_COST_OF_GOODS_SOLD,
				ClientAccount.TYPE_OTHER_EXPENSE,
				ClientAccount.TYPE_FIXED_ASSET, ClientAccount.TYPE_EXPENSE));
		HistoryTokenUtils.setPresentToken(action, null);
		action.run(null, true);

	}

	@Override
	public SelectItemType getSelectItemType() {
		return SelectItemType.ACCOUNT;
	}
}
