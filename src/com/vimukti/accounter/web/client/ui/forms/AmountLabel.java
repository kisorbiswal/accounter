package com.vimukti.accounter.web.client.ui.forms;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.ui.DataUtils;

public class AmountLabel extends LabelItem {

	private Double doubleAmount;

	public AmountLabel(final String name) {

		setName(name);
		setTitle(name);
		setWidth("180px");

		// Set Default Values
		setAmount(0.00D);
		// setWidth("0px");
		((Label) getMainWidget())
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	}

	public void setAmount(Double amount) {

		this.doubleAmount = amount;
		setValue(DataUtils.getAmountAsString(amount));

	}

	public Double getAmount() {
		return this.doubleAmount;
	}
}
