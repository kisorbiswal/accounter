package com.vimukti.accounter.web.client.ui;

import com.vimukti.accounter.web.client.core.ClientActivity;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class RecentTransactionGrid extends ListGrid<ClientActivity> {

	public RecentTransactionGrid() {
		super(false);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int getColumnType(int index) {
		// TODO Auto-generated method stub
		return 0;
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

	@Override
	protected Object getColumnValue(ClientActivity obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String[] getSelectValues(ClientActivity obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onValueChange(ClientActivity obj, int index, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isEditable(ClientActivity obj, int row, int index) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onClick(ClientActivity obj, int row, int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDoubleClick(ClientActivity obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int sort(ClientActivity obj1, ClientActivity obj2, int index) {
		// TODO Auto-generated method stub
		return 0;
	}

}
