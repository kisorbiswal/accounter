package com.vimukti.accounter.web.client.ui.settings;

import com.vimukti.accounter.core.StockTransfer;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class WarehouseTransferListGrid extends BaseListGrid<StockTransfer> {

	public WarehouseTransferListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected int getColumnType(int index) {
		switch (index) {
		case 0:
			return ListGrid.COLUMN_TYPE_TEXT;

		case 1:
			return ListGrid.COLUMN_TYPE_TEXT;

		case 2:
			return ListGrid.COLUMN_TYPE_TEXT;

		default:
			return 0;
		}
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean validateGrid() throws InvalidTransactionEntryException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { "From Warehouse", "To Warehouse", "Item Status" };
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT };
	}

	@Override
	protected void executeDelete(StockTransfer object) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object getColumnValue(StockTransfer obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDoubleClick(StockTransfer obj) {
		// TODO Auto-generated method stub

	}

}
