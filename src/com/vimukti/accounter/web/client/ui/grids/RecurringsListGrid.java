package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientRecurringTransaction;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.customers.RecurringTransactionDialog;
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
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 1:
			return 130;
		case 2:
			return 130;
		case 3:
			return 130;
		case 4:
			return 130;
		case 5:
			return 150;
		case 6:
			return 130;

		}
		return -1;
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
		case 3: // prevScheduleOn
			return obj.getPrevScheduleOn() == 0 ? null : new ClientFinanceDate(
					obj.getPrevScheduleOn());
		case 4: // nextScheduleOn
			return obj.getNextScheduleOn() == 0 ? null : new ClientFinanceDate(
					obj.getNextScheduleOn());
		case 5: // occurrences completed
			return String.valueOf(obj.getOccurencesCompleted());
		case 6: // transaction amount
			return obj.getRefTransactionTotal();
		case 7: // delete image
			return Accounter.getFinanceMenuImages().delete();
		default:
			break;
		}
		return null;
	}

	@Override
	public void onDoubleClick(ClientRecurringTransaction obj) {

		ReportsRPC.openTransactionView(obj.getRefTransactionType(), obj
				.getReferringTransaction());

		// TODO need to open dialog also.
		RecurringTransactionDialog dialog = null;
		dialog = new RecurringTransactionDialog(obj);
		dialog.show();
	}

	@Override
	protected void onClick(ClientRecurringTransaction obj, int row, int col) {

		if (col == 7) { // click on delete
			executeDelete(obj);
			return;
		}
		super.onClick(obj, row, col);
	}

	@Override
	protected String[] getColumns() {
		final AccounterConstants CONST = Accounter.constants();
		return new String[] { CONST.name(), CONST.transactionType(),
				CONST.frequency(), CONST.prevScheduleOn(),
				CONST.nextScheduleOn(), CONST.occurrencesCompleted(),
				CONST.transactionAmount(), "" };
	}
}
