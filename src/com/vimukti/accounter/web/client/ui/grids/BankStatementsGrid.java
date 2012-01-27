package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.core.ClientStatement;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class BankStatementsGrid extends BaseListGrid<ClientStatement> {

	public BankStatementsGrid() {
		super(false, true);
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DATE, ListGrid.COLUMN_TYPE_DATE,
				ListGrid.COLUMN_TYPE_DATE, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT };
	}

	@Override
	protected void executeDelete(ClientStatement object) {

	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 15;
		case 1:
			return 55;
		case 2:
			return 55;
		case 3:
			return 55;
		case 4:
			return 55;
		case 5:
			return 55;
		}
		return 0;
	}

	@Override
	protected Object getColumnValue(ClientStatement obj, int index) {
		switch (index) {
		case 0:
			return String.valueOf(obj.getID());
		case 1:
			return obj.getImporttedDate();
		case 2:
			return obj.getStartDate();
		case 3:
			return obj.getEndDate();
		case 4:
			return DataUtils.amountAsStringWithCurrency(obj.getStartBalance(),
					getCompany().getPrimaryCurrency());
		case 5:
			return DataUtils.amountAsStringWithCurrency(
					obj.getClosingBalance(), getCompany().getPrimaryCurrency());
		}
		return null;
	}

	@Override
	public void onDoubleClick(ClientStatement obj) {
		if (obj.isReconciled()) {
			Accounter
					.showInformation("This statement is already reconciled.For details go to reconcilation history");
		} else {
			UIUtils.runAction(
					null,
					ActionFactory.getStatementReconcilationAction(
							obj.getAccount(), obj));
		}

	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.no(), "Imported Date",
				messages.startDate(), messages.endDate(), "Starting Balance",
				messages.ClosingBalance() };
	}
}
