package com.vimukti.accounter.web.client.ui.settings;

import com.vimukti.accounter.web.client.core.ClientStockTransfer;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class WarehouseListGrid extends BaseListGrid<ClientStockTransfer> {

	public WarehouseListGrid(boolean isMultiSelectionEnable) {
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

		case 3:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXT;

		default:
			return 0;
		}
	}


	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT };
	}

	@Override
	protected String[] getColumns() {
		return new String[] { Accounter.constants().warehouseName(),
				Accounter.constants().itemName(),
				Accounter.constants().availableItems(),
				Accounter.constants().totalPrice() };
	}

	@Override
	protected void executeDelete(ClientStockTransfer object) {
		// currently not using

	}

	@Override
	protected Object getColumnValue(ClientStockTransfer obj, int index) {

		return null;
	}

	@Override
	public void onDoubleClick(ClientStockTransfer obj) {
		// currently not using

	}

}
