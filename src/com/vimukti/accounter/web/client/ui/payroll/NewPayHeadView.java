package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.client.core.ClientAttendancePayhead;
import com.vimukti.accounter.web.client.core.ClientComputationFormulaFunction;
import com.vimukti.accounter.web.client.core.ClientComputationPayHead;
import com.vimukti.accounter.web.client.core.ClientFlatRatePayHead;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.ClientProductionPayHead;
import com.vimukti.accounter.web.client.core.ClientUserDefinedPayHead;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.AttendanceOrProductionTypeCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class NewPayHeadView extends BaseView<ClientPayHead> {

	private VerticalPanel panel;
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
	private DynamicForm calculationForm;
	private DynamicForm computationForm;

	private List<ClientComputationFormulaFunction> formulas = new ArrayList<ClientComputationFormulaFunction>();
	private AccountCombo accountCombo;

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
		// TODO Auto-generated method stub

	}

	protected void createControls() {
		panel = new VerticalPanel();

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

		typeCombo = new SelectCombo(messages.payHeadType(), false);
		typeCombo.initCombo(typeList);

		affectNetSalarytem = new RadioGroupItem(messages.affectNetSalary());
		affectNetSalarytem.setValueMap(messages.yes(), messages.no());
		affectNetSalarytem.setDefaultValue(messages.yes());

		payslipNameItem = new TextItem(messages.paySlipName(),
				"payslipNameItem");

		roundingMethodCombo = new SelectCombo(messages.roundingMethod(), false);
		roundingMethodCombo.initCombo(roundingList);

		accountCombo = new AccountCombo(messages.expenseAccount()) {

			@Override
			protected List<ClientAccount> getAccounts() {
				return getCompany().getActiveAccounts();
			}
		};

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

		productionTypeCombo = new AttendanceOrProductionTypeCombo(
				ClientAttendanceOrProductionType.TYPE_PRODUCTION,
				messages.productionType(), "productionTypeCombo");

		calculationPeriodCombo = new SelectCombo(messages.calculationPeriod(),
				false);
		calculationPeriodCombo.initCombo(calPeriodList);

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
						} else {
							leaveWithoutPayCombo.setEnabled(false);
						}
					}
				});

		leaveWithoutPayCombo = new AttendanceOrProductionTypeCombo(
				ClientAttendanceOrProductionType.TYPE_LEAVE_WITHOUT_PAY,
				messages.leaveWithoutPay(), "leaveWithoutPayCombo");
		leaveWithoutPayCombo.setEnabled(false);

		perdayCalculationCombo = new SelectCombo(
				messages.perDayCalculationBasis(), false);
		perdayCalculationCombo.initCombo(perDayCalcList);

		computationTypeCombo = new SelectCombo(messages.compute(), false);
		computationTypeCombo.initCombo(computationTypeList);
		computationTypeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						computationTypeChanged(selectItem);
					}
				});

		slabTable = new ComputationSlabTable();

		DynamicForm form = new DynamicForm("form");
		form.add(nameItem, typeCombo, affectNetSalarytem, payslipNameItem,
				roundingMethodCombo, accountCombo, calculationTypeCombo);

		calculationForm = new DynamicForm("calculationForm");

		panel.add(form);
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
					prepareFormula();
				}
			});
			dialog.center();
			dialog.show();
		}
	}

	protected void prepareFormula() {
		String string = new String();
		ClientComputationFormulaFunction formulaFunction = formulas.get(0);
		string += formulaFunction.getPayHead() != null ? formulaFunction
				.getPayHead().getName() : formulaFunction.getAttendanceType()
				.getName();
		formulas.remove(0);
		for (ClientComputationFormulaFunction function : formulas) {
			if (function.getFunctionType() == ClientComputationFormulaFunction.FUNCTION_ADD_PAY_HEAD) {
				string = "(" + string + "+" + function.getPayHead().getName()
						+ ")";
			} else if (function.getFunctionType() == ClientComputationFormulaFunction.FUNCTION_SUBSTRACT_PAY_HEAD) {
				string = "(" + string + "-" + function.getPayHead().getName()
						+ ")";
			} else if (function.getFunctionType() == ClientComputationFormulaFunction.FUNCTION_MULTIPLY_ATTENDANCE) {
				string = "(" + string + "*"
						+ function.getAttendanceType().getName() + ")";
			} else if (function.getFunctionType() == ClientComputationFormulaFunction.FUNCTION_DIVIDE_ATTENDANCE) {
				string = "(" + string + "/"
						+ function.getAttendanceType().getName() + ")";
			}
		}
		System.out.println("@@@@@ Formula -----" + string);
	}

	protected void calculationTypeChanged(String selectItem) {
		calculationForm.clear();
		if (selectItem.equals("Attendence")) {
			calculationForm.add(leaveWithPayCombo, leaveWithoutPayCombo,
					calculationPeriodCombo, perdayCalculationCombo);

		} else if (selectItem.equals("As Computed Value")) {
			computationForm = new DynamicForm("computationForm");
			calculationForm.add(calculationPeriodCombo, computationTypeCombo);
			calculationForm.add(computationForm);
			calculationForm.add(slabTable);

		} else if (selectItem.equals("Flat Rate")) {
			calculationForm.add(calculationPeriodCombo);

		} else if (selectItem.equals("Production")) {
			calculationForm.add(productionTypeCombo);

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
		return super.validate();
	}

	private void updateData() {
		ClientPayHead head = new ClientPayHead();

		String selectedValue = calculationTypeCombo.getSelectedValue();
		if (selectedValue.equals("Attendence")) {
			ClientAttendancePayhead payhead = new ClientAttendancePayhead();
			payhead.setLeaveWithoutPay(leaveWithoutPayCombo.getSelectedValue());
			payhead.setLeaveWithPay(leaveWithPayCombo.getSelectedValue());
			payhead.setCalculationPeriod(calculationPeriodCombo
					.getSelectedIndex());
			payhead.setPerDayCalculationBasis(perdayCalculationCombo
					.getSelectedIndex());
			head = payhead;

		} else if (selectedValue.equals("As Computed Value")) {
			ClientComputationPayHead payhead = new ClientComputationPayHead();

			payhead.setCalculationPeriod(calculationPeriodCombo
					.getSelectedIndex());
			payhead.setComputationType(computationTypeCombo.getSelectedIndex());
			payhead.setFormulaFunctions(this.formulas);
			payhead.setSlabs(slabTable.getAllRows());
			head = payhead;

		} else if (selectedValue.equals("Flat Rate")) {
			ClientFlatRatePayHead payHead = new ClientFlatRatePayHead();
			payHead.setCalculationPeriod(calculationPeriodCombo
					.getSelectedIndex());
			head = payHead;

		} else if (selectedValue.equals("Production")) {
			ClientProductionPayHead payHead = new ClientProductionPayHead();
			payHead.setProductionType(productionTypeCombo.getSelectedValue());
			head = payHead;

		} else if (selectedValue.equals("As User Defined")) {
			ClientUserDefinedPayHead payHead = new ClientUserDefinedPayHead();
			head = payHead;

		}

		head.setName(nameItem.getValue());
		head.setType(typeCombo.getSelectedIndex());
		head.setCalculationType(calculationTypeCombo.getSelectedIndex());
		head.setRoundingMethod(roundingMethodCombo.getSelectedIndex());
		head.setNameToAppearInPaySlip(payslipNameItem.getValue());
		head.setAffectNetSalary(affectNetSalarytem.getValue().endsWith("true"));
		head.setExpenseAccount(accountCombo.getSelectedValue());
		saveOrUpdate(head);
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
