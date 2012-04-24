package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.client.core.ClientPayrollUnit;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class NewClientAttendanceOrProductionDialog extends
		BaseDialog<ClientAttendanceOrProductionType> {

	private DynamicForm form;
	private TextItem name;
	private ClientAttendanceOrProductionType selectedAttendanceOrProductionType;
	private SelectCombo leaveType;
	private SelectCombo daysTypeCombo;
	private PayRollUnitCombo payrollUnitCombo;
	private List<String> attendanceOrProductionType, daysTypeList;

	public NewClientAttendanceOrProductionDialog(String text,
			ClientAttendanceOrProductionType clientAttendanceOrProductionType) {
		super(text);
		this.getElement().setId("NewClientAttendanceOrProductionDialog");
		this.selectedAttendanceOrProductionType = clientAttendanceOrProductionType;
		if (this.selectedAttendanceOrProductionType == null) {
			selectedAttendanceOrProductionType = new ClientAttendanceOrProductionType();
		}
		createControl();
		center();
	}

	private void createControl() {
		form = new DynamicForm("newClientAttendanceForm");
		name = new TextItem(messages.name(), "attendanceName");
		name.setRequired(true);

		StyledPanel layout = new StyledPanel("layout");
		form.add(name);

		leaveType = new SelectCombo(messages.attendanceOrProductionType());
		leaveType.initCombo(getAttendanceTypeLIst());
		leaveType.setRequired(true);
		form.add(leaveType);

		daysTypeCombo = new SelectCombo(messages.type());
		daysTypeCombo.initCombo(getDaysTypeList());
		daysTypeCombo.setRequired(true);
		form.add(daysTypeCombo);

		payrollUnitCombo = new PayRollUnitCombo(messages.payrollUnitList(),
				"payrollUnitsListCombo",
				selectedAttendanceOrProductionType.getUnit());
		form.add(payrollUnitCombo);

		layout.add(form);
		setBodyLayout(layout);
		if (this.selectedAttendanceOrProductionType.getID() != 0) {
			name.setValue(selectedAttendanceOrProductionType.getName());
			leaveType.setValue(attendanceOrProductionType
					.get(selectedAttendanceOrProductionType.getPeriodType()));
			daysTypeCombo.setValue(daysTypeList
					.get(selectedAttendanceOrProductionType.getType()));
		}
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = form.validate();
		return result;
	}

	private List<String> getDaysTypeList() {
		daysTypeList = new ArrayList<String>();
		daysTypeList.add(messages.days());
		daysTypeList.add(messages.weeks());
		daysTypeList.add(messages.months());
		return daysTypeList;
	}

	private List<String> getAttendanceTypeLIst() {
		attendanceOrProductionType = new ArrayList<String>();
		attendanceOrProductionType.add(messages.leaveWithPay());
		attendanceOrProductionType.add(messages.leaveWithoutPay());
		attendanceOrProductionType.add(messages.productionType());
		attendanceOrProductionType.add(messages.userDefindCalendar());
		return attendanceOrProductionType;
	}

	@Override
	protected boolean onOK() {
		createorUpdateObject();
		return true;
	}

	private void createorUpdateObject() {
		selectedAttendanceOrProductionType.setName(name.getValue());
		selectedAttendanceOrProductionType
				.setType(leaveType.getSelectedIndex() + 1);
		selectedAttendanceOrProductionType.setPeriodType(daysTypeCombo
				.getSelectedIndex() + 1);
		ClientPayrollUnit selectedValue = payrollUnitCombo.getSelectedValue();
		if (selectedValue != null) {
			selectedAttendanceOrProductionType.setUnit(selectedValue.getID());
		}
		AccounterAsyncCallback<Long> callback = new AccounterAsyncCallback<Long>() {

			@Override
			public void onException(AccounterException exception) {
			}

			@Override
			public void onResultSuccess(Long result) {
				selectedAttendanceOrProductionType.setID(result);
				getCallback().actionResult(selectedAttendanceOrProductionType);
			}
		};
		Accounter.createCRUDService().create(
				selectedAttendanceOrProductionType, callback);

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean onCancel() {
		return true;
	}

}
