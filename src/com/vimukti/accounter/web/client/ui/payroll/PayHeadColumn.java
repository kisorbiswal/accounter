package com.vimukti.accounter.web.client.ui.payroll;

import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.ClientPayStructureItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.edittable.AbstractDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.ComboColumn;

public abstract class PayHeadColumn extends
		ComboColumn<ClientPayStructureItem, ClientPayHead> {
	PayHeadDropDownTable dropdown = new PayHeadDropDownTable(true);

	@Override
	protected ClientPayHead getValue(ClientPayStructureItem row) {
		return Accounter.getCompany().getPayHead(row.getPayHead());
	}

	@Override
	public AbstractDropDownTable<ClientPayHead> getDisplayTable(
			ClientPayStructureItem row) {
		return dropdown;
	}

	@Override
	public int getWidth() {
		return 100;
	}

	@Override
	protected String getColumnName() {
		return messages.payhead();
	}
}
