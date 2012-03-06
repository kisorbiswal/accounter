package com.vimukti.accounter.web.client.ui.forms;

import com.vimukti.accounter.web.client.ui.DataUtils;

public class AmountLabel extends LabelItem {

	private Double doubleAmount;

	public AmountLabel(final String name) {
		super(name,"AmountLabel");
		setTitle(name);
		setAmount(0.00D);
	}

	public void setAmount(Double amount) {

		this.doubleAmount = amount;
		setValue(DataUtils.getAmountAsStringInCurrency(amount, null));

	}

	public Double getAmount() {
		return this.doubleAmount;
	}
}
