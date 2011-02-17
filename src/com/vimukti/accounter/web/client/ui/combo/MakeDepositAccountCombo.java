package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.company.NewAccountAction;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;

public class MakeDepositAccountCombo extends AccountCombo {

	private List<ClientAccount> deposiInAccounts;

	public MakeDepositAccountCombo(String title) {
		super(title);
	}

	@Override
	public List<ClientAccount> getAccounts() {
		deposiInAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : FinanceApplication.getCompany()
				.getActiveAccounts()) {
			if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
				if (Arrays.asList(ClientAccount.TYPE_OTHER_CURRENT_ASSET,
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
		for (ClientAccount account : FinanceApplication.getCompany()
				.getActiveAccounts()) {
			if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
				if (Arrays.asList(ClientAccount.TYPE_OTHER_CURRENT_ASSET,
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

		this.initCombo(deposiInAccounts);
		setDefaultDepositInAccount();

	}

	private void setDefaultDepositInAccount() {
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
	public SelectItemType getSelectItemType() {
		return SelectItemType.ACCOUNT;
	}

	@Override
	public void onAddNew() {
		NewAccountAction action = CompanyActionFactory.getNewAccountAction();
		action.setActionSource(this);
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			action.setAccountTypes(Arrays.asList(
					ClientAccount.TYPE_OTHER_CURRENT_ASSET,
					ClientAccount.TYPE_OTHER_CURRENT_LIABILITY,
					ClientAccount.TYPE_EQUITY));
		} else {
			action.setAccountTypes(Arrays.asList(
					ClientAccount.TYPE_OTHER_CURRENT_ASSET,
					ClientAccount.TYPE_OTHER_CURRENT_LIABILITY,
					ClientAccount.TYPE_BANK, ClientAccount.TYPE_EQUITY));
		}

		action.run(null, true);

	}

	@Override
	public void addItemThenfireEvent(ClientAccount obj) {
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			if (Arrays.asList(ClientAccount.TYPE_OTHER_CURRENT_ASSET,
					ClientAccount.TYPE_OTHER_CURRENT_LIABILITY,
					ClientAccount.TYPE_EQUITY).contains(obj.getType())) {
				super.addItemThenfireEvent(obj);
			}

		} else {
			if (Arrays.asList(ClientAccount.TYPE_OTHER_CURRENT_ASSET,
					ClientAccount.TYPE_OTHER_CURRENT_LIABILITY,
					ClientAccount.TYPE_BANK, ClientAccount.TYPE_EQUITY)
					.contains(obj.getType())) {
				super.addItemThenfireEvent(obj);
			}

		}

	}

}
