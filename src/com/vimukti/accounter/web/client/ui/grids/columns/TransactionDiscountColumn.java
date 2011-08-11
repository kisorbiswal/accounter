package com.vimukti.accounter.web.client.ui.grids.columns;

import com.vimukti.accounter.web.client.core.ClientTransactionItem;

public class TransactionDiscountColumn extends AmountColumn<ClientTransactionItem>{

	@Override
	public void update(int index, ClientTransactionItem object, Double value) {
		object.setDiscount(value);
	}

	@Override
	public Double getValue(ClientTransactionItem object) {
		return object.getDiscount();
	}

}
