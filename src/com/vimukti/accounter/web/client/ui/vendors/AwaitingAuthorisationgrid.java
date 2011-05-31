package com.vimukti.accounter.web.client.ui.vendors;

import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class AwaitingAuthorisationgrid extends BaseListGrid<BillsList> {
	AwaitingAuthorisationView view;

	public AwaitingAuthorisationgrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void executeDelete(BillsList object) {
		// TODO Auto-generated method stub

	}

	public void setView(AwaitingAuthorisationView view) {
		this.view = view;
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DATE,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT };
	}

	@Override
	protected Object getColumnValue(BillsList obj, int index) {
		switch (index) {

		case 0:
			return obj.getExpenseStatus();
		case 1:
			return obj.getVendorName();
		case 2:
			return obj.getDate().getTime();
		case 3:
			return obj.getOriginalAmount();
		}
		return null;
	}

	@Override
	public void onDoubleClick(BillsList obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean validateGrid() throws InvalidTransactionEntryException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { "Status", "Paid To", " Receipt Date", "Total" };
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

}
