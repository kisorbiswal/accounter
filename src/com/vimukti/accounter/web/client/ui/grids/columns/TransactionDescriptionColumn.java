package com.vimukti.accounter.web.client.ui.grids.columns;

import com.google.gwt.cell.client.EditTextCell;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;

public class TransactionDescriptionColumn extends ColumnWithFieldUpdater<ClientTransactionItem, String> {

	public TransactionDescriptionColumn() {
		super(new EditTextCell());
	}

	@Override
	public void update(int index, ClientTransactionItem object,
			String value) {
		object.setDescription(value);
	}

	@Override
	public String getValue(ClientTransactionItem object) {
		return object.getDescription();
	}

}
