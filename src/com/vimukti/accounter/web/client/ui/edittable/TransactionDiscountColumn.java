package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.user.client.ui.TextBox;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;

public class TransactionDiscountColumn extends
		AmountColumn<ClientTransactionItem> {

	public TransactionDiscountColumn(ICurrencyProvider currencyProvider) {
		super(currencyProvider);
	}

	@Override
	protected double getAmount(ClientTransactionItem row) {
		return row.getDiscount();
	}

	@Override
	protected void setAmount(ClientTransactionItem row, double value) {
		row.setDiscount(value);
		// TODO doubt, currencyConversion.
		double lt = row.getQuantity().getValue() * row.getUnitPrice();
		double disc = row.getDiscount();
		row.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt * disc / 100))
				: lt);
		getTable().update(row);
	}

	@Override
	public int getWidth() {
		return 45;
	}

	@Override
	protected void configure(TextBox textBox) {
		super.configure(textBox);
		textBox.addStyleName("discount");
	}

	@Override
	protected String getColumnName() {
		return Accounter.constants().discPerc();
	}
}
