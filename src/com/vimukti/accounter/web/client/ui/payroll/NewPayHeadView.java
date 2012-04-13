package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.client.core.ClientAttendancePayhead;
import com.vimukti.accounter.web.client.core.ClientComputationFormulaFunction;
import com.vimukti.accounter.web.client.core.ClientComputationPayHead;
import com.vimukti.accounter.web.client.core.ClientComputationSlab;
import com.vimukti.accounter.web.client.core.ClientFlatRatePayHead;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.ClientProductionPayHead;
import com.vimukti.accounter.web.client.core.ClientUserDefinedPayHead;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.AttendanceOrProductionTypeCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class NewPayHeadView extends BaseView<ClientPayHead> {

	private StyledPanel panel;
	private TextItem nameItem, payslipNameItem;
	private SelectCombo typeCombo, calculationTypeCombo,
			calculationPeriodCombo, roundingMethodCombo;
	RadioGroupItem affectNetSalarytem;
	String[] types = { "Earnings for Employees", "Deductions for Employees",
			"Employees Statutory Deductions",
			"Employees Statutory Contributions", "Employees Other Charges",
			"Bonus", "Gratuity", "Loans and Advances",
			"Reimbursements to Employees" };

	String[] calType = { "Attendence", "As Computed Value", "Flat Rate",
			"Production", "As User Defined" };

	String[] calPeriod = { messages.days(), messages.weeks(), messages.months() };

	String[] roundingMethods = { "Downword", "Normal", "Upword" };

	String[] perDayCalculations = { "As Per Calendar Period", "User Defined",
			"User Defined Calendar" };

	String[] computationTypes = { "On Deduction Total", "On Earning Total",
			"On Subtotal", "On Specified Formula" };

	List<String> typeList = new ArrayList<String>();
	List<String> calTypeList = new ArrayList<String>();
	List<String> calPeriodList = new ArrayList<String>();
	List<String> roundingList = new ArrayList<String>();
	private List<String> perDayCalcList = new ArrayList<String>();
	private List<String> computationTypeList = new ArrayList<String>();
	private AttendanceOrProductionTypeCombo productionTypeCombo;
	private AttendanceOrProductionTypeCombo leaveWithPayCombo;
	private AttendanceOrProductionTypeCombo leaveWithoutPayCombo;
	private SelectCombo perdayCalculationCombo;
	private SelectCombo computationTypeCombo;
	private ComputationSlabTable slabTable;
	private DynamicForm formulaForm, computationForm, calculationForm,
			attendanceForm, flatrateForm, productionForm;

	private List<ClientComputationFormulaFunction> formulas = new ArrayList<ClientComputationFormulaFunction>();
	private AccountCombo accountCombo;

	private AddNewButton itemTableButton;
	private LabelItem formula;
	private DynamicForm mainform, leftForm, rightForm, attendanceLeftForm,
			attendanceRightForm;

	public NewPayHeadView() {
		this.getElement().setId("NewPayHeadView");
	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	@Override
	public void initData() {
		if (getData() == null) {
			setData(new ClientPayHead());
		} else {
			initViewData(getData());
		}
		super.initData();
	}

	private void initViewData(ClientPayHead data) {
		if (data.getID() == 0) {
			return;
		}
		nameItem.setValue(data.getName());
		typeCombo.setValue(data.getType());
		affectNetSalarytem
				.setValue(data.isAffectNetSalary() ? "true" : "false");
		payslipNameItem.setValue(data.getNameToAppearInPaySlip());
		roundingMethodCombo.setValue(data.getRoundingMethod());
		accountCombo.setValue(data.getExpenseAccount());
		calculationTypeCombo.setValue(data.getCalculationType());
		if (data.getCalculationType() == 0) {
			return;
		}
		String calType = calTypeList.get(data.getCalculationType() - 1);
		calculationTypeChanged(calType);
		if (calType.equals("Attendence")) {
			ClientAttendancePayhead attendance = (ClientAttendancePayhead) data;

			leaveWithPayCombo.setValue(attendance.getLeaveWithPay());
			leaveWithoutPayCombo.setValue(attendance.getLeaveWithoutPay());
			calculationPeriodCombo.setValue(attendance.getCalculationPeriod());
			perdayCalculationCombo.setValue(attendance
					.getPerDayCalculationBasis());

		} else if (calType.equals("As Computed Value")) {
			ClientComputationPayHead computation = (ClientComputationPayHead) data;

			calculationPeriodCombo.setValue(computation.getCalculationPeriod());
			String compType = computationTypeList.get(computation
					.getComputationType());
			computationTypeCombo.setValue(compType);
			computationTypeChanged(compType);
			if (compType.equals("On Specified Formula")) {
				this.formulas = computation.getFormulaFunctions();
				prepareFormula(formulas);
			}
			slabTable.setAllRows(computation.getSlabs());

		} else if (calType.equals("Flat Rate")) {
			ClientFlatRatePayHead flatrate = (ClientFlatRatePayHead) data;

			calculationPeriodCombo.setValue(flatrate.getCalculationPeriod());

		} else if (calType.equals("Production")) {
			ClientProductionPayHead production = (ClientProductionPayHead) data;

			productionTypeCombo.setValue(production.getProductionType());

		} else if (calType.equals("As User Defined")) {

		}
	}

	protected void createControls() {
		panel = new StyledPanel("panel");

		for (int i = 0; i < types.length; i++) {
			typeList.add(types[i]);
		}

		for (int i = 0; i < calType.length; i++) {
			calTypeList.add(calType[i]);
		}

		for (int i = 0; i < calPeriod.length; i++) {
			calPeriodList.add(calPeriod[i]);
		}

		for (int i = 0; i < roundingMethods.length; i++) {
			roundingList.add(roundingMethods[i]);
		}

		for (int i = 0; i < perDayCalculations.length; i++) {
			perDayCalcList.add(perDayCalculations[i]);
		}

		for (int i = 0; i < computationTypes.length; i++) {
			computationTypeList.add(computationTypes[i]);
		}

		nameItem = new TextItem(messages.name(), "nameItem");
		nameItem.setRequired(true);
		nameItem.setEnabled(!isInViewMode());

		typeCombo = new SelectCombo(messages.payHeadType(), false);
		typeCombo.initCombo(typeList);
		typeCombo.setRequired(true);
		typeCombo.setEnabled(!isInViewMode());

		affectNetSalarytem = new RadioGroupItem(messages.affectNetSalary());
		affectNetSalarytem.setValueMap(messages.yes(), messages.no());
		affectNetSalarytem.setDefaultValue(messages.yes());
		affectNetSalarytem.setEnabled(!isInViewMode());

		payslipNameItem = new TextItem(messages.paySlipName(),
				"payslipNameItem");
		payslipNameItem.setEnabled(!isInViewMode());

		roundingMethodCombo = new SelectCombo(messages.roundingMethod(), false);
		roundingMethodCombo.initCombo(roundingList);
		roundingMethodCombo.setEnabled(!isInViewMode());

		accountCombo = new AccountCombo(messages.expenseAccount()) {

			@Override
			protected List<ClientAccount> getAccounts() {
				return getCompany().getActiveAccounts();
			}
		};
		accountCombo.setEnabled(!isInViewMode());
		accountCombo.setRequired(true);

		calculationTypeCombo = new SelectCombo(messages.calculationType(),
				false);
		calculationTypeCombo.initCombo(calTypeList);
		calculationTypeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						calculationTypeChanged(selectItem);
					}
				});
		calculationTypeCombo.setRequired(true);
		calculationTypeCombo.setEnabled(!isInViewMode());

		productionTypeCombo = new AttendanceOrProductionTypeCombo(
				ClientAttendanceOrProductionType.TYPE_PRODUCTION,
				messages.productionType(), "productionTypeCombo");
		productionTypeCombo.setRequired(true);
		productionTypeCombo.setEnabled(!isInViewMode());

		calculationPeriodCombo = new SelectCombo(messages.calculationPeriod(),
				false);
		calculationPeriodCombo.initCombo(calPeriodList);
		calculationPeriodCombo.setEnabled(!isInViewMode());
		calculationPeriodCombo.setRequired(true);

		leaveWithPayCombo = new AttendanceOrProductionTypeCombo(
				ClientAttendanceOrProductionType.TYPE_LEAVE_WITH_PAY,
				messages.leaveWithPay(), "leaveWithPayCombo");
		leaveWithPayCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAttendanceOrProductionType>() {

					@Override
					public void selectedComboBoxItem(
							ClientAttendanceOrProductionType selectItem) {
						if (selectItem.getName().equals("Not Applicable")) {
							leaveWithoutPayCombo.setEnabled(true);
							leaveWithoutPayCombo.setRequired(true);
						} else {
							leaveWithoutPayCombo.setEnabled(false);
							leaveWithoutPayCombo.setRequired(false);
						}
					}
				});
		leaveWithPayCombo.setEnabled(!isInViewMode());
		leaveWithPayCombo.setRequired(true);

		leaveWithoutPayCombo = new AttendanceOrProductionTypeCombo(
				ClientAttendanceOrProductionType.TYPE_LEAVE_WITHOUT_PAY,
				messages.leaveWithoutPay(), "leaveWithoutPayCombo");
		leaveWithoutPayCombo.setEnabled(false);

		perdayCalculationCombo = new SelectCombo(
				messages.perDayCalculationBasis(), false);
		perdayCalculationCombo.initCombo(perDayCalcList);
		perdayCalculationCombo.setEnabled(!isInViewMode());
		perdayCalculationCombo.setRequired(true);

		computationTypeCombo = new SelectCombo(messages.compute(), false);
		computationTypeCombo.initCombo(computationTypeList);
		computationTypeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						computationTypeChanged(selectItem);
					}
				});
		computationTypeCombo.setEnabled(!isInViewMode());
		computationTypeCombo.setRequired(true);

		slabTable = new ComputationSlabTable();
		slabTable.setEnabled(!isInViewMode());

		itemTableButton = new AddNewButton();
		itemTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ClientComputationSlab row = new ClientComputationSlab();
				slabTable.add(row);
			}
		});
		itemTableButton.setEnabled(!isInViewMode());

		formula = new LabelItem(messages.specifiedFormula(), "formula");

		leftForm = new DynamicForm("leftForm");
		rightForm = new DynamicForm("rightForm");
		mainform = new DynamicForm("form");

		leftForm.add(nameItem, typeCombo, affectNetSalarytem,
				calculationTypeCombo);
		rightForm.add(payslipNameItem, roundingMethodCombo, accountCombo);

		mainform.add(leftForm);
		mainform.add(rightForm);

		calculationForm = new DynamicForm("calculationForm");

		panel.add(mainform);
		panel.add(calculationForm);
		this.add(panel);

		setSize("100%", "100%");
	}

	protected void computationTypeChanged(String selectItem) {
		if (selectItem.equals("On Specified Formula")) {
			ComputationFormulaDialog dialog = new ComputationFormulaDialog(
					"Computation Formula") {
				@Override
				protected boolean onOK() {

					return super.onOK();
				}
			};
			dialog.addCallback(new ValueCallBack<List<ClientComputationFormulaFunction>>() {

				@Override
				public void execute(List<ClientComputationFormulaFunction> value) {
					NewPayHeadView.this.formulas = value;
					prepareFormula(value);
				}
			});
			dialog.center();
			dialog.show();
		} else {
			formulaForm.clear();
		}
	}

	protected void prepareFormula(
			List<ClientComputationFormulaFunction> formulas) {
		String string = new String();
		ClientComputationFormulaFunction formulaFunction = formulas.get(0);
		string += formulaFunction.getPayHead() != null ? formulaFunction
				.getPayHead().getName() : formulaFunction.getAttendanceType()
				.getName();
		formulas.remove(0);
		for (ClientComputationFormulaFunction function : formulas) {
			if (function.getFunctionType() == ClientComputationFormulaFunction.FUNCTION_ADD_PAY_HEAD) {
				string = "(" + string + " + " + function.getPayHead().getName()
						+ ")";
			} else if (function.getFunctionType() == ClientComputationFormulaFunction.FUNCTION_SUBSTRACT_PAY_HEAD) {
				string = "(" + string + " - " + function.getPayHead().getName()
						+ ")";
			} else if (function.getFunctionType() == ClientComputationFormulaFunction.FUNCTION_MULTIPLY_ATTENDANCE) {
				string = "(" + string + " * "
						+ function.getAttendanceType().getName() + ")";
			} else if (function.getFunctionType() == ClientComputationFormulaFunction.FUNCTION_DIVIDE_ATTENDANCE) {
				string = "(" + string + " / "
						+ function.getAttendanceType().getName() + ")";
			}
		}
		formula.setValue(string);
		formulaForm.add(formula);
	}

	protected void calculationTypeChanged(String selectItem) {
		calculationForm.clear();
		if (selectItem.equals("Attendence")) {
			attendanceForm = new DynamicForm("attendanceForm");

			attendanceLeftForm = new DynamicForm("attendanceLeftForm");
			attendanceRightForm = new DynamicForm("attendanceRightForm");

			attendanceLeftForm.add(leaveWithPayCombo, calculationPeriodCombo);
			attendanceRightForm.add(leaveWithoutPayCombo,
					perdayCalculationCombo);

			attendanceForm.add(attendanceLeftForm);
			attendanceForm.add(attendanceRightForm);

			calculationForm.add(attendanceForm);

		} else if (selectItem.equals("As Computed Value")) {
			formulaForm = new DynamicForm("computationForm");
			computationForm = new DynamicForm("computationForm");
			computationForm.add(calculationPeriodCombo, computationTypeCombo);
			computationForm.add(formulaForm);
			computationForm.add(slabTable);
			computationForm.add(itemTableButton);
			calculationForm.add(computationForm);

		} else if (selectItem.equals("Flat Rate")) {
			flatrateForm = new DynamicForm("flaterateForm");
			flatrateForm.add(calculationPeriodCombo);
			calculationForm.add(flatrateForm);

		} else if (selectItem.equals("Production")) {
			productionForm = new DynamicForm("productionForm");
			productionForm.add(productionTypeCombo);
			calculationForm.add(productionForm);

		} else if (selectItem.equals("As User Defined")) {

		}
	}

	@Override
	public void saveAndUpdateView() {
		updateData();
		saveOrUpdate(getData());
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();
		result.add(mainform.validate());
		if (result.haveErrors()) {
			return result;
		}
		String selectedValue = calculationTypeCombo.getSelectedValue();
		if (selectedValue.equals("Attendence")) {
			result.add(attendanceForm.validate());

		} else if (selectedValue.equals("As Computed Value")) {
			result.add(computationForm.validate());
			if (result.haveErrors()) {
				return result;
			}
			if (slabTable.getRows().isEmpty()) {
				result.addError(slabTable, "");
				return result;
			}
		} else if (selectedValue.equals("Flat Rate")) {
			result.add(flatrateForm.validate());

		} else if (selectedValue.equals("Production")) {
			result.add(productionForm.validate());

		}
		return result;
	}

	private void updateData() {

		String selectedValue = calculationTypeCombo.getSelectedValue();
		if (selectedValue.equals("Attendence")) {
			ClientAttendancePayhead payhead = new ClientAttendancePayhead();
			payhead.setLeaveWithoutPay(leaveWithoutPayCombo.getSelectedValue());
			payhead.setLeaveWithPay(leaveWithPayCombo.getSelectedValue());
			payhead.setCalculationPeriod(calculationPeriodCombo
					.getSelectedIndex());
			payhead.setPerDayCalculationBasis(perdayCalculationCombo
					.getSelectedIndex());
			data = payhead;

		} else if (selectedValue.equals("As Computed Value")) {
			ClientComputationPayHead payhead = new ClientComputationPayHead();

			payhead.setCalculationPeriod(calculationPeriodCombo
					.getSelectedIndex());
			payhead.setComputationType(computationTypeCombo.getSelectedIndex());
			payhead.setFormulaFunctions(this.formulas);
			payhead.setSlabs(slabTable.getAllRows());
			data = payhead;

		} else if (selectedValue.equals("Flat Rate")) {
			ClientFlatRatePayHead payHead = new ClientFlatRatePayHead();
			payHead.setCalculationPeriod(calculationPeriodCombo
					.getSelectedIndex());
			data = payHead;

		} else if (selectedValue.equals("Production")) {
			ClientProductionPayHead payHead = new ClientProductionPayHead();
			payHead.setProductionType(productionTypeCombo.getSelectedValue());
			data = payHead;

		} else if (selectedValue.equals("As User Defined")) {
			ClientUserDefinedPayHead payHead = new ClientUserDefinedPayHead();
			data = payHead;

		}

		data.setName(nameItem.getValue());
		data.setType(typeCombo.getSelectedIndex());
		data.setCalculationType(calculationTypeCombo.getSelectedIndex());
		data.setRoundingMethod(roundingMethodCombo.getSelectedIndex());
		data.setNameToAppearInPaySlip(payslipNameItem.getValue());
		data.setAffectNetSalary(affectNetSalarytem.getValue().endsWith("true"));
		data.setExpenseAccount(accountCombo.getSelectedValue());
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return messages.newPayHead();
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

}
