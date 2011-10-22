package com.vimukti.accounter.web.client.ui;

import com.vimukti.accounter.web.client.core.ClientActivity;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class TransactionHistoryGrid extends ListGrid<ClientActivity> {

	private AccounterConstants constants = Accounter.constants();

	public TransactionHistoryGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);

	}

	public TransactionHistoryGrid() {
		super(false);
	}

	@Override
	protected int getColumnType(int index) {
		return COLUMN_TYPE_LABEL;
	}

	@Override
	protected Object getColumnValue(ClientActivity obj, int index) {
		switch (index) {
		case 1:
			obj.getActivityType();
		case 2:
			obj.getDate();
		case 3:
			obj.getUserName();
		default:
			obj.getDataType();
		}
		return null;
	}

	@Override
	protected String[] getSelectValues(ClientActivity obj, int index) {
		return null;
	}

	@Override
	protected boolean isEditable(ClientActivity obj, int row, int index) {
		return false;
	}

	@Override
	protected void onClick(ClientActivity obj, int row, int index) {

	}

	@Override
	public void onDoubleClick(ClientActivity obj) {

	}

	@Override
	protected void onValueChange(ClientActivity obj, int index, Object value) {

	}

	@Override
	protected int sort(ClientActivity obj1, ClientActivity obj2, int index) {
		return 0;
	}

	@Override
	protected int getCellWidth(int index) {
		return -1;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { constants.changes(), constants.date(),
				constants.user(), constants.details() };
	}

}
