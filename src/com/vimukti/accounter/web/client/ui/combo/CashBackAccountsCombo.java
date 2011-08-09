package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.AccounterConstants;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.company.NewAccountAction;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;

public class CashBackAccountsCombo extends AccountCombo {

	private List<ClientAccount> cashBackAccounts;

	public CashBackAccountsCombo(String title) {
		super(title);
	}

	public List<ClientAccount> getAccounts() {
		cashBackAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : Accounter.getCompany().getActiveAccounts()) {
			if (account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
					&& !account.getName().equals(
							AccounterConstants.UN_DEPOSITED_FUNDS)) {
				cashBackAccounts.add(account);
			}
		}
		return cashBackAccounts;
	}

	@Override
	public void onAddNew() {
		NewAccountAction action = ActionFactory.getNewAccountAction();
		action.setCallback(new ActionCallback<ClientAccount>() {

			@Override
			public void actionResult(ClientAccount result) {
				addItemThenfireEvent(result);
			}
		});
		action.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.CASH_BACK_ACCOUNTS_COMBO));

		action.run(null, true);

	}

	@Override
	public SelectItemType getSelectItemType() {
		return SelectItemType.ACCOUNT;
	}

}
