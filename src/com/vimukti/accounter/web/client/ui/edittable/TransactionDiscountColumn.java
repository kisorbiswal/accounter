package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.user.client.ui.TextBox;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.Accounter;

public class TransactionDiscountColumn extends
		AmountColumn<ClientTransactionItem> {

	@Override
	protected double getAmount(ClientTransactionItem row) {
		return row.getDiscount();
	}

	@Override
	protected void setAmount(ClientTransactionItem row, double value) {
		row.setDiscount(value);
		getTable().update(row);
	}

	@Override
	public int getWidth() {
		return 40;
	}

	@Override
	protected void configure(TextBox textBox) {
		super.configure(textBox);
		textBox.addStyleName("Dis");
	}

	@Override
	protected String getColumnName() {
		return Accounter.constants().discountPerc();
	}
}
