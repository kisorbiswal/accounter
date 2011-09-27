package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.user.client.ui.TextBox;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class TransactionUnitPriceColumn extends TransactionAmountColumn {

	@Override
	protected double getAmount(ClientTransactionItem row) {
		return row.getUnitPrice();
	}

	@Override
	protected void setAmount(ClientTransactionItem row, double value) {
		row.setUnitPrice(value);
		// TODO doubt, currencyConversion.
		double lt = row.getQuantity().getValue() * row.getUnitPrice();
		double disc = row.getDiscount();
		row.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt * disc / 100))
				: lt);
		getTable().update(row);
	}

	@Override
	public int getWidth() {
		return 80;
	}

	@Override
	protected void configure(TextBox textBox) {
		super.configure(textBox);
		textBox.addStyleName("unitPrice");
	}

	@Override
	protected String getColumnName() {
		return Accounter.constants().unitPrice();
	}
}
