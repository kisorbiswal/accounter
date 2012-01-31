package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.user.client.ui.TextBoxBase;
import com.vimukti.accounter.web.client.core.ClientInventoryAssemblyItem;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;

public class InventoryTotalColumn extends
		AmountColumn<ClientInventoryAssemblyItem> {

	public InventoryTotalColumn(ICurrencyProvider currencyProvider) {
		super(currencyProvider, true);
	}

	@Override
	protected Double getAmount(ClientInventoryAssemblyItem row) {
		return row.getLineTotal();
	}

	@Override
	protected void setAmount(ClientInventoryAssemblyItem row, Double value) {
		row.setLineTotal(value);
	}

	@Override
	public int getWidth() {
		return 100;
	}

	@Override
	protected void configure(TextBoxBase textBox) {
		super.configure(textBox);
		textBox.addStyleName("total");
		textBox.setEnabled(false);
	}

	@Override
	protected String getColumnName() {
		return messages.total();
	}

	@Override
	protected boolean isEnable() {
		return false;
	}

}
