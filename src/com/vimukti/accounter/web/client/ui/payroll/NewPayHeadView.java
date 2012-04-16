package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.client.core.ClientAttendancePayHead;
import com.vimukti.accounter.web.client.core.ClientComputaionFormulaFunction;
import com.vimukti.accounter.web.client.core.ClientComputationSlab;
import com.vimukti.accounter.web.client.core.ClientComputionPayHead;
import com.vimukti.accounter.web.client.core.ClientFlatRatePayHead;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.ClientProductionPayHead;
import com.vimukti.accounter.web.client.core.ClientUserDefinedPayHead;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.AttendanceOrProductionTypeCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.EditMode;
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
	String[] types = { messages.earningsForEmployees(),
			messages.deductionsForEmployees(),
			messages.employeesStatutoryDeductions(),
			messages.employeesStatutoryContributions(),
			messages.employeesOtherCharges(), messages.bonus(),
			messages.loansAndAdvances(), messages.reimbursmentsToEmployees() };

	String[] calType = { messages.attendance(), messages.asComputedValue(),
			messages.flatRate(), messages.production(),
			messages.asUserDefined() };

	String[] calPeriod = { messages.days(), messages.weeks(), messages.months() };

	String[] roundingMethods = { messages.downward(), messages.normal(),
			messages.upward() };

	String[] perDayCalculations = { messages.asPerCalendarPeriod(),
			messages.userDefined(), messages.userDefinedCalendar() };

	String[] computationTypes = { messages.onDeductionTotal(),
			messages.onEarningTotal(), messages.onSubTotal(),
			messages.onSpecifiedFormula() };

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

	private List<ClientComputaionFormulaFunction> formulas = new ArrayList<ClientComputaionFormulaFunction>();
	private AccountCombo accountCombo;

	private AddNewButton itemTableButton;
	private LabelItem formula;
	private DynamicForm mainform, leftForm, rightForm, attendanceLeftForm,
			attendanceRightForm;
	private DynamicForm compuPeriodAndTypeForm;
	private ComputationFormulaDialog dialog;

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
		typeCombo.setComboItem(ClientPayHead.getPayHeadType(data.getType()));
		affectNetSalarytem
				.setValue(data.isAffectNetSalary() ? "true" : "false");
		payslipNameItem.setValue(data.getNameToAppearInPaySlip());
		if (data.getRoundingMethod() != 0) {
			roundingMethodCombo.setComboItem(roundingList.get(data
					.getRoundingMethod() - 1));
		}
		accountCombo.setComboItem(data.getExpenseAccount());
		calculationTypeCombo.setComboItem(ClientPayHead.getCalculationType(data
				.getCalculationType()));
		if (data.getCalculationType() == 0) {
			return;
		}
		String calType = ClientPayHead.getCalculationType(data
				.getCalculationType());
		calculationTypeChanged(calType);
		if (calType.equals(messages.attendance())) {
			ClientAttendancePayHead attendance = (ClientAttendancePayHead) data;
			if (attendance.getLeaveWithPay() != 0) {
				List<ClientAttendanceOrProductionType> comboItems = leaveWithPayCombo
						.getComboItems();
				for (ClientAttendanceOrProductionType clientAttendanceOrProductionType : comboItems) {
					if (attendance.getLeaveWithPay() == clientAttendanceOrProductionType
							.getID()) {
						leaveWithPayCombo
								.setComboItem(clientAttendanceOrProductionType);
						break;
					}
				}
			}
			if (attendance.getLeaveWithoutPay() != 0) {
				List<ClientAttendanceOrProductionType> comboItems = leaveWithoutPayCombo
						.getComboItems();
				for (ClientAttendanceOrProductionType clientAttendanceOrProductionType : comboItems) {
					if (attendance.getLeaveWithoutPay() == clientAttendanceOrProductionType
							.getID()) {
						leaveWithoutPayCombo
								.setComboItem(clientAttendanceOrProductionType);
						break;
					}
				}
			}
			calculationPeriodCombo.setComboItem(getCalculationPeriod(attendance
					.getCalculationPeriod()));
			perdayCalculationCombo
					.setComboItem(getPerdayCalculationBasis(attendance
							.getPerDayCalculationBasis()));

		} else if (calType.equals(messages.asComputedValue())) {
			ClientComputionPayHead computation = (ClientComputionPayHead) data;

			calculationPeriodCombo
					.setComboItem(getCalculationPeriod(computation
							.getCalculationPeriod()));
			String compType = computationTypeList.get(computation
					.getComputationType() - 1);
			computationTypeCombo.setComboItem(compType);
			if (compType.equals(messages.onSpecifiedFormula())) {
				this.formulas = computation.getFormulaFunctions();
				prepareFormula(formulas);
			}
			slabTable.setAllRows(computation.getSlabs());

		} else if (calType.equals(messages.flatRate())) {
			ClientFlatRatePayHead flatrate = (ClientFlatRatePayHead) data;

			calculationPeriodCombo.setComboItem(getCalculationPeriod(flatrate
					.getCalculationPeriod()));

		} else if (calType.equals(messages.production())) {
			ClientProductionPayHead production = (ClientProductionPayHead) data;
			List<ClientAttendanceOrProductionType> comboItems = productionTypeCombo
					.getComboItems();
			for (ClientAttendanceOrProductionType clientAttendanceOrProductionType : comboItems) {
				if (production.getProductionType() == clientAttendanceOrProductionType
						.getID()) {
					productionTypeCombo
							.setComboItem(clientAttendanceOrProductionType);
				}
			}

		} else if (calType.equals(messages.asUserDefined())) {

		}
	}

	private String getPerdayCalculationBasis(int perDayCalculationBasis) {
		switch (perDayCalculationBasis) {
		case ClientAttendancePayHead.PER_DAY_CALCULATION_AS_PER_CALANDAR_PERIOD:
			return messages.asPerCalendarPeriod();

		case ClientAttendancePayHead.PER_DAY_CALCULATION_USER_DEFINED:
			return messages.userDefined();

		case ClientAttendancePayHead.PER_DAY_CALCULATION_USER_DEFINED_CALANDAR:
			return messages.userDefindCalendar();

		default:
			return null;
		}
	}

	private String getCalculationPeriod(int calculationPeriod) {
		switch (calculationPeriod) {
		case ClientPayHead.CALCULATION_PERIOD_DAYS:
			return messages.days();

		case ClientPayHead.CALCULATION_PERIOD_MONTHS:
			return messages.months();

		case ClientPayHead.CALCULATION_PERIOD_WEEKS:
			return messages.weeks();

		default:
			return null;
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
				slabTable.addRow(row);
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
			if (dialog == null) {
				dialog = new ComputationFormulaDialog("Computation Formula") {
					@Override
					protected boolean onOK() {

						return super.onOK();
					}
				};
				dialog.addCallback(new ValueCallBack<List<ClientComputaionFormulaFunction>>() {

					@Override
					public void execute(
							List<ClientComputaionFormulaFunction> value) {
						NewPayHeadView.this.formulas = value;
						prepareFormula(value);
					}
				});
			}
			if (!formulas.isEmpty()) {
				dialog.setData(formulas);
			}
			dialog.center();
			dialog.show();
		} else {
			dialog = null;
			formulaForm.clear();
		}
	}

	protected void prepareFormula(List<ClientComputaionFormulaFunction> formulas) {
		String string = new String();
		ClientComputaionFormulaFunction formulaFunction = formulas.get(0);
		string += formulaFunction.getPayHead() != null ? formulaFunction
				.getPayHead().getName() : formulaFunction.getAttendanceType()
				.getName();
		formulas.remove(0);
		for (ClientComputaionFormulaFunction function : formulas) {
			if (function.getFunctionType() == ClientComputaionFormulaFunction.FUNCTION_ADD_PAY_HEAD) {
				string = "(" + string + " + " + function.getPayHead().getName()
						+ ")";
			} else if (function.getFunctionType() == ClientComputaionFormulaFunction.FUNCTION_SUBSTRACT_PAY_HEAD) {
				string = "(" + string + " - " + function.getPayHead().getName()
						+ ")";
			} else if (function.getFunctionType() == ClientComputaionFormulaFunction.FUNCTION_MULTIPLY_ATTENDANCE) {
				string = "(" + string + " * "
						+ function.getAttendanceType().getName() + ")";
			} else if (function.getFunctionType() == ClientComputaionFormulaFunction.FUNCTION_DIVIDE_ATTENDANCE) {
				string = "(" + string + " / "
						+ function.getAttendanceType().getName() + ")";
			}
		}
		formula.setValue(string);
		formulaForm.add(formula);
	}

	protected void calculationTypeChanged(String selectItem) {
		calculationForm.clear();
		if (selectItem.equals(messages.attendance())) {
			attendanceForm = new DynamicForm("attendanceForm");

			attendanceLeftForm = new DynamicForm("attendanceLeftForm");
			attendanceRightForm = new DynamicForm("attendanceRightForm");

			attendanceLeftForm.add(leaveWithPayCombo, calculationPeriodCombo);
			attendanceRightForm.add(leaveWithoutPayCombo,
					perdayCalculationCombo);

			attendanceForm.add(attendanceLeftForm);
			attendanceForm.add(attendanceRightForm);

			calculationForm.add(attendanceForm);

		} else if (selectItem.equals(messages.asComputedValue())) {
			formulaForm = new DynamicForm("computationForm");
			computationForm = new DynamicForm("computationForm");
			compuPeriodAndTypeForm = new DynamicForm("compuPeriodAndTypeForm");
			compuPeriodAndTypeForm.add(calculationPeriodCombo,
					computationTypeCombo);
			computationForm.add(compuPeriodAndTypeForm);
			computationForm.add(formulaForm);
			computationForm.add(slabTable);
			computationForm.add(itemTableButton);
			calculationForm.add(computationForm);

		} else if (selectItem.equals(messages.flatRate())) {
			flatrateForm = new DynamicForm("flaterateForm");
			flatrateForm.add(calculationPeriodCombo);
			calculationForm.add(flatrateForm);

		} else if (selectItem.equals(messages.production())) {
			productionForm = new DynamicForm("productionForm");
			productionForm.add(productionTypeCombo);
			calculationForm.add(productionForm);

		} else if (selectItem.equals(messages.asUserDefined())) {

		}
	}

	@Override
	public void onEdit() {

		AsyncCallback<Boolean> editCallBack = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter.showMessage(messages.sessionExpired());
				} else {
					int errorCode = ((AccounterException) caught)
							.getErrorCode();
					Accounter.showError(AccounterExceptions
							.getErrorString(errorCode));

				}
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		AccounterCoreType type = data.getObjectType();
		this.rpcDoSerivce.canEdit(type, data.getID(), editCallBack);

	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);

		nameItem.setEnabled(!isInViewMode());
	}

	@Override
	public void saveAndUpdateView() {
		updateData();
		saveOrUpdate(getData());
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();
		result.add(leftForm.validate());
		result.add(rightForm.validate());

		if (result.haveErrors()) {
			return result;
		}

		String selectedValue = calculationTypeCombo.getSelectedValue();
		if (selectedValue.equals("Attendence")) {
			result.add(attendanceForm.validate());

		} else if (selectedValue.equals("As Computed Value")) {
			result.add(compuPeriodAndTypeForm.validate());
			result.add(slabTable.validate());
			if (result.haveErrors()) {
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
		if (selectedValue == null) {
			return;
		}
		if (selectedValue.equals(messages.attendance())) {
			ClientAttendancePayHead payhead = new ClientAttendancePayHead();
			if (leaveWithoutPayCombo.getSelectedValue() != null) {
				payhead.setLeaveWithoutPay(leaveWithoutPayCombo
						.getSelectedValue().getID());
			}
			if (leaveWithPayCombo.getSelectedValue() != null) {
				payhead.setLeaveWithPay(leaveWithPayCombo.getSelectedValue()
						.getID());
			}
			payhead.setID(data.getID());
			payhead.setCalculationPeriod(calculationPeriodCombo
					.getSelectedIndex() + 1);
			payhead.setPerDayCalculationBasis(perdayCalculationCombo
					.getSelectedIndex() + 1);
			data = payhead;

		} else if (selectedValue.equals(messages.asComputedValue())) {
			ClientComputionPayHead payhead = new ClientComputionPayHead();
			payhead.setID(data.getID());
			payhead.setCalculationPeriod(calculationPeriodCombo
					.getSelectedIndex() + 1);
			payhead.setComputationType(computationTypeCombo.getSelectedIndex() + 1);
			payhead.setFormulaFunctions(this.formulas);
			payhead.setSlabs(slabTable.getAllRows());
			data = payhead;

		} else if (selectedValue.equals(messages.flatRate())) {
			ClientFlatRatePayHead payHead = new ClientFlatRatePayHead();
			payHead.setID(data.getID());
			payHead.setCalculationPeriod(calculationPeriodCombo
					.getSelectedIndex() + 1);
			data = payHead;

		} else if (selectedValue.equals(messages.production())) {
			ClientProductionPayHead payHead = new ClientProductionPayHead();
			payHead.setProductionType(productionTypeCombo.getSelectedValue()
					.getID());
			payHead.setID(data.getID());
			data = payHead;

		} else if (selectedValue.equals(messages.asUserDefined())) {
			ClientUserDefinedPayHead payHead = new ClientUserDefinedPayHead();
			payHead.setID(data.getID());
			data = payHead;

		}

		data.setName(nameItem.getValue());
		data.setType(typeCombo.getSelectedIndex() + 1);
		data.setCalculationType(calculationTypeCombo.getSelectedIndex() + 1);
		data.setRoundingMethod(roundingMethodCombo.getSelectedIndex() + 1);
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
