package com.vimukti.accounter.web.client.ui.vendors;

import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.PayeeList;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class WarehouseListGrid extends BaseListGrid<PayeeList> {

	public WarehouseListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected int getColumnType(int index) {
		return ListGrid.COLUMN_TYPE_TEXT;
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT };
	}

	@Override
	public boolean validateGrid() throws InvalidTransactionEntryException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { "Warehouse Name", "Item Name", "Available Items",
				"Total Price" };
	}

	@Override
	protected void executeDelete(PayeeList object) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object getColumnValue(PayeeList obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDoubleClick(PayeeList obj) {
		// TODO Auto-generated method stub

	}

}
