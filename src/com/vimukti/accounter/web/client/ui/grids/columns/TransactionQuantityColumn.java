package com.vimukti.accounter.web.client.ui.grids.columns;

import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;

public class TransactionQuantityColumn extends QuantityColumn<ClientTransactionItem> {

	@Override
	public ClientQuantity getValue(ClientTransactionItem object) {
		return object.getQuantity();
	}

	@Override
	public void update(int index, ClientTransactionItem object,
			ClientQuantity value) {
		object.setQuantity(value);
	}

}
