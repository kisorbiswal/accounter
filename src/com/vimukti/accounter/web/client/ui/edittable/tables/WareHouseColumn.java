package com.vimukti.accounter.web.client.ui.edittable.tables;

import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.edittable.AbstractDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.ComboColumn;

public abstract class WareHouseColumn extends
		ComboColumn<ClientTransactionItem, ClientWarehouse> {

	@Override
	public AbstractDropDownTable<ClientWarehouse> getDisplayTable(
			ClientTransactionItem row) {
		return null;
	}

	@Override
	protected ClientWarehouse getValue(ClientTransactionItem row) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setValue(ClientTransactionItem row, ClientWarehouse newValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getWidth() {
		return 150;
	}

	@Override
	protected String getColumnName() {
		return Accounter.constants().wareHouse();
	}
}
