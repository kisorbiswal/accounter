package com.vimukti.accounter.web.client.ui.edittable;

import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.Accounter;

public class TransactionVatColumn extends TransactionAmountColumn {

	@Override
	protected double getAmount(ClientTransactionItem row) {
		return row.getVATfraction();
	}

	@Override
	protected void setAmount(ClientTransactionItem row, double value) {
		row.setVATfraction(value);
	}

	@Override
	public int getWidth() {
		return 80;
	}

	@Override
	protected String getColumnName() {
		return Accounter.constants().vat();
	}

	@Override
	protected boolean isEnable() {
		return false;
	}
}
