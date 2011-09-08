package com.vimukti.accounter.web.client.uibinder.companypreferences;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class EmployeeSettingsPage extends AbstractCompanyInfoPanel {

	@UiField
	VerticalPanel mainViewPanel;
	@UiField
	VerticalPanel viewPanel;
	@UiField
	CheckBox w2Employees;
	@UiField
	CheckBox contractors;
	@UiField
	Label trackExpenses;
	@UiField
	RadioButton trackEmployeeYes;
	@UiField
	RadioButton trackEmployeeNo;
	@UiField
	RadioButton trackEmployeeExpenseYes;
	@UiField
	RadioButton trackEmployeeExpenseNo;
	@UiField
	VerticalPanel trackPanel;
	@UiField
	Label headerLabel;
	@UiField
	VerticalPanel checkBoxPanel;

	private static EmployeeSettingsPageUiBinder uiBinder = GWT
			.create(EmployeeSettingsPageUiBinder.class);

	interface EmployeeSettingsPageUiBinder extends
			UiBinder<Widget, EmployeeSettingsPage> {
	}

	public EmployeeSettingsPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	private void initData() {
		if (companyPreferences.isHaveEpmloyees()) {
			trackEmployeeYes.setValue(true);
			if (!trackPanel.isAttached())
				mainViewPanel.add(trackPanel);
		} else {
			trackEmployeeNo.setValue(true);
			if (trackPanel.isAttached())
				mainViewPanel.remove(trackPanel);
		}

		if (companyPreferences.isTrackEmployeeExpenses()) {
			trackEmployeeExpenseYes.setValue(true);
		} else {
			trackEmployeeExpenseNo.setValue(true);
		}
		if (companyPreferences.isHaveW_2Employees()) {
			w2Employees.setValue(true);
		} else {
			w2Employees.setValue(false);
		}

		if (companyPreferences.isHave1099contractors()) {
			contractors.setValue(true);
		} else {
			contractors.setValue(false);
		}

	}

	protected void createControls() {
		headerLabel.setText(constants.doyouHaveEmployees());
		checkBoxPanel.getElement().getStyle().setMarginLeft(15, Unit.PX);

		w2Employees.setText(constants.wehavW2Employes());
		contractors.setText(constants.wehavContractors());
		trackExpenses.setText(constants.trackEmployeeExpenses());
		trackEmployeeYes.setText(constants.yes());
		trackEmployeeNo.setText(constants.no());
		trackEmployeeExpenseYes.setText(constants.yes());
		trackEmployeeExpenseNo.setText(constants.no());

		if (!trackEmployeeExpenseYes.getValue()) {
			// if (trackPanel.isAttached())
			mainViewPanel.remove(trackPanel);
		}

	}

	@Override
	public void onSave() {
		if (trackEmployeeYes.getValue()) {
			if ((w2Employees.getValue() || contractors.getValue())) {
				companyPreferences
						.setHaveEpmloyees(trackEmployeeYes.getValue());
				companyPreferences
						.setTrackEmployeeExpenses(trackEmployeeExpenseYes
								.getValue());
				companyPreferences.setHaveW_2Employees(w2Employees.getValue());
				companyPreferences.setHave1099contractors(contractors
						.getValue());
			}

		} else {
			companyPreferences.setHaveEpmloyees(trackEmployeeYes.getValue());
			companyPreferences.setTrackEmployeeExpenses(trackEmployeeExpenseYes
					.getValue());
			companyPreferences.setHaveW_2Employees(w2Employees.getValue());
			companyPreferences.setHave1099contractors(contractors.getValue());
		}

	}

	@UiHandler("trackEmployeeYes")
	void onTrackEmployeeYesClick(ClickEvent event) {
		if (!trackPanel.isAttached())
			mainViewPanel.add(trackPanel);
	}

	@UiHandler("trackEmployeeNo")
	void onTrackEmployeeNoClick(ClickEvent event) {
		if (trackPanel.isAttached())
			mainViewPanel.remove(trackPanel);

	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub

	}

	/*
	 * @Override protected boolean validate() { if (trackEmployeeYes.getValue())
	 * { if (!(w2Employees.getValue() || contractors.getValue())) {
	 * Accounter.showError(accounterMessages
	 * .pleaseselectvalidtransactionGrid(accounterConstants .employeeType()));
	 * return false; } else if (!(trackEmployeeExpenseYes.getValue() ||
	 * trackEmployeeExpenseNo .getValue())) {
	 * Accounter.showError(accounterConstants.trackEmployeeExpenses()); return
	 * false; } else { return true; } } else { return true; } }
	 */

}
