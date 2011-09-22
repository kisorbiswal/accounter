package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.company.NewAccountAction;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;

public class MakeDepositAccountCombo extends AccountCombo {

	private List<ClientAccount> deposiInAccounts;

	public MakeDepositAccountCombo(String title) {
		super(title);
	}

	@Override
	public List<ClientAccount> getAccounts() {
		deposiInAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : getCompany().getActiveAccounts()) {
			if (getCompany().getPreferences().isRegisteredForVAT()) {
				if (Arrays.asList(ClientAccount.TYPE_BANK,
						ClientAccount.TYPE_OTHER_CURRENT_ASSET,
						ClientAccount.TYPE_OTHER_CURRENT_LIABILITY,
						ClientAccount.TYPE_EQUITY).contains(account.getType())) {

					deposiInAccounts.add(account);

				}
			} else {
				if (Arrays.asList(ClientAccount.TYPE_OTHER_CURRENT_ASSET,
						ClientAccount.TYPE_OTHER_CURRENT_LIABILITY,
						ClientAccount.TYPE_BANK, ClientAccount.TYPE_EQUITY)
						.contains(account.getType())) {

					deposiInAccounts.add(account);

				}
			}

		}
		setDefaultDepositInAccount();
		return deposiInAccounts;
	}

	public void setAccounts() {
		deposiInAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : getCompany().getActiveAccounts()) {
			if (Arrays.asList(ClientAccount.TYPE_OTHER_CURRENT_ASSET,
					ClientAccount.TYPE_OTHER_CURRENT_LIABILITY,
					ClientAccount.TYPE_BANK, ClientAccount.TYPE_EQUITY)
					.contains(account.getType())) {

				deposiInAccounts.add(account);

			}
		}

		this.initCombo(deposiInAccounts);
		setDefaultDepositInAccount();

	}

	private void setDefaultDepositInAccount() {
		List<ClientAccount> accounts = getCompany().getAccounts();
		for (ClientAccount account : accounts) {
			if (account.getNumber().equals("1100")) {
				this.addItemThenfireEvent(account);
				break;
			}
		}
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
		action.setAccountTypes(Arrays.asList(
				ClientAccount.TYPE_OTHER_CURRENT_ASSET,
				ClientAccount.TYPE_OTHER_CURRENT_LIABILITY,
				ClientAccount.TYPE_BANK, ClientAccount.TYPE_EQUITY));

		action.run(null, true);

	}

	@Override
	public void addItemThenfireEvent(ClientAccount obj) {
		if (filter(obj)) {
			super.addItemThenfireEvent(obj);
		}
	}

	private boolean filter(ClientAccount account) {
		return Arrays.asList(ClientAccount.TYPE_OTHER_CURRENT_ASSET,
				ClientAccount.TYPE_OTHER_CURRENT_LIABILITY,
				ClientAccount.TYPE_BANK, ClientAccount.TYPE_EQUITY).contains(
				account.getType());
	}

}
