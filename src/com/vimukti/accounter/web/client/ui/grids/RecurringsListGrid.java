package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.core.ClientRecurringTransaction;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

public class RecurringsListGrid extends
		BaseListGrid<ClientRecurringTransaction> {

	public RecurringsListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT, ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected void executeDelete(ClientRecurringTransaction object) {		
		deleteObject(object);
	}

	@Override
	protected Object getColumnValue(ClientRecurringTransaction obj, int index) {
		switch (index) {
		case 0:// Name
			return obj.getName();
		case 1: // Transaction Type
			return Utility.getTransactionName(obj.getRefTransactionType());
		case 2: // Frequency
			return obj.getFrequencyString();
		case 3: // transaction amount
			return obj.getRefTransactionTotal();
		case 4: // delete image
			return Accounter.getFinanceMenuImages().delete();
		default:
			break;
		}
		return null;
	}

	@Override
	public void onDoubleClick(ClientRecurringTransaction obj) {
		
		ReportsRPC.openTransactionView(obj.getRefTransactionType(),
				obj.getReferringTransaction());

		//TODO need to open dialog also.
	}

	@Override
	protected void onClick(ClientRecurringTransaction obj, int row, int col) {
		
		if(col==4){ // click on delete
			executeDelete(obj);
			return;
		}		
		super.onClick(obj, row, col);
	}
	
	@Override
	protected String[] getColumns() {
		return new String[] { "Name", "Transaction Type", "Frequency",
				"Transaction Amount", "" };
	}
}
