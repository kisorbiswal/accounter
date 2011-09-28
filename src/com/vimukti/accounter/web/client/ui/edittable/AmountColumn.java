package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.vimukti.accounter.web.client.ui.DataUtils;

public abstract class AmountColumn<T> extends TextEditColumn<T> {
	@Override
	protected String getValue(T row) {
		return DataUtils.getAmountAsString(getAmount(row));
	}

	protected abstract double getAmount(T row);

	@Override
	public void setValue(T row, String value) {
		try {
			double amount = DataUtils.getAmountStringAsDouble(value);
			setAmount(row, amount);
		} catch (Exception e) {
			e.printStackTrace();
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
