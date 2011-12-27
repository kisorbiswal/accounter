package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.user.client.ui.TextBoxBase;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;

public class TransactionUnitPriceColumn extends TransactionAmountColumn {

	public TransactionUnitPriceColumn(ICurrencyProvider currencyProvider) {
		super(currencyProvider, true);
	}

	@Override
	protected Double getAmount(ClientTransactionItem row) {
		return row.getUnitPrice();
	}

	@Override
	protected void setAmount(ClientTransactionItem row, Double value) {

		double amount = 0;
		if (value != null) {
			amount = value / currencyProvider.getCurrencyFactor();
			row.setUnitPrice(amount);
		}
		if (row.getQuantity() != null && row.getUnitPrice() != null
				&& row.getDiscount() != null) {
			double lt = row.getQuantity().getValue() * row.getUnitPrice();
			double disc = row.getDiscount();
			row.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
					* disc / 100)) : lt);
		}
		getTable().update(row);
	}

	@Override
	public int getWidth() {
		return 80;
	}

	@Override
	protected void configure(TextBoxBase textBox) {
		super.configure(textBox);
		textBox.addStyleName("unitPrice");
	}

	@Override
	protected String getColumnName() {
		return getColumnNameWithCurrency(Accounter.messages().unitPrice());
	}
}
