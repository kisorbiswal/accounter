package com.vimukti.accounter.web.client.ui;

import com.vimukti.accounter.web.client.core.ClientActivity;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

public class RecentTransactionHistoryGrid extends ListGrid<ClientActivity> {

	public RecentTransactionHistoryGrid() {
		super(false);
	}

	@Override
	public void init() {
		super.init();
		this.header.getElement().getParentElement().getParentElement()
				.addClassName("dashboard_grid_header");
	}

	@Override
	protected int getColumnType(int index) {
		switch (index) {
		case 1:
			return ListGrid.COLUMN_TYPE_LINK;
		case 2:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXT;
		default:
			return ListGrid.COLUMN_TYPE_LABEL;
		}
	}

	@Override
	protected Object getColumnValue(ClientActivity obj, int index) {
		switch (index) {
		case 1:
			return obj.getDataType();
		case 2:
			return DataUtils.amountAsStringWithCurrency(obj.getAmount(),
					obj.getCurrency());
		case 0:
			return obj.getDate();
		case 3:
			return obj.getUserName();
		default:
			break;
		}
		return null;
	}

	@Override
	protected String[] getSelectValues(ClientActivity obj, int index) {
		return null;
	}

	@Override
	protected void onValueChange(ClientActivity obj, int index, Object value) {

	}

	@Override
	protected boolean isEditable(ClientActivity obj, int row, int index) {
		return false;
	}

	@Override
	protected void onClick(ClientActivity obj, int row, int index) {
		ReportsRPC.openTransactionView(obj.getObjType(), obj.getObjectID());
	}

	@Override
	public void onDoubleClick(ClientActivity obj) {
		ReportsRPC.openTransactionView(obj.getObjType(), obj.getObjectID());
	}

	@Override
	protected int sort(ClientActivity obj1, ClientActivity obj2, int index) {
		return 0;
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 60;
		case 3:
			return 100;
		default:
			break;
		}
		return 0;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { "", "", "", "" };
	}

}
