package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class AccountTransactionItem {

	long id;
	Transaction transaction;
	Account account;
	double amount;

	public AccountTransactionItem(Account account, Transaction transaction,
			double amount) {

		this.account = account;
		this.transaction = transaction;
		this.amount = amount;

	}

	public boolean equals(AccountTransactionItem at) {

		if (this.id == at.id && DecimalUtil.isEquals(this.amount, at.amount)
				&& this.transaction.equals(at.transaction)
				&& this.account.equals(at.account))
			return true;
		return false;

	}

}
