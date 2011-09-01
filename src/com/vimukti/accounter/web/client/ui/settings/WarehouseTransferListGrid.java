package com.vimukti.accounter.web.client.ui.settings;

import com.vimukti.accounter.web.client.core.ClientStockTransfer;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class WarehouseTransferListGrid extends
		BaseListGrid<ClientStockTransfer> {

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
	protected String[] getColumns() {
		return new String[] { Accounter.constants().fromWarehouse(),
				Accounter.constants().toWarehouse(),
				Accounter.constants().itemStatus() };
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT };
	}

	@Override
	protected void executeDelete(ClientStockTransfer object) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object getColumnValue(ClientStockTransfer obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDoubleClick(ClientStockTransfer obj) {
		// TODO Auto-generated method stub

	}

}
