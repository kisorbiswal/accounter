package com.vimukti.accounter.web.client.ui.payroll;

import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class PayheadListGrid extends BaseListGrid<ClientPayHead> {

	public PayheadListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_LINK,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.name(), messages.paySlipName(),
				messages.calculationType(), messages.calculationPeriod(),
				messages.delete() };
	}

	@Override
	protected void executeDelete(ClientPayHead object) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object getColumnValue(ClientPayHead obj, int col) {
		switch (col) {
		case 0:
			return obj.getName();
		case 1:
			return obj.getNameToAppearInPaySlip();
		case 2:
			return obj.getCalculationType();
		case 3:
			// return obj.getCalculationPeriod();
		case 4:
			return Accounter.getFinanceImages().delete();
		default:
			break;
		}
		return null;
	}

	@Override
	public void onDoubleClick(ClientPayHead obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { messages.name(), messages.paySlipName(),
				messages.calculationType(), messages.calculationPeriod(),
				messages.delete() };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { messages.name(), messages.paySlipName(),
				messages.calculationType(), messages.calculationPeriod(),
				messages.delete() };
	}

}
