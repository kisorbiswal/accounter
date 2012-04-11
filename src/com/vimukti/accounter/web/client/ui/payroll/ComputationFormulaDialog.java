package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientComputationFormulaFunction;
import com.vimukti.accounter.web.client.ui.combo.AttendanceOrProductionTypeCombo;
import com.vimukti.accounter.web.client.ui.combo.PayheadCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class ComputationFormulaDialog extends
		BaseDialog<ClientComputationFormulaFunction> {

	private DynamicForm mainForm;
	private List<String> payheadFunctionTypeList = new ArrayList<String>();
	private List<String> attendanceFunctionTypeList = new ArrayList<String>();
	String[] payheadFunctionTypes = { "Add Pay Head", "Subtract Pay Head" };
	String[] attendanceFuntionTypes = { "Divide Attendance",
			"Multiply Attendance" };
	private SelectCombo payheadFunctionTypeCombo;
	private PayheadCombo payheadCombo;
	private List<SelectCombo> attendanceFunctionTypeCombos = new ArrayList<SelectCombo>();
	private List<SelectCombo> payheadFunctionTypeCombos = new ArrayList<SelectCombo>();
	private List<PayheadCombo> payheadCombos = new ArrayList<PayheadCombo>();
	private List<AttendanceOrProductionTypeCombo> attendanceCombos = new ArrayList<AttendanceOrProductionTypeCombo>();
	private AttendanceOrProductionTypeCombo attendanceCombo;
	private SelectCombo attendanceFunctionTypeCombo;

	private ValueCallBack<List<ClientComputationFormulaFunction>> successCallback;

	public ComputationFormulaDialog(String string) {
		super(string);
		createControls();
	}

	public void createControls() {
		for (int i = 0; i < payheadFunctionTypes.length; i++) {
			payheadFunctionTypeList.add(payheadFunctionTypes[i]);
		}

		for (int i = 0; i < attendanceFuntionTypes.length; i++) {
			attendanceFunctionTypeList.add(attendanceFuntionTypes[i]);
		}

		mainForm = new DynamicForm("mainForm");

		addPayHeadForm();
		addAttendanceForm();

		HorizontalPanel panel = new HorizontalPanel();

		Button payheadButton = new Button("Pay Head");
		payheadButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addPayHeadForm();
			}
		});

		Button attendanceButton = new Button("Attendance");
		attendanceButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addAttendanceForm();
			}
		});

		panel.add(payheadButton);
		panel.add(attendanceButton);

		bodyLayout.add(mainForm);
		bodyLayout.add(panel);
	}

	private void addAttendanceForm() {
		final DynamicForm attendanceForm = new DynamicForm("attendanceForm");

		attendanceFunctionTypeCombo = new SelectCombo(messages.functionType());
		attendanceFunctionTypeCombo.initCombo(attendanceFunctionTypeList);

		attendanceCombo = new AttendanceOrProductionTypeCombo(100,
				messages.attendanceOrProductionType(), "attendanceCombo");

		Button deleteButton = new Button(messages.delete());
		deleteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				mainForm.remove(attendanceForm);
			}
		});

		attendanceFunctionTypeCombos.add(attendanceFunctionTypeCombo);
		attendanceCombos.add(attendanceCombo);

		attendanceForm.add(attendanceFunctionTypeCombo, attendanceCombo);
		attendanceForm.add(deleteButton);

		mainForm.add(attendanceForm);
	}

	private void addPayHeadForm() {
		final DynamicForm payheadForm = new DynamicForm("payheadForm");

		payheadFunctionTypeCombo = new SelectCombo(messages.functionType());
		payheadFunctionTypeCombo.initCombo(payheadFunctionTypeList);

		payheadCombo = new PayheadCombo(messages.payhead());

		Button deleteButton = new Button(messages.delete());
		deleteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				mainForm.remove(payheadForm);
			}
		});

		payheadFunctionTypeCombos.add(payheadFunctionTypeCombo);
		payheadCombos.add(payheadCombo);

		payheadForm.add(payheadFunctionTypeCombo, payheadCombo);
		payheadForm.add(deleteButton);

		mainForm.add(payheadForm);
	}

	@Override
	protected boolean onOK() {
		if (successCallback != null) {
			successCallback.execute(createFunctions());
		}
		return false;
	}

	public void addCallback(
			ValueCallBack<List<ClientComputationFormulaFunction>> successCallback) {
		this.successCallback = successCallback;
	}

	private List<ClientComputationFormulaFunction> createFunctions() {
		List<ClientComputationFormulaFunction> functions = new ArrayList<ClientComputationFormulaFunction>();
		for (int i = 0; i < attendanceCombos.size(); i++) {
			AttendanceOrProductionTypeCombo attendanceCombo = attendanceCombos
					.get(i);
			SelectCombo selectCombo = attendanceFunctionTypeCombos.get(i);

			ClientComputationFormulaFunction function = new ClientComputationFormulaFunction();
			function.setFunctionType(getFunctionType(selectCombo
					.getSelectedValue()));
			function.setAttendanceType(attendanceCombo.getSelectedValue());
			functions.add(function);
		}

		for (int i = 0; i < payheadCombos.size(); i++) {
			PayheadCombo payheadCombo = payheadCombos.get(i);
			SelectCombo selectCombo = payheadFunctionTypeCombos.get(i);

			ClientComputationFormulaFunction function = new ClientComputationFormulaFunction();
			function.setFunctionType(getFunctionType(selectCombo
					.getSelectedValue()));
			function.setPayHead(payheadCombo.getSelectedValue());
			functions.add(function);
		}
		return functions;
	}

	private int getFunctionType(String selectedValue) {
		if (selectedValue.equals("Add Pay Head")) {
			return ClientComputationFormulaFunction.FUNCTION_ADD_PAY_HEAD;
		} else if (selectedValue.equals("Subtract Pay Head")) {
			return ClientComputationFormulaFunction.FUNCTION_SUBSTRACT_PAY_HEAD;
		} else if (selectedValue.equals("Divide Attendance")) {
			return ClientComputationFormulaFunction.FUNCTION_DIVIDE_ATTENDANCE;
		} else if (selectedValue.equals("Multiply Attendance")) {
			return ClientComputationFormulaFunction.FUNCTION_MULTIPLY_ATTENDANCE;
		}
		return 0;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
