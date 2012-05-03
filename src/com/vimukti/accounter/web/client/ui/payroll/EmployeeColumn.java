package com.vimukti.accounter.web.client.ui.payroll;

import com.vimukti.accounter.web.client.core.ClientAttendanceManagementItem;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.ClientPayStructureDestination;
import com.vimukti.accounter.web.client.ui.edittable.AbstractDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.ComboColumn;

public class EmployeeColumn extends
		ComboColumn<ClientAttendanceManagementItem, ClientEmployee> {

	EmployeeDropDownTable dropdown = new EmployeeDropDownTable(null);

	@Override
	protected ClientEmployee getValue(ClientAttendanceManagementItem row) {
		return dropdown.getEmployee(row.getEmployee());
	}

	@Override
	protected void setValue(ClientAttendanceManagementItem row,
			ClientEmployee newValue) {
		row.setEmployee(newValue.getID());
	}

	@Override
	public AbstractDropDownTable<ClientEmployee> getDisplayTable(
			ClientAttendanceManagementItem row) {
		return dropdown;
	}

	@Override
	public int getWidth() {
		return 120;
	}

	@Override
	protected String getColumnName() {
		return messages.employee();
	}

	public void updateList(ClientPayStructureDestination group) {
		dropdown.initList(group);
	}
}
