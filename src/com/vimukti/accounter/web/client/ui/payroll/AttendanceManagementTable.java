package com.vimukti.accounter.web.client.ui.payroll;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAttendanceManagementItem;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionItem;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.ClientEmployeeGroup;
import com.vimukti.accounter.web.client.core.ClientPayStructureDestination;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.edittable.DeleteColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;

public class AttendanceManagementTable extends
		EditTable<ClientAttendanceManagementItem> {

	private EmployeeColumn employeeColumn;
	private List<ClientAttendanceManagementItem> items;

	public AttendanceManagementTable() {
		super();
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
		Accounter
				.createPayrollService()
				.getAttendanceProductionTypes(
						0,
						-1,
						new AsyncCallback<PaginationList<ClientAttendanceOrProductionType>>() {

							@Override
							public void onSuccess(
									PaginationList<ClientAttendanceOrProductionType> result) {
								AttendanceManagementTable.this
										.createAttendanceOrProductionTypeColumns(result);
								setAllRows(items);
							}

							@Override
							public void onFailure(Throwable caught) {

							}
						});

	}

	protected void createAttendanceOrProductionTypeColumns(
			PaginationList<ClientAttendanceOrProductionType> result) {

		employeeColumn = new EmployeeColumn() {

			@Override
			public int getWidth() {
				return 100;
			}

			@Override
			protected boolean isEnable() {
				return false;
			}
		};

		this.addColumn(employeeColumn);

		this.addColumn(new TextEditColumn<ClientAttendanceManagementItem>() {

			@Override
			protected String getValue(ClientAttendanceManagementItem row) {
				return UIUtils.toStr(row.getAbscentDays());
			}

			@Override
			protected void setValue(ClientAttendanceManagementItem row,
					String value) {
				row.setAbscentDays(UIUtils.toDbl(value));
				update(row);
			}

			@Override
			protected String getColumnName() {
				return messages.abscent();
			}

			@Override
			public int getWidth() {
				return 80;
			}
		});

		for (final ClientAttendanceOrProductionType clientAttendanceOrProductionType : result) {
			final ClientAttendanceOrProductionItem item = new ClientAttendanceOrProductionItem();
			item.setAttendanceOrProductionType(clientAttendanceOrProductionType);
			item.setValue(UIUtils.toDbl(0));
			this.addColumn(new TextEditColumn<ClientAttendanceManagementItem>() {

				@Override
				protected String getValue(ClientAttendanceManagementItem row) {
					List<ClientAttendanceOrProductionItem> attendanceOrProductionItems = row
							.getAttendanceOrProductionItems();
					for (ClientAttendanceOrProductionItem item : attendanceOrProductionItems) {
						if (item.getAttendanceOrProductionType().getID() == clientAttendanceOrProductionType
								.getID()) {
							return UIUtils.toStr(item.getValue());
						}
					}
					return UIUtils.toStr(item.getValue());
				}

				@Override
				protected void setValue(ClientAttendanceManagementItem row,
						String value) {
					for (ClientAttendanceOrProductionItem item : row
							.getAttendanceOrProductionItems()) {
						if (item.getAttendanceOrProductionType().getID() == clientAttendanceOrProductionType
								.getID()) {
							item.setValue(UIUtils.toDbl(value));
							update(row);
							return;
						}
					}
					item.setValue(UIUtils.toDbl(value));
					row.getAttendanceOrProductionItems().add(item);
					update(row);
				}

				@Override
				protected String getColumnName() {
					return clientAttendanceOrProductionType.getName();
				}

				@Override
				public int getWidth() {
					return 80;
				}
			});
		}

		this.addColumn(new DeleteColumn<ClientAttendanceManagementItem>());
	}

	@Override
	protected boolean isInViewMode() {
		return false;
	}

	public void updateList(ClientPayStructureDestination group) {
		employeeColumn.updateList(group);
	}

	public void setSelectedEmployeeOrGroup(
			ClientPayStructureDestination selectedValue) {
		clear();
		if (selectedValue instanceof ClientEmployee) {
			ClientAttendanceManagementItem item = new ClientAttendanceManagementItem();
			item.setEmployee((ClientEmployee) selectedValue);
			add(item);
		} else {
			ClientEmployeeGroup clientEmployeeGroup = (ClientEmployeeGroup) selectedValue;
			for (ClientEmployee employee : clientEmployeeGroup.getEmployees()) {
				ClientAttendanceManagementItem item = new ClientAttendanceManagementItem();
				item.setEmployee(employee);
				add(item);
			}
		}
	}

	public void setData(List<ClientAttendanceManagementItem> items) {
		this.items = items;
	}
}
