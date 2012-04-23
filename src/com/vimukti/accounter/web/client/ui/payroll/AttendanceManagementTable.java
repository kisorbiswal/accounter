package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
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

	private TextEditColumn<ClientAttendanceManagementItem> curBalColumn;

	public AttendanceManagementTable() {
		super();
		addEmptyRecords();
	}

	private void addEmptyRecords() {
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
		this.addColumn(new EmployeeColumn());

		this.addColumn(new AttendanceProductionTypeColumn() {
			@Override
			protected void setValue(ClientAttendanceManagementItem row,
					ClientAttendanceOrProductionType newValue) {
				super.setValue(row, newValue);
				updateRow(row);
			}
		});

		curBalColumn = new TextEditColumn<ClientAttendanceManagementItem>() {

			@Override
			protected String getValue(ClientAttendanceManagementItem row) {
				return UIUtils.toStr(row.getCurrBal());
			}

			@Override
			protected void setValue(ClientAttendanceManagementItem row,
					String value) {
				row.setCurrBal(UIUtils.toLong(value));
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			protected String getColumnName() {
				return messages.currentBalance();
			}
		};
		this.addColumn(curBalColumn);

		this.addColumn(new TextEditColumn<ClientAttendanceManagementItem>() {

			@Override
			protected String getValue(ClientAttendanceManagementItem row) {
				return UIUtils.toStr(row.getNumber());
			}

			@Override
			protected void setValue(ClientAttendanceManagementItem row,
					String value) {
				row.setCurrBal(row.getCurrBal() - row.getNumber());
				row.setNumber(UIUtils.toInt(value));
				row.setCurrBal(row.getCurrBal() + row.getNumber());
				update(row);
			}

			@Override
			protected String getColumnName() {
				return messages.value();
			}

			@Override
			public int getWidth() {
				return 50;
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

	protected void updateRow(final ClientAttendanceManagementItem row) {
		ClientEmployee employee = row.getEmployee();
		ClientAttendanceOrProductionType attendanceType = row
				.getAttendanceType();
		if (employee == null || attendanceType == null) {
			return;
		}
		Accounter.createPayrollService().getEmployeeAttendanceCurrentBal(
				employee.getID(), attendanceType.getID(),
				new AsyncCallback<Long>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(Long result) {
						row.setCurrBal(result);
						update(row);
					}
				});

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
