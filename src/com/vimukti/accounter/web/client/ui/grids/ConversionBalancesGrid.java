package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;

public class ConversionBalancesGrid extends ListGrid {

	public ConversionBalancesGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected int getColumnType(int index) {
		switch (index) {
		case 0:
			return ListGrid.COLUMN_TYPE_TEXT;
		default:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		}
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	protected Object getColumnValue(Object obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String[] getSelectValues(Object obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isEditable(Object obj, int row, int index) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onClick(Object obj, int row, int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDoubleClick(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onValueChange(Object obj, int index, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int sort(Object obj1, Object obj2, int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean validateGrid() throws InvalidTransactionEntryException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected int getCellWidth(int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected String[] getColumns() {
		// TODO Auto-generated method stub
		return null;
	}

}
