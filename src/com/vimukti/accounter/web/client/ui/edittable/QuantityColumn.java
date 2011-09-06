package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.user.client.ui.TextBox;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.ui.Accounter;

public abstract class QuantityColumn<T> extends TextEditColumn<T> {

	@Override
	protected String getValue(T row) {
		ClientQuantity value = getQuantity(row);
		return String.valueOf(value.getValue());
	}

	protected abstract ClientQuantity getQuantity(T row);

	@Override
	protected void setValue(T row, String value) {
		try {
			ClientQuantity quantity = getQuantity(row);
			quantity.setValue(Double.parseDouble(value));
			setQuantity(row, quantity);
		} catch (NumberFormatException e) {
		}
	}

	protected abstract void setQuantity(T row, ClientQuantity d);

	@Override
	public int getWidth() {
		return 50;
	}

	@Override
	protected String getColumnName() {
		return Accounter.constants().quantity();
	}

	@Override
	protected void configure(TextBox textBox) {
		super.configure(textBox);
		textBox.addStyleName("Qty");
	}
}
