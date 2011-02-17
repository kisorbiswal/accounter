package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.company.NewAccountAction;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;

public class DepositInAccountCombo extends AccountCombo {

	private List<ClientAccount> deposiInAccounts;

	public DepositInAccountCombo(String title) {
		super(title);
	}

	public List<ClientAccount> getAccounts() {
		deposiInAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : FinanceApplication.getCompany()
				.getActiveAccounts()) {
			if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
				if (Arrays.asList(
						// ClientAccount.TYPE_BANK, ClientAccount.TYPE_CASH,
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
		deposiInAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : FinanceApplication.getCompany()
				.getActiveAccounts()) {
			if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
				if (Arrays.asList(
						// ClientAccount.TYPE_BANK, ClientAccount.TYPE_CASH,
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
		List<ClientAccount> accounts = FinanceApplication.getCompany()
				.getAccounts();
		for (ClientAccount account : accounts) {
			if (account.getNumber().equals("1100")) {
				this.addItemThenfireEvent(account);
				break;
			}
		}

	}

	@Override
	public void onAddNew() {
		NewAccountAction action = CompanyActionFactory.getNewAccountAction();
		action.setActionSource(this);
		action.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.DEPOSIT_IN_ACCOUNT));
		action.run(null, true);

	}

	@Override
	public SelectItemType getSelectItemType() {
		return SelectItemType.ACCOUNT;
	}

	@Override
	public void init() {
		super.init();
		setDefaultDepositInAccount();
	}
}
