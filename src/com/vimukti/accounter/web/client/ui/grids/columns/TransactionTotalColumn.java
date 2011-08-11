package com.vimukti.accounter.web.client.ui.grids.columns;

import com.vimukti.accounter.web.client.core.ClientTransactionItem;

public class TransactionTotalColumn extends AmountColumn<ClientTransactionItem>{

	@Override
	public void update(int index, ClientTransactionItem object, Double value) {
		object.setLineTotal(value);
	}

	@Override
	public Double getValue(ClientTransactionItem object) {
		return object.getLineTotal();
	}

}
