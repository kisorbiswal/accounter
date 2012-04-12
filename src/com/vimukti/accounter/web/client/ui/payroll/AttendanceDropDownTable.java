package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.cellview.client.TextColumn;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.client.ui.edittable.AbstractDropDownTable;

public class AttendanceDropDownTable extends
		AbstractDropDownTable<ClientAttendanceOrProductionType> {

	private List<ClientAttendanceOrProductionType> list;

	public AttendanceDropDownTable() {
		super(new ArrayList<ClientAttendanceOrProductionType>(), false);
	}

	public void setList(List<ClientAttendanceOrProductionType> list) {
		this.list = list;
	}

	@Override
	public List<ClientAttendanceOrProductionType> getTotalRowsData() {
		return list;
	}

	@Override
	protected ClientAttendanceOrProductionType getAddNewRow() {
		return null;
	}

	@Override
	public void initColumns() {
		TextColumn<ClientAttendanceOrProductionType> number = new TextColumn<ClientAttendanceOrProductionType>() {

			@Override
			public String getValue(ClientAttendanceOrProductionType row) {
				return row.getName();
			}
		};
		this.addColumn(number);
	}

	@Override
	protected boolean filter(ClientAttendanceOrProductionType t, String string) {
		if (t.getName() != null && t.getName().toLowerCase().startsWith(string)) {
			return true;
		}
		return false;
	}

	@Override
	protected String getDisplayValue(ClientAttendanceOrProductionType value) {
		return value.getName();
	}

	@Override
	protected void addNewItem(String text) {

	}

	@Override
	protected void addNewItem() {

	}

}
