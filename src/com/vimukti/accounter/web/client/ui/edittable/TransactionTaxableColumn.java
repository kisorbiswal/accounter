package com.vimukti.accounter.web.client.ui.edittable;

import com.vimukti.accounter.web.client.core.ClientTransactionItem;

public class TransactionTaxableColumn extends
		CheckboxEditColumn<ClientTransactionItem> {

	@Override
	protected void onChangeValue(boolean value, ClientTransactionItem row) {
		row.setTaxable(value);
	}

}
