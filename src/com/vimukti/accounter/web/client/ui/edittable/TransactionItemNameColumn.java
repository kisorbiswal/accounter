package com.vimukti.accounter.web.client.ui.edittable;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccountable;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.ui.Accounter;

public abstract class TransactionItemNameColumn extends
		ComboColumn<ClientTransactionItem, IAccountable> {

	AccountDropDownTable accountsList = new AccountDropDownTable(
			getAccountsFilter());

	// ItensDropDownTable productsList = new ItensDropDownTable(
	// new ListFilter<ClientItem>() {
	//
	// @Override
	// public boolean filter(ClientItem e) {
	// return e.getType() != ClientItem.TYPE_SERVICE;
	// }
	// });

	ItensDropDownTable itemsList = new ItensDropDownTable(
			new ListFilter<ClientItem>() {

				@Override
				public boolean filter(ClientItem e) {
					return true;
				}
			});

	@Override
	protected IAccountable getValue(ClientTransactionItem row) {
		return row.getAccountable();
	}

	public abstract ListFilter<ClientAccount> getAccountsFilter();

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public AbstractDropDownTable getDisplayTable(ClientTransactionItem row) {
		switch (row.getType()) {
		case ClientTransactionItem.TYPE_ACCOUNT:
			return accountsList;
		case ClientTransactionItem.TYPE_ITEM:
		case ClientTransactionItem.TYPE_SERVICE:
			return itemsList;
		default:
			break;
		}
		return null;
	}

	@Override
	public int getWidth() {
		return 150;
	}

	@Override
	protected String getColumnName() {
		return Accounter.constants().name();
	}
}
