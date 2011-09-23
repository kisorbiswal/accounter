package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.Accounter;

public class TransactionTaxableColumn extends
		CheckboxEditColumn<ClientTransactionItem> {

	@Override
	protected void onChangeValue(boolean value, ClientTransactionItem row) {
		row.setTaxable(value);
	}

	@Override
	public IsWidget getHeader() {
		return new Label(Accounter.constants().taxable());
	}
}
