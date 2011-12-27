package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientBudgetItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class AddBudgetAmountDialogue extends BaseDialog {

	private static final String MONTHS = Accounter.messages().months();
	private static final String QUARTERS = Accounter.messages().Quarters();
	private static final String YEARS = Accounter.messages().years();

	private static final String NEAREST_AMOUNT = Accounter.messages().NearestAmount();
	private static final String NEAREST_TEN = Accounter.messages().NearestTen();
	private static final String NEAREST_HUNDRED = Accounter.messages().NearestHundred();
	private static final String NONE = Accounter.messages().none();

	IntegerField janAmount;
	IntegerField febAmount;
	IntegerField marAmount;
	IntegerField aprAmount;
	IntegerField mayAmount;
	IntegerField junAmount;
	IntegerField julAmount;
	IntegerField augAmount;
	IntegerField septAmount;
	IntegerField octAmount;
	IntegerField novAmount;
	IntegerField decAmount;

	IntegerField quater1Amount;
	IntegerField quater2Amount;
	IntegerField quater3Amount;
	IntegerField quater4Amount;

	IntegerField annualAmount;

	HashMap<String, String> newMap;

	DynamicForm budgetInfoForm;
	DynamicForm budgetAddForm;
	SelectCombo budgetAddBy;
	ClientBudgetItem defaultValues = new ClientBudgetItem();
	private DisclosurePanel discloserPanel;
	private SelectCombo budgetRoundOfMethod;

	int calculation = 2;
	int roundOff = 0;

	public AddBudgetAmountDialogue(String title, String desc,
			HashMap<String, String> map, ClientBudgetItem budgetItem) {
		super(title, desc);
		newMap = new HashMap<String, String>();
		defaultValues = budgetItem;
		map = newMap;
		createControls();
	}

	private void createControls() {
		VerticalPanel verticalPanel = new VerticalPanel();
		setWidth("400px");

		budgetAddBy = new SelectCombo(messages.budgetAddBy());
		budgetAddBy.setHelpInformation(true);
		budgetAddBy.initCombo(getStartWithList());
		budgetAddBy.setSelected(MONTHS);
		budgetAddBy
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (selectItem == MONTHS) {
							resetView(MONTHS);
						} else if (selectItem == QUARTERS) {
							resetView(QUARTERS);
						} else if (selectItem == YEARS) {
							resetView(YEARS);
						}
					}

				});

		janAmount = new IntegerField(this, DayAndMonthUtil.january() + ":");
		janAmount.setHelpInformation(true);
		janAmount.setRequired(false);
		janAmount.setWidth(100);

		febAmount = new IntegerField(this, DayAndMonthUtil.february() + ":");
		febAmount.setHelpInformation(true);
		febAmount.setRequired(false);
		febAmount.setWidth(100);

		marAmount = new IntegerField(this, DayAndMonthUtil.march() + ":");
		marAmount.setHelpInformation(true);
		marAmount.setRequired(false);
		marAmount.setWidth(100);

		aprAmount = new IntegerField(this, DayAndMonthUtil.april() + ":");
		aprAmount.setHelpInformation(true);
		aprAmount.setRequired(false);
		aprAmount.setWidth(100);

		mayAmount = new IntegerField(this, DayAndMonthUtil.may_full() + ":");
		mayAmount.setHelpInformation(true);
		mayAmount.setRequired(false);
		mayAmount.setWidth(100);

		junAmount = new IntegerField(this, DayAndMonthUtil.june() + ":");
		junAmount.setHelpInformation(true);
		junAmount.setRequired(false);
		junAmount.setWidth(100);

		julAmount = new IntegerField(this, DayAndMonthUtil.july() + ":");
		julAmount.setHelpInformation(true);
		julAmount.setRequired(false);
		julAmount.setWidth(100);

		augAmount = new IntegerField(this, DayAndMonthUtil.august() + ":");
		augAmount.setHelpInformation(true);
		augAmount.setRequired(false);
		augAmount.setWidth(100);

		septAmount = new IntegerField(this, DayAndMonthUtil.september() + ":");
		septAmount.setHelpInformation(true);
		septAmount.setRequired(false);
		septAmount.setWidth(100);

		octAmount = new IntegerField(this, DayAndMonthUtil.october() + ":");
		octAmount.setHelpInformation(true);
		octAmount.setRequired(false);
		octAmount.setWidth(100);

		novAmount = new IntegerField(this, DayAndMonthUtil.november() + ":");
		novAmount.setHelpInformation(true);
		novAmount.setRequired(false);
		novAmount.setWidth(100);

		decAmount = new IntegerField(this, DayAndMonthUtil.december() + ":");
		decAmount.setHelpInformation(true);
		decAmount.setRequired(false);
		decAmount.setWidth(100);

		budgetAddForm = UIUtils.form(messages.chartOfAccountsInformation());
		budgetAddForm.setWidth("100%");
		budgetAddForm.setFields(janAmount, febAmount, marAmount, aprAmount,
				mayAmount, junAmount, julAmount, augAmount, septAmount,
				octAmount, novAmount, decAmount);

		budgetInfoForm = UIUtils.form(messages.chartOfAccountsInformation());
		budgetInfoForm.setWidth("100%");

		budgetInfoForm.setFields(budgetAddBy);

		budgetRoundOfMethod = new SelectCombo(Accounter.messages().RoundOffBudgetAmountto());
		budgetRoundOfMethod.setHelpInformation(true);
		budgetRoundOfMethod.initCombo(getBudgetCalculationTypesList());
		budgetRoundOfMethod.setSelected(NONE);
		budgetRoundOfMethod
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

						if (selectItem.equals(NEAREST_AMOUNT)) {
							roundOff = 1;
						} else if (selectItem.equals(NEAREST_TEN)) {
							roundOff = 2;
						} else if (selectItem.equals(NEAREST_HUNDRED)) {
							roundOff = 3;
						} else if (selectItem.equals(NONE)) {
							roundOff = 0;
						}
					}

				});

		DynamicForm advanceCalculationVPanel = new DynamicForm();
		advanceCalculationVPanel.setFields(budgetRoundOfMethod);

		VerticalPanel vPanel = new VerticalPanel();
		vPanel.add(advanceCalculationVPanel);

		final String[] stringArray = {
				Accounter.messages().CalendarDaysAnnualAmountNoofDaysinMonth365or366(),
				Accounter.messages().CalendarMonthAnnualAmount12() };

		for (int i = 0; i < stringArray.length; i++) {
			String sport = stringArray[i];
			final RadioButton radioButton = new RadioButton(Accounter.messages().sport(), sport);
			radioButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					if (radioButton == event.getSource()) {
						System.out.println(radioButton.getText());
						if ((radioButton.getText().toString())
								.endsWith(stringArray[0]))
							calculation = 1;
						else if ((radioButton.getText().toString())
								.endsWith(stringArray[1]))
							calculation = 2;
					}
				}
			});
			if (i == 1) {
				radioButton.setValue(true);
			}
			vPanel.add(radioButton);
		}

		discloserPanel = new DisclosurePanel(Accounter.messages().AdvanceBudgetCalculation());
		discloserPanel.setContent(vPanel);

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.add(budgetInfoForm);

		verticalPanel.add(horizontalPanel);
		verticalPanel.add(budgetAddForm);
		verticalPanel.add(discloserPanel);

		setBodyLayout(verticalPanel);
		center();
		this.setDefaultValues();

	}

	private List<String> getStartWithList() {
		List<String> list = new ArrayList<String>();

		list.add(MONTHS);
		list.add(QUARTERS);
		list.add(YEARS);

		return list;
	}

	private List<String> getBudgetCalculationTypesList() {
		List<String> list = new ArrayList<String>();

		list.add(NEAREST_AMOUNT);
		list.add(NEAREST_TEN);
		list.add(NEAREST_HUNDRED);
		list.add(NONE);

		return list;
	}

	private void resetView(String viewType) {
		if (viewType == MONTHS) {

			budgetAddForm.removeAllRows();
			budgetAddForm.setFields(janAmount, febAmount, marAmount, aprAmount,
					mayAmount, junAmount, julAmount, augAmount, septAmount,
					octAmount, novAmount, decAmount);

		} else if (viewType == QUARTERS) {

			quater1Amount = new IntegerField(this, messages.quarterPeriod("1",
					DayAndMonthUtil.jan(), DayAndMonthUtil.apr()));
			quater1Amount.setHelpInformation(true);
			quater1Amount.setRequired(false);
			quater1Amount.setWidth(100);
			quater1Amount.setValue(Double.toString(Double.parseDouble(janAmount
					.getValue())
					+ Double.parseDouble(febAmount.getValue())
					+ Double.parseDouble(marAmount.getValue())));

			quater2Amount = new IntegerField(this, messages.quarterPeriod("2",
					DayAndMonthUtil.apr(), DayAndMonthUtil.mayS()));
			quater2Amount.setHelpInformation(true);
			quater2Amount.setRequired(false);
			quater2Amount.setWidth(100);
			quater2Amount.setValue(Double.toString(Double.parseDouble(aprAmount
					.getValue())
					+ Double.parseDouble(mayAmount.getValue())
					+ Double.parseDouble(junAmount.getValue())));

			quater3Amount = new IntegerField(this, messages.quarterPeriod("3",
					DayAndMonthUtil.jun(), DayAndMonthUtil.sep()));
			quater3Amount.setHelpInformation(true);
			quater3Amount.setRequired(false);
			quater3Amount.setWidth(100);
			quater3Amount.setValue(Double.toString(Double.parseDouble(julAmount
					.getValue())
					+ Double.parseDouble(augAmount.getValue())
					+ Double.parseDouble(septAmount.getValue())));

			quater4Amount = new IntegerField(this, messages.quarterPeriod("4",
					DayAndMonthUtil.oct(), DayAndMonthUtil.dec()));
			quater4Amount.setHelpInformation(true);
			quater4Amount.setRequired(false);
			quater4Amount.setWidth(100);
			quater4Amount.setValue(Double.toString(Double.parseDouble(octAmount
					.getValue())
					+ Double.parseDouble(novAmount.getValue())
					+ Double.parseDouble(decAmount.getValue())));

			budgetAddForm.removeAllRows();
			budgetAddForm.setFields(quater1Amount, quater2Amount,
					quater3Amount, quater4Amount);

		} else if (viewType == YEARS) {

			annualAmount = new IntegerField(this, "Annual" + ":");
			annualAmount.setHelpInformation(true);
			annualAmount.setRequired(false);
			annualAmount.setWidth(100);
			annualAmount.setValue(Double.toString(Double.parseDouble(janAmount
					.getValue())
					+ Double.parseDouble(febAmount.getValue())
					+ Double.parseDouble(marAmount.getValue())
					+ Double.parseDouble(aprAmount.getValue())
					+ Double.parseDouble(mayAmount.getValue())
					+ Double.parseDouble(junAmount.getValue())
					+ Double.parseDouble(julAmount.getValue())
					+ Double.parseDouble(augAmount.getValue())
					+ Double.parseDouble(septAmount.getValue())
					+ Double.parseDouble(octAmount.getValue())
					+ Double.parseDouble(novAmount.getValue())
					+ Double.parseDouble(decAmount.getValue())));

			budgetAddForm.removeAllRows();
			budgetAddForm.setFields(annualAmount);
		}

	}

	@Override
	protected boolean onOK() {

		if (budgetAddBy.getSelectedValue() == MONTHS) {

			newMap.put(DayAndMonthUtil.jan(), janAmount.getValue() != null ? janAmount
					.getValue().toString() : "0");

			newMap.put(DayAndMonthUtil.feb(), febAmount.getValue() != null ? febAmount
					.getValue().toString() : "0");

			newMap.put(DayAndMonthUtil.mar(), marAmount.getValue() != null ? marAmount
					.getValue().toString() : "0");

			newMap.put(DayAndMonthUtil.apr(), aprAmount.getValue() != null ? aprAmount
					.getValue().toString() : "0");

			newMap.put(DayAndMonthUtil.mayS(), mayAmount.getValue() != null ? mayAmount
					.getValue().toString() : "0");

			newMap.put(DayAndMonthUtil.jun(), junAmount.getValue() != null ? junAmount
					.getValue().toString() : "0");

			newMap.put(DayAndMonthUtil.jul(), julAmount.getValue() != null ? julAmount
					.getValue().toString() : "0");

			newMap.put(DayAndMonthUtil.aug(), augAmount.getValue() != null ? augAmount
					.getValue().toString() : "0");

			newMap.put(DayAndMonthUtil.sep(), septAmount.getValue() != null ? septAmount
					.getValue().toString() : "0");

			newMap.put(DayAndMonthUtil.oct(), octAmount.getValue() != null ? octAmount
					.getValue().toString() : "0");

			newMap.put(DayAndMonthUtil.nov(), novAmount.getValue() != null ? novAmount
					.getValue().toString() : "0");

			newMap.put(DayAndMonthUtil.dec(), decAmount.getValue() != null ? decAmount
					.getValue().toString() : "0");

		} else if (budgetAddBy.getSelectedValue() == QUARTERS) {

			String one, two, three, four;

			if (quater1Amount.getValue() != null)
				one = Double
						.toString(round(
								false,
								0,
								roundOff,
								calculation,
								Double.parseDouble(quater1Amount.getValue()) / 3.00,
								2));
			else
				one = "0.00";

			if (quater2Amount.getValue() != null)
				two = Double
						.toString(round(
								false,
								0,
								roundOff,
								calculation,
								Double.parseDouble(quater2Amount.getValue()) / 3.00,
								2));
			else
				two = "0.00";

			if (quater3Amount.getValue() != null)
				three = Double
						.toString(round(
								false,
								0,
								roundOff,
								calculation,
								Double.parseDouble(quater3Amount.getValue()) / 3.00,
								2));
			else
				three = "0.00";

			if (quater4Amount.getValue() != null)
				four = Double
						.toString(round(
								false,
								0,
								roundOff,
								calculation,
								Double.parseDouble(quater4Amount.getValue()) / 3.00,
								2));
			else
				four = "0.00";

			newMap.put(DayAndMonthUtil.jan(), one);

			newMap.put(DayAndMonthUtil.feb(), one);

			newMap.put(DayAndMonthUtil.mar(), one);

			newMap.put(DayAndMonthUtil.apr(), two);

			newMap.put(DayAndMonthUtil.mayS(), two);

			newMap.put(DayAndMonthUtil.jun(), two);

			newMap.put(DayAndMonthUtil.jul(), three);

			newMap.put(DayAndMonthUtil.aug(), three);

			newMap.put(DayAndMonthUtil.sep(), three);

			newMap.put(DayAndMonthUtil.oct(), four);

			newMap.put(DayAndMonthUtil.nov(), four);

			newMap.put(DayAndMonthUtil.dec(), four);

		} else if (budgetAddBy.getSelectedValue() == YEARS) {

			double valueToDivide = 12.00;
			if (calculation == 1) {
				valueToDivide = 365.00;
			}
			if (annualAmount.getValue() != null) {

				newMap.put(DayAndMonthUtil.jan(), Double.toString(round(true, 31, roundOff,
						calculation,
						Double.parseDouble(annualAmount.getValue())
								/ valueToDivide, 2)));

				newMap.put(DayAndMonthUtil.feb(), Double.toString(round(true, 28, roundOff,
						calculation,
						Double.parseDouble(annualAmount.getValue())
								/ valueToDivide, 2)));

				newMap.put(DayAndMonthUtil.mar(), Double.toString(round(true, 31, roundOff,
						calculation,
						Double.parseDouble(annualAmount.getValue())
								/ valueToDivide, 2)));

				newMap.put(DayAndMonthUtil.apr(), Double.toString(round(true, 30, roundOff,
						calculation,
						Double.parseDouble(annualAmount.getValue())
								/ valueToDivide, 2)));

				newMap.put(DayAndMonthUtil.mayS(), Double.toString(round(true, 31, roundOff,
						calculation,
						Double.parseDouble(annualAmount.getValue())
								/ valueToDivide, 2)));

				newMap.put(DayAndMonthUtil.jun(), Double.toString(round(true, 30, roundOff,
						calculation,
						Double.parseDouble(annualAmount.getValue())
								/ valueToDivide, 2)));

				newMap.put(DayAndMonthUtil.jul(), Double.toString(round(true, 31, roundOff,
						calculation,
						Double.parseDouble(annualAmount.getValue())
								/ valueToDivide, 2)));

				newMap.put(DayAndMonthUtil.aug(), Double.toString(round(true, 31, roundOff,
						calculation,
						Double.parseDouble(annualAmount.getValue())
								/ valueToDivide, 2)));

				newMap.put(DayAndMonthUtil.sep(), Double.toString(round(true, 30, roundOff,
						calculation,
						Double.parseDouble(annualAmount.getValue())
								/ valueToDivide, 2)));

				newMap.put(DayAndMonthUtil.oct(), Double.toString(round(true, 31, roundOff,
						calculation,
						Double.parseDouble(annualAmount.getValue())
								/ valueToDivide, 2)));

				newMap.put(DayAndMonthUtil.nov(), Double.toString(round(true, 30, roundOff,
						calculation,
						Double.parseDouble(annualAmount.getValue())
								/ valueToDivide, 2)));

				newMap.put(DayAndMonthUtil.dec(), Double.toString(round(true, 31, roundOff,
						calculation,
						Double.parseDouble(annualAmount.getValue())
								/ valueToDivide, 2)));
			} else {
				String one;
				one = "0.00";

				newMap.put(DayAndMonthUtil.jan(), one);

				newMap.put(DayAndMonthUtil.feb(), one);

				newMap.put(DayAndMonthUtil.mar(), one);

				newMap.put(DayAndMonthUtil.apr(), one);

				newMap.put(DayAndMonthUtil.mayS(), one);

				newMap.put(DayAndMonthUtil.jun(), one);

				newMap.put(DayAndMonthUtil.jul(), one);

				newMap.put(DayAndMonthUtil.aug(), one);

				newMap.put(DayAndMonthUtil.sep(), one);

				newMap.put(DayAndMonthUtil.oct(), one);

				newMap.put(DayAndMonthUtil.nov(), one);

				newMap.put(DayAndMonthUtil.dec(), one);
			}
		}

		getCallback().actionResult(newMap);
		return true;
	}

	public void setDefaultValues() {

		janAmount.setValue(Double.toString(defaultValues.getJanuaryAmount()));
		febAmount.setValue(Double.toString(defaultValues.getFebruaryAmount()));
		marAmount.setValue(Double.toString(defaultValues.getMarchAmount()));
		aprAmount.setValue(Double.toString(defaultValues.getAprilAmount()));
		mayAmount.setValue(Double.toString(defaultValues.getMayAmount()));
		junAmount.setValue(Double.toString(defaultValues.getJuneAmount()));
		julAmount.setValue(Double.toString(defaultValues.getJulyAmount()));
		augAmount.setValue(Double.toString(defaultValues.getAugustAmount()));
		septAmount
				.setValue(Double.toString(defaultValues.getSpetemberAmount()));
		octAmount.setValue(Double.toString(defaultValues.getOctoberAmount()));
		novAmount.setValue(Double.toString(defaultValues.getNovemberAmount()));
		decAmount.setValue(Double.toString(defaultValues.getDecemberAmount()));
	}

	@Override
	public void setFocus() {
		budgetAddBy.setFocus();

	}

	public static double round(boolean year, int month, int round, int calc,
			double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);

		double valueFinal = tmp / factor;

		double valueToReturn = calculateValue(valueFinal, round, calc, month,
				year);

		return valueToReturn;
	}

	private static double calculateValue(double valueFinal, int round,
			int calc, int monthDays, boolean year) {

		long round2 = Math.round(valueFinal);

		if (round == 1) {
			valueFinal = round2;
		} else if (round == 2) {
			round2 = (round2 / 10);
			round2 = round2 * 10;
			valueFinal = round2;
		} else if (round == 3) {
			round2 = (round2 / 100);
			round2 = round2 * 100;
			valueFinal = round2;
		}

		if (year == true) {
			if (calc == 1) {
				valueFinal = valueFinal * monthDays;
			}
		}

		return valueFinal;
	}

	@Override
	protected boolean onCancel() {
		return true;
	}
}
