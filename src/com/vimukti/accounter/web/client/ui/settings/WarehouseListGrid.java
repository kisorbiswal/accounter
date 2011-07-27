package com.vimukti.accounter.web.client.ui.settings;

import com.vimukti.accounter.core.StockTransfer;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class WarehouseListGrid extends BaseListGrid<StockTransfer> {

	public WarehouseListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected int getColumnType(int index) {
		return ListGrid.COLUMN_TYPE_TEXT;
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT };
	}

	@Override
	public boolean validateGrid() throws InvalidTransactionEntryException {
		// currently not using
		return false;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { "Warehouse Name", "Item Name", "Available Items",
				"Total Price" };
	}

	@Override
	protected void executeDelete(StockTransfer object) {
		// currently not using

	}

	@Override
	protected Object getColumnValue(StockTransfer obj, int index) {

		return null;
	}

	@Override
	public void onDoubleClick(StockTransfer obj) {
		// currently not using

	}

}
