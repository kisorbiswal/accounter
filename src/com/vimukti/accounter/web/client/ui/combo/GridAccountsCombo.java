package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.HistoryTokenUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.company.NewAccountAction;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;

public class GridAccountsCombo extends AccountCombo {

	private List<ClientAccount> gridAccounts;

	public GridAccountsCombo(String title) {
		super(title);
		initCombo(getAccounts());
	}

	public GridAccountsCombo(String title, boolean isAddNewRequired) {
		super(title, isAddNewRequired);
		initCombo(getAccounts());
	}

	public List<ClientAccount> getAccounts() {
		gridAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : FinanceApplication.getCompany()
				.getActiveAccounts()) {
			if (account.getType() != ClientAccount.TYPE_CASH
					&& account.getType() != ClientAccount.TYPE_BANK
					&& account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE)
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
		action.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.GRID_ACCOUNTS_COMBO));
		HistoryTokenUtils.setPresentToken(action, null);
		action.run(null, true);

	}

	@Override
	public SelectItemType getSelectItemType() {
		return SelectItemType.ACCOUNT;
	}

}
