package com.vimukti.accounter.web.client.ui;

import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class SalesTaxItemsGrid extends BaseListGrid<ClientTAXItem> {

	public SalesTaxItemsGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected void executeDelete(ClientTAXItem object) {
		// NOTHING TO DO.
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT };
	}

	@Override
	protected Object getColumnValue(ClientTAXItem obj, int index) {
		switch (index) {
		case 0:
			return obj.getName() != null ? obj.getName() : "";
		case 1:
			return obj.getTaxRate() + "%";
		}
		return null;
	}

	@Override
	public void onDoubleClick(ClientTAXItem obj) {
		// NOTHING TO DO.
	}

	@Override
	protected String[] getColumns() {
		return new String[] { Accounter.constants().name(),
				Accounter.constants().currentRate() };
	}


}
