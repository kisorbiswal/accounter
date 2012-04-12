package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
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
		this.selectedAttendanceOrProductionType = clientAttendanceOrProductionType;
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
		form.add(leaveType);
		daysTypeCombo = new SelectCombo(messages.type());
		daysTypeCombo.initCombo(getDaysTypeList());
		form.add(daysTypeCombo);
		payrollUnitCombo = new PayRollUnitCombo(messages.payrollUnitList(),
				"payrollUnitsListCombo");
		form.add(payrollUnitCombo);
		layout.add(form);
		setBodyLayout(layout);
		if (this.selectedAttendanceOrProductionType != null) {
			name.setValue(selectedAttendanceOrProductionType.getName());
			leaveType.setValue(attendanceOrProductionType
					.get(selectedAttendanceOrProductionType.getPeriodType()));
			daysTypeCombo.setValue(daysTypeList
					.get(selectedAttendanceOrProductionType.getType()));
			daysTypeCombo
					.setValue(selectedAttendanceOrProductionType.getUnit());

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
		ClientAttendanceOrProductionType clientAttendanceOrProductionType = null;
		if (selectedAttendanceOrProductionType != null) {
			clientAttendanceOrProductionType = selectedAttendanceOrProductionType;
			clientAttendanceOrProductionType.setName(name.getValue());
			clientAttendanceOrProductionType.setPeriodType(leaveType
					.getSelectedIndex());
			clientAttendanceOrProductionType.setType(daysTypeCombo
					.getSelectedIndex());
		} else {
			clientAttendanceOrProductionType = new ClientAttendanceOrProductionType();
			clientAttendanceOrProductionType.setName(name.getValue());
			clientAttendanceOrProductionType.setPeriodType(leaveType
					.getSelectedIndex());
			clientAttendanceOrProductionType.setType(daysTypeCombo
					.getSelectedIndex());

		}
		AccounterAsyncCallback<Long> callback = new AccounterAsyncCallback<Long>() {

			@Override
			public void onException(AccounterException exception) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onResultSuccess(Long result) {
				// TODO Auto-generated method stub

			}
		};
		Accounter.createCRUDService().create(clientAttendanceOrProductionType,
				callback);

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
