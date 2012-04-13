package com.vimukti.accounter.web.client.ui.payroll;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.client.core.ClientComputationFormulaFunction;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.edittable.AbstractDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.ComboBox;
import com.vimukti.accounter.web.client.ui.edittable.ComboChangeHandler;
import com.vimukti.accounter.web.client.ui.edittable.EditColumn;
import com.vimukti.accounter.web.client.ui.edittable.RenderContext;

public class ComputationFormulaTypeColumn extends
		EditColumn<ClientComputationFormulaFunction> {

	PayHeadDropDownTable payheadDropdown = new PayHeadDropDownTable(false);

	AttendanceDropDownTable attendanceDropdown = new AttendanceDropDownTable();

	public ComputationFormulaTypeColumn() {
	}

	@Override
	public int getWidth() {
		return -1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void render(IsWidget widget,
			RenderContext<ClientComputationFormulaFunction> context) {
		HorizontalPanel panel = (HorizontalPanel) widget;

		ClientComputationFormulaFunction row = context.getRow();
		IAccounterCore value = getValue(row);

		ComboBox<ClientComputationFormulaFunction, ClientPayHead> payheadComboBox = (ComboBox<ClientComputationFormulaFunction, ClientPayHead>) panel
				.getWidget(0);

		ComboBox<ClientComputationFormulaFunction, ClientAttendanceOrProductionType> attendanceCombobox = (ComboBox<ClientComputationFormulaFunction, ClientAttendanceOrProductionType>) panel
				.getWidget(1);

		if (row.getFunctionType() == ClientComputationFormulaFunction.FUNCTION_ADD_PAY_HEAD
				|| row.getFunctionType() == ClientComputationFormulaFunction.FUNCTION_SUBSTRACT_PAY_HEAD) {
			payheadComboBox.setValue((ClientPayHead) value);
			payheadComboBox.setDesable(false);
			attendanceCombobox.setDesable(true);
		} else if (row.getFunctionType() == ClientComputationFormulaFunction.FUNCTION_MULTIPLY_ATTENDANCE
				|| row.getFunctionType() == ClientComputationFormulaFunction.FUNCTION_DIVIDE_ATTENDANCE) {
			attendanceCombobox
					.setValue((ClientAttendanceOrProductionType) value);
			payheadComboBox.setDesable(true);
			attendanceCombobox.setDesable(false);

		}
	}

	private IAccounterCore getValue(ClientComputationFormulaFunction row) {
		if (row.getFunctionType() == ClientComputationFormulaFunction.FUNCTION_ADD_PAY_HEAD
				|| row.getFunctionType() == ClientComputationFormulaFunction.FUNCTION_SUBSTRACT_PAY_HEAD) {
			return row.getPayHead();
		} else {
			return row.getAttendanceType();
		}
	}

	@Override
	public IsWidget getWidget(
			RenderContext<ClientComputationFormulaFunction> context) {
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		ComboBox<ClientComputationFormulaFunction, ClientPayHead> payheadComboBox = new ComboBox<ClientComputationFormulaFunction, ClientPayHead>();
		AbstractDropDownTable<ClientPayHead> payheadDisplayTable = payheadDropdown;
		payheadComboBox.setDropDown(payheadDisplayTable);
		payheadComboBox
				.setComboChangeHandler(new ComboChangeHandler<ClientComputationFormulaFunction, ClientPayHead>() {

					@Override
					public void onChange(ClientComputationFormulaFunction row,
							ClientPayHead newValue) {
						setValue(row, newValue);
					}

					@Override
					public void onAddNew(final String text) {

					}
				});
		payheadComboBox.setRow(context.getRow());

		ComboBox<ClientComputationFormulaFunction, ClientAttendanceOrProductionType> attendanceComboBox = new ComboBox<ClientComputationFormulaFunction, ClientAttendanceOrProductionType>();
		final AbstractDropDownTable<ClientAttendanceOrProductionType> attendanceDisplayTable = attendanceDropdown;
		attendanceComboBox.setDropDown(attendanceDisplayTable);
		attendanceComboBox
				.setComboChangeHandler(new ComboChangeHandler<ClientComputationFormulaFunction, ClientAttendanceOrProductionType>() {

					@Override
					public void onChange(ClientComputationFormulaFunction row,
							ClientAttendanceOrProductionType newValue) {
						setValue(row, newValue);
					}

					@Override
					public void onAddNew(final String text) {

					}
				});
		attendanceComboBox.setRow(context.getRow());

		horizontalPanel.add(payheadComboBox);
		horizontalPanel.add(attendanceComboBox);

		return horizontalPanel;
	}

	protected void setValue(ClientComputationFormulaFunction row,
			IAccounterCore newValue) {
		if (newValue instanceof ClientPayHead) {
			ClientPayHead payhead = (ClientPayHead) newValue;
			row.setPayHead(payhead);
		} else {
			ClientAttendanceOrProductionType attendanceType = (ClientAttendanceOrProductionType) newValue;
			row.setAttendanceType(attendanceType);
		}
	}

}
