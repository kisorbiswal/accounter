package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;

public abstract class AmountColumn<T> extends TextEditColumn<T> {
	@Override
	protected String getValue(T row) {
		return String.valueOf(getAmount(row));
	}

	protected abstract double getAmount(T row);

	@Override
	public void setValue(T row, String value) {
		try {
			if (value.isEmpty()) {
				value = "0";
			}
			setAmount(row, Double.valueOf(value));
		} catch (NumberFormatException e) {
		}
	}

	protected abstract void setAmount(T row, double value);

	@Override
	protected void configure(TextBox textBox) {
		super.configure(textBox);
		textBox.addStyleName("amount");
	}

	@Override
	public void setTable(EditTable<T> table) {
		super.setTable(table);
		FlexTable flexTable = (FlexTable) table.getWidget();
		flexTable.getCellFormatter().addStyleName(0,
				flexTable.getCellCount(0) - 1, "amount");
	}
}
