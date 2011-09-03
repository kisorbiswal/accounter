package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class AddBudgetAmountDialogue extends BaseDialog {

	private static final String MONTHS = "Months";
	private static final String QUARTERS = "Quarters";
	private static final String YEARS = "Years";

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
	private String name;

	public AddBudgetAmountDialogue(String title, String desc,
			HashMap<String, String> map) {
		super(title, desc);
		newMap = new HashMap<String, String>();

		map = newMap;
		createControls();
	}

	private void createControls() {
		VerticalPanel verticalPanel = new VerticalPanel();
		setWidth("400px");

		budgetAddBy = new SelectCombo(Global.get().constants().budget() + " "
				+ Global.get().constants().add() + " by:");
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

		janAmount = new IntegerField(this, Global.get().constants().january()
				+ ":");
		janAmount.setHelpInformation(true);
		janAmount.setRequired(false);
		janAmount.setWidth(100);

		febAmount = new IntegerField(this, Global.get().constants().february()
				+ ":");
		febAmount.setHelpInformation(true);
		febAmount.setRequired(false);
		febAmount.setWidth(100);

		marAmount = new IntegerField(this, Global.get().constants().march()
				+ ":");
		marAmount.setHelpInformation(true);
		marAmount.setRequired(false);
		marAmount.setWidth(100);

		aprAmount = new IntegerField(this, Global.get().constants().april()
				+ ":");
		aprAmount.setHelpInformation(true);
		aprAmount.setRequired(false);
		aprAmount.setWidth(100);

		mayAmount = new IntegerField(this, Global.get().constants().may() + ":");
		mayAmount.setHelpInformation(true);
		mayAmount.setRequired(false);
		mayAmount.setWidth(100);

		junAmount = new IntegerField(this, Global.get().constants().june()
				+ ":");
		junAmount.setHelpInformation(true);
		junAmount.setRequired(false);
		junAmount.setWidth(100);

		julAmount = new IntegerField(this, Global.get().constants().july()
				+ ":");
		julAmount.setHelpInformation(true);
		julAmount.setRequired(false);
		julAmount.setWidth(100);

		augAmount = new IntegerField(this, Global.get().constants().august()
				+ ":");
		augAmount.setHelpInformation(true);
		augAmount.setRequired(false);
		augAmount.setWidth(100);

		septAmount = new IntegerField(this, Global.get().constants()
				.september()
				+ ":");
		septAmount.setHelpInformation(true);
		septAmount.setRequired(false);
		septAmount.setWidth(100);

		octAmount = new IntegerField(this, Global.get().constants().october()
				+ ":");
		octAmount.setHelpInformation(true);
		octAmount.setRequired(false);
		octAmount.setWidth(100);

		novAmount = new IntegerField(this, Global.get().constants().november()
				+ ":");
		novAmount.setHelpInformation(true);
		novAmount.setRequired(false);
		novAmount.setWidth(100);

		decAmount = new IntegerField(this, Global.get().constants().december()
				+ ":");
		decAmount.setHelpInformation(true);
		decAmount.setRequired(false);
		decAmount.setWidth(100);

		budgetAddForm = UIUtils.form(Accounter.messages()
				.chartOfAccountsInformation(Global.get().Account()));
		budgetAddForm.setWidth("100%");
		budgetAddForm.setFields(janAmount, febAmount, marAmount, aprAmount,
				mayAmount, junAmount, julAmount, augAmount, septAmount,
				octAmount, novAmount, decAmount);

		budgetInfoForm = UIUtils.form(Accounter.messages()
				.chartOfAccountsInformation(Global.get().Account()));
		budgetInfoForm.setWidth("100%");

		budgetInfoForm.setFields(budgetAddBy);

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.add(budgetInfoForm);

		verticalPanel.add(horizontalPanel);
		verticalPanel.add(budgetAddForm);

		setBodyLayout(verticalPanel);
		center();
	}

	private List<String> getStartWithList() {
		List<String> list = new ArrayList<String>();

		list.add(MONTHS);
		list.add(QUARTERS);
		list.add(YEARS);

		return list;
	}

	private void resetView(String years) {
		if (years == MONTHS) {

			budgetAddForm.removeAllRows();
			budgetAddForm.setFields(janAmount, febAmount, marAmount, aprAmount,
					mayAmount, junAmount, julAmount, augAmount, septAmount,
					octAmount, novAmount, decAmount);

		} else if (years == QUARTERS) {

			quater1Amount = new IntegerField(this, "Q1("
					+ Global.get().constants().jan() + " - "
					+ Global.get().constants().mar() + " ) :");
			quater1Amount.setHelpInformation(true);
			quater1Amount.setRequired(false);
			quater1Amount.setWidth(100);

			quater2Amount = new IntegerField(this, "Q2("
					+ Global.get().constants().apr() + " - "
					+ Global.get().constants().may() + " ) :");
			quater2Amount.setHelpInformation(true);
			quater2Amount.setRequired(false);
			quater2Amount.setWidth(100);

			quater3Amount = new IntegerField(this, "Q3("
					+ Global.get().constants().jun() + " - "
					+ Global.get().constants().sept() + " ) :");
			quater3Amount.setHelpInformation(true);
			quater3Amount.setRequired(false);
			quater3Amount.setWidth(100);

			quater4Amount = new IntegerField(this, "Q4("
					+ Global.get().constants().oct() + " - "
					+ Global.get().constants().dec() + " ) :");
			quater4Amount.setHelpInformation(true);
			quater4Amount.setRequired(false);
			quater4Amount.setWidth(100);

			budgetAddForm.removeAllRows();
			budgetAddForm.setFields(quater1Amount, quater2Amount,
					quater3Amount, quater4Amount);

		} else if (years == YEARS) {

			annualAmount = new IntegerField(this, "Annual" + ":");
			annualAmount.setHelpInformation(true);
			annualAmount.setRequired(false);
			annualAmount.setWidth(100);

			budgetAddForm.removeAllRows();
			budgetAddForm.setFields(annualAmount);
		}

	}

	@Override
	protected boolean onOK() {

		if (budgetAddBy.getSelectedValue() == MONTHS) {

			newMap.put("jan", janAmount.getNumber() != null ? janAmount
					.getNumber().toString() : "0");

			newMap.put("feb", febAmount.getNumber() != null ? febAmount
					.getNumber().toString() : "0");

			newMap.put("mar", marAmount.getNumber() != null ? marAmount
					.getNumber().toString() : "0");

			newMap.put("apr", aprAmount.getNumber() != null ? aprAmount
					.getNumber().toString() : "0");

			newMap.put("may", mayAmount.getNumber() != null ? mayAmount
					.getNumber().toString() : "0");

			newMap.put("jun", junAmount.getNumber() != null ? junAmount
					.getNumber().toString() : "0");

			newMap.put("jul", julAmount.getNumber() != null ? julAmount
					.getNumber().toString() : "0");

			newMap.put("aug", augAmount.getNumber() != null ? augAmount
					.getNumber().toString() : "0");

			newMap.put("sept", septAmount.getNumber() != null ? septAmount
					.getNumber().toString() : "0");

			newMap.put("oct", octAmount.getNumber() != null ? octAmount
					.getNumber().toString() : "0");

			newMap.put("nov", novAmount.getNumber() != null ? novAmount
					.getNumber().toString() : "0");

			newMap.put("dec", decAmount.getNumber() != null ? decAmount
					.getNumber().toString() : "0");

		} else if (budgetAddBy.getSelectedValue() == QUARTERS) {

			String one, two, three, four;

			if (quater1Amount.getNumber() != null)
				one = Double.toString(quater1Amount.getNumber() / 3);
			else
				one = "0.00";

			if (quater2Amount.getNumber() != null)
				two = Double.toString(quater2Amount.getNumber() / 3);
			else
				two = "0.00";

			if (quater3Amount.getNumber() != null)
				three = Double.toString(quater3Amount.getNumber() / 3);
			else
				three = "0.00";

			if (quater4Amount.getNumber() != null)
				four = Double.toString(quater4Amount.getNumber() / 3);
			else
				four = "0.00";

			newMap.put("jan", one);

			newMap.put("feb", one);

			newMap.put("mar", one);

			newMap.put("apr", two);

			newMap.put("may", two);

			newMap.put("jun", two);

			newMap.put("jul", three);

			newMap.put("aug", three);

			newMap.put("sept", three);

			newMap.put("oct", four);

			newMap.put("nov", four);

			newMap.put("dec", four);

		} else if (budgetAddBy.getSelectedValue() == YEARS) {

			String one;

			if (annualAmount.getNumber() != null)
				one = Double.toString(annualAmount.getNumber() / 12);
			else
				one = "0.00";

			newMap.put("jan", one);

			newMap.put("feb", one);

			newMap.put("mar", one);

			newMap.put("apr", one);

			newMap.put("may", one);

			newMap.put("jun", one);

			newMap.put("jul", one);

			newMap.put("aug", one);

			newMap.put("sept", one);

			newMap.put("oct", one);

			newMap.put("nov", one);

			newMap.put("dec", one);
		}

		getCallback().actionResult(newMap);
		return true;
	}
}
