package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.company.NewAccountAction;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;

public class DepositInAccountCombo extends AccountCombo {


	public DepositInAccountCombo(String title) {
		super(title);
	}

	public List<ClientAccount> getAccounts() {
		ArrayList<ClientAccount> deposiInAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : getCompany().getActiveAccounts()) {
			if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
				if (Arrays.asList(
						ClientAccount.TYPE_BANK,
						// ClientAccount.TYPE_CASH,
						ClientAccount.TYPE_CREDIT_CARD,
						ClientAccount.TYPE_OTHER_CURRENT_ASSET,
						ClientAccount.TYPE_FIXED_ASSET).contains(
						account.getType())) {

					deposiInAccounts.add(account);

				}
			} else {
				if (Arrays
						.asList(ClientAccount.TYPE_CREDIT_CARD,
								ClientAccount.TYPE_OTHER_CURRENT_ASSET,
								ClientAccount.TYPE_BANK,
								ClientAccount.TYPE_FIXED_ASSET).contains(
								account.getType())) {

					deposiInAccounts.add(account);

				}
			}
		}

		return deposiInAccounts;
	}

	public void setAccounts() {
		ArrayList<ClientAccount> deposiInAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : getCompany().getActiveAccounts()) {
			if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
				if (Arrays.asList(
						ClientAccount.TYPE_BANK,
						// ClientAccount.TYPE_CASH,
						ClientAccount.TYPE_CREDIT_CARD,
						ClientAccount.TYPE_OTHER_CURRENT_ASSET,
						ClientAccount.TYPE_FIXED_ASSET).contains(
						account.getType())) {

					deposiInAccounts.add(account);
				}
			} else {
				if (Arrays
						.asList(ClientAccount.TYPE_CREDIT_CARD,
								ClientAccount.TYPE_OTHER_CURRENT_ASSET,
								ClientAccount.TYPE_BANK,
								ClientAccount.TYPE_FIXED_ASSET).contains(
								account.getType())) {

					deposiInAccounts.add(account);
				}
			}

		}

		this.initCombo(deposiInAccounts);
	}

	private void setDefaultDepositInAccount() {
		/* Default deposit in account is set to Bank Current Account */
		List<ClientAccount> accounts = getCompany().getAccounts();
		for (ClientAccount account : accounts) {
			if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK
					&& account.getNumber().equals("1100")) {
				this.addItemThenfireEvent(account);
				break;
			} else if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US
					&& account.getNumber().equals("1175")) {
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
		action.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.DEPOSIT_IN_ACCOUNT));

		action.run(null, true);

	}

	@Override
	public void init() {
		super.init();
		setDefaultDepositInAccount();
	}
}
