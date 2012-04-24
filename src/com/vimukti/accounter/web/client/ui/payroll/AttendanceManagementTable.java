package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAttendanceManagementItem;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.edittable.DeleteColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;

public class AttendanceManagementTable extends
		EditTable<ClientAttendanceManagementItem> {

	public AttendanceManagementTable() {
		super();
		addEmptyRecords();
	}

	public void addEmptyRecords() {
		for (int i = 0; i < 4; i++) {
			addEmptyRowAtLast();
		}
	}

	@Override
	public void addEmptyRowAtLast() {
		ClientAttendanceManagementItem item = new ClientAttendanceManagementItem();
		add(item);
	}

	@Override
	protected void initColumns() {
		this.addColumn(new EmployeeColumn() {
			@Override
			protected void setValue(ClientAttendanceManagementItem row,
					ClientEmployee newValue) {
				super.setValue(row, newValue);
				row.setAttendanceType(null);
				row.setNumber(0);
				update(row);
			}

			@Override
			public int getWidth() {
				return 200;
			}
		});

		this.addColumn(new AttendanceProductionTypeColumn() {
			@Override
			protected void setValue(ClientAttendanceManagementItem row,
					ClientAttendanceOrProductionType newValue) {
				if (newValue != null
						&& AttendanceManagementTable.this
								.isExistsWithSameEmployee(row, newValue)) {
					super.setValue(row, null);
					Accounter
							.showError("The selected Attendance/Production item already existed in another item with same employee."
									+ "Please change the employee or remove previous attendance management item");
				} else {
					super.setValue(row, newValue);
				}
			}

			@Override
			public int getWidth() {
				return 200;
			}
		});

		this.addColumn(new TextEditColumn<ClientAttendanceManagementItem>() {

			@Override
			protected String getValue(ClientAttendanceManagementItem row) {
				return UIUtils.toStr(row.getNumber());
			}

			@Override
			protected void setValue(ClientAttendanceManagementItem row,
					String value) {
				row.setNumber(UIUtils.toInt(value));
				update(row);
			}

			@Override
			protected String getColumnName() {
				return messages.value();
			}

			@Override
			public int getWidth() {
				return 80;
			}
		});

		this.addColumn(new TextEditColumn<ClientAttendanceManagementItem>() {

			@Override
			protected String getValue(ClientAttendanceManagementItem row) {
				ClientAttendanceOrProductionType attendanceType = row
						.getAttendanceType();
				if (attendanceType != null) {
					return ClientPayHead.getCalculationPeriod(attendanceType
							.getPeriodType());
				}
				return null;
			}

			@Override
			protected void setValue(ClientAttendanceManagementItem row,
					String value) {
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			protected String getColumnName() {
				return messages.unit();
			}
		});

		this.addColumn(new DeleteColumn<ClientAttendanceManagementItem>());
	}

	protected boolean isExistsWithSameEmployee(
			ClientAttendanceManagementItem row,
			ClientAttendanceOrProductionType newValue) {
		if (row.getEmployee() != null) {
			List<ClientAttendanceManagementItem> allRows = getAllRows();
			for (ClientAttendanceManagementItem item : allRows) {
				if (item.getEmployee().getID() == row.getEmployee().getID()
						&& newValue.getID() == item.getAttendanceType().getID()) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected boolean isInViewMode() {
		return false;
	}

	@Override
	public List<ClientAttendanceManagementItem> getAllRows() {
		List<ClientAttendanceManagementItem> selected = new ArrayList<ClientAttendanceManagementItem>();
		List<ClientAttendanceManagementItem> allRows = super.getAllRows();
		for (ClientAttendanceManagementItem clientAttendanceManagementItem : allRows) {
			if (clientAttendanceManagementItem.isAllowed()) {
				selected.add(clientAttendanceManagementItem);
			}
		}
		return selected;
	}
}
