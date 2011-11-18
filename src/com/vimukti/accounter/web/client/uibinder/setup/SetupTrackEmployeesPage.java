/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.Accounter;

/**
 * @author Administrator
 * 
 */
public class SetupTrackEmployeesPage extends AbstractSetupPage {

	private static SetupTrackEmployeesPageUiBinder uiBinder = GWT
			.create(SetupTrackEmployeesPageUiBinder.class);

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

	interface SetupTrackEmployeesPageUiBinder extends
			UiBinder<Widget, SetupTrackEmployeesPage> {
	}

	public SetupTrackEmployeesPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	@Override
	protected void createControls() {
		headerLabel.setText(accounterMessages.doyouHaveEmployees());

		w2Employees.setText(accounterMessages.wehavW2Employes());
		contractors.setText(accounterMessages.wehavContractors());
		trackExpenses.setText(accounterMessages.trackEmployeeExpenses());
		trackEmployeeYes.setText(accounterMessages.yes());
		trackEmployeeNo.setText(accounterMessages.no());
		trackEmployeeExpenseYes.setText(accounterMessages.yes());
		trackEmployeeExpenseNo.setText(accounterMessages.no());

		if (!trackEmployeeExpenseYes.getValue()) {
			// if (trackPanel.isAttached())
			mainViewPanel.remove(trackPanel);
		}

	}

	@Override
	public void onLoad() {
		if (preferences.isHaveEpmloyees()) {
			trackEmployeeYes.setValue(true);
		} else {
			trackEmployeeNo.setValue(true);
		}

		if (preferences.isTrackEmployeeExpenses()) {
			trackEmployeeExpenseYes.setValue(true);
		} else {
			trackEmployeeExpenseNo.setValue(true);
		}
		if (preferences.isHaveW_2Employees()) {
			w2Employees.setValue(true);
		} else {
			w2Employees.setValue(false);
		}

		if (preferences.isHave1099contractors()) {
			contractors.setValue(true);
		} else {
			contractors.setValue(false);
		}

	}

	@Override
	public void onSave() {
		if (trackEmployeeYes.getValue()) {
			if ((w2Employees.getValue() || contractors.getValue())) {
				preferences.setHaveEpmloyees(trackEmployeeYes.getValue());
				preferences.setTrackEmployeeExpenses(trackEmployeeExpenseYes
						.getValue());
				preferences.setHaveW_2Employees(w2Employees.getValue());
				preferences.setHave1099contractors(contractors.getValue());
			}

		} else {
			preferences.setHaveEpmloyees(trackEmployeeYes.getValue());
			preferences.setTrackEmployeeExpenses(trackEmployeeExpenseYes
					.getValue());
			preferences.setHaveW_2Employees(w2Employees.getValue());
			preferences.setHave1099contractors(contractors.getValue());
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
	protected boolean validate() {
		if (trackEmployeeYes.getValue()) {
			if (!(w2Employees.getValue() || contractors.getValue())) {
				Accounter.showError(accounterMessages
						.pleaseselectvalidtransactionGrid(accounterMessages
								.employeeType()));
				return false;
			} else if (!(trackEmployeeExpenseYes.getValue() || trackEmployeeExpenseNo
					.getValue())) {
				Accounter.showError(accounterMessages.trackEmployeeExpenses());
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

	@Override
	public String getViewName() {
		return accounterMessages.trackEmployeeExpenses();
	}
}
