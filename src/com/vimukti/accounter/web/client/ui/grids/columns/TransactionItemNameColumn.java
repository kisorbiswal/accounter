package com.vimukti.accounter.web.client.ui.grids.columns;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccountable;

public class TransactionItemNameColumn extends
		Column<ClientTransactionItem, IAccountable> implements
		FieldUpdater<ClientTransactionItem, IAccountable> {

	public TransactionItemNameColumn() {
		super(new ComboCell());
		this.setSortable(true);
		this.setFieldUpdater(this);
	}

	@Override
	public IAccountable getValue(ClientTransactionItem object) {
		return object.getAccountable();
	}

	@Override
	public void update(int index, ClientTransactionItem object,
			IAccountable value) {
		object.setAccountable(value);
	}

}
