package com.vimukti.accounter.web.client.ui.edittable;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;

public abstract class AccountNameColumn extends
		ComboColumn<ClientTransactionItem, ClientAccount> {

	AccountDropDownTable accountsList = new AccountDropDownTable(
			getAccountsFilter(), getCanAddedAccountTypes());

	@Override
	protected ClientAccount getValue(ClientTransactionItem row) {
		return (ClientAccount) row.getAccountable();
	}

	public abstract ListFilter<ClientAccount> getAccountsFilter();

	public abstract List<Integer> getCanAddedAccountTypes();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AbstractDropDownTable getDisplayTable(ClientTransactionItem row) {
		return accountsList;
	}

	@Override
	public int getWidth() {
		return 150;
	}

	@Override
	protected void setValue(ClientTransactionItem row, ClientAccount newValue) {
		row.setAccountable(newValue);
		if (newValue != null) {
			row.setDescription(newValue.getComment());
			row.setTaxable(true);
			onValueChange(row);
		}
	}

	@Override
	protected String getColumnName() {
		return messages.name();
	}

	@Override
	public boolean isPrimaryColumn() {
		return true;
	}
}
