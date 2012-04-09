package com.vimukti.accounter.web.client.ui.payroll;

import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class EmployeeListGrid extends BaseListGrid<ClientEmployee> {

	public EmployeeListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_LINK, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected void executeDelete(ClientEmployee emp) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object getColumnValue(ClientEmployee emp, int col) {
		switch (col) {
		case 0:
			return emp.getNumber();
		case 1:
			return emp.getName();
		case 2:
			return emp.getDesignation();
		case 3:
			return emp.getLocation();
		case 4:
			return Accounter.getFinanceImages().delete();
		default:
			break;
		}
		return null;
	}

	@Override
	public void onDoubleClick(ClientEmployee emp) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.employeeID(), messages.employeeName(),
				messages.designation(), messages.location(), messages.delete() };
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { messages.employeeID(), messages.employeeName(),
				messages.designation(), messages.location(), messages.delete() };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { messages.employeeID(), messages.employeeName(),
				messages.designation(), messages.location(), messages.delete() };
	}
}
