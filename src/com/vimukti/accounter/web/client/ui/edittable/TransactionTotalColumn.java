package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.user.client.ui.TextBox;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;

public class TransactionTotalColumn extends AmountColumn<ClientTransactionItem> {

	public TransactionTotalColumn(ICurrencyProvider currencyProvider) {
		super(currencyProvider);
	}

	@Override
	protected double getAmount(ClientTransactionItem row) {

		return row.getLineTotal();
	}

	@Override
	protected void setAmount(ClientTransactionItem row, double value) {
		row.setLineTotal(value);
	}

	@Override
	public int getWidth() {
		return 100;
	}

	@Override
	protected void configure(TextBox textBox) {
		super.configure(textBox);
		textBox.addStyleName("total");
		textBox.setEnabled(false);
	}

	@Override
	protected String getColumnName() {
		return Accounter.constants().total();
	}

	@Override
	protected boolean isEnable() {
		return false;
	}
}
