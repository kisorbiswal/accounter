package com.vimukti.accounter.web.client.ui.payroll;

import com.vimukti.accounter.web.client.core.ClientPayrollUnit;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class PayrollUnitListGrid extends BaseListGrid<ClientPayrollUnit> {

	public PayrollUnitListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_LINK,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected void executeDelete(ClientPayrollUnit object) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object getColumnValue(ClientPayrollUnit obj, int col) {
		switch (col) {
		case 0:
			return obj.getSymbol();
		case 1:
			return obj.getFormalname();
		case 2:
			return obj.getNoofDecimalPlaces();
		default:
			break;
		}
		return null;
	}

	@Override
	public void onDoubleClick(ClientPayrollUnit obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.symbol(), messages.formalName(),
				messages.noOfDecimalPlaces(), messages.delete() };
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { messages.symbol(), messages.formalName(),
				messages.noOfDecimalPlaces(), messages.delete() };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { messages.symbol(), messages.formalName(),
				messages.noOfDecimalPlaces(), messages.delete() };
	}

}
