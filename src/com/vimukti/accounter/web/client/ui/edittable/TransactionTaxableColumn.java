package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.edittable.tables.AbstractTransactionTable;

public class TransactionTaxableColumn extends
		CheckboxEditColumn<ClientTransactionItem> {

	@Override
	protected void onChangeValue(boolean value, ClientTransactionItem row) {
		row.setTaxable(value);
		((AbstractTransactionTable) getTable()).update(row);
	}

	@Override
	public void render(IsWidget widget,
			RenderContext<ClientTransactionItem> context) {
		super.render(widget, context);
		CheckBox box = (CheckBox) widget;
		box.setValue(context.getRow().isTaxable());
	}

	@Override
	public IsWidget getHeader() {
		return new Label(messages.taxable());
	}

	@Override
	public int getWidth() {
		return 46;
	}
}
