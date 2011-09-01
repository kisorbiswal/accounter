package com.vimukti.accounter.web.client.ui;

import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class TaxGroupGrid extends BaseListGrid<ClientTAXGroup> {

	public TaxGroupGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected void executeDelete(ClientTAXGroup object) {

	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT };
	}

	@Override
	protected Object getColumnValue(ClientTAXGroup obj, int index) {
		return obj.getName() != null ? obj.getName() : "";
	}

	@Override
	public void onDoubleClick(ClientTAXGroup obj) {

	}

	@Override
	protected void onClick(ClientTAXGroup obj, int row, int col) {
		if (recordClickHandler != null)
			recordClickHandler.onRecordClick(obj, col);
	}

	@Override
	protected String[] getColumns() {
		return new String[] { Accounter.constants().name() };
	}

}
