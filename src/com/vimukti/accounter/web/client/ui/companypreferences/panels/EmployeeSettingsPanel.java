package com.vimukti.accounter.web.client.ui.companypreferences.panels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;

public class EmployeeSettingsPanel extends AbstractCompanyInfoPanel {
	private RadioGroupItem employeeExpenseGroupItem;
	private RadioButton employeeYesGroupItem, employeeNoGroupItem;
	private CheckBox contractorsCheckboxItem, w2EmployeeItem;
	private DynamicForm employeeExpenseForm;
	private VerticalPanel mainPanel;

	public EmployeeSettingsPanel() {
		super();
		createControls();
	}

	private void createControls() {
		mainPanel = new VerticalPanel();

		VerticalPanel employeeForm = new VerticalPanel();
		employeeExpenseForm = new DynamicForm();

		employeeYesGroupItem = new RadioButton(constants.employee(), constants
				.yes());
		employeeNoGroupItem = new RadioButton(constants.employee(), constants
				.no());
		employeeExpenseGroupItem = new RadioGroupItem();
		w2EmployeeItem = new CheckBox(constants.wehavW2Employes());
		contractorsCheckboxItem = new CheckBox(constants.wehavContractors());
		Label employeeLabelItem = new Label(constants.doyouHaveEmployees());
		LabelItem employeeExpenseItem = new LabelItem();
		employeeExpenseItem.setValue(constants.trackEmployeeExpenses());

		employeeForm.add(employeeLabelItem);
		employeeForm.add(employeeYesGroupItem);
		employeeForm.add(w2EmployeeItem);
		employeeForm.add(contractorsCheckboxItem);
		employeeForm.add(employeeNoGroupItem);

		employeeExpenseGroupItem.setValue(constants.yes(), constants.no());

		employeeExpenseForm.setFields(employeeExpenseItem,
				employeeExpenseGroupItem);

		mainPanel.add(employeeForm);

		employeeYesGroupItem.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (!(employeeExpenseForm.isAttached()))
					mainPanel.add(employeeExpenseForm);
			}
		});
		employeeNoGroupItem.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (employeeExpenseForm.isAttached())
					mainPanel.remove(employeeExpenseForm);
			}
		});
		add(mainPanel);
	}

	@Override
	public void onLoad() {
		if (companyPreferences.isHaveEpmloyees())
			employeeYesGroupItem.setValue(true);
		else
			employeeNoGroupItem.setValue(true);

		if (companyPreferences.isTrackEmployeeExpenses())
			employeeExpenseGroupItem.setValue(constants.yes());
		else
			employeeExpenseGroupItem.setValue(constants.no());

		w2EmployeeItem.setValue(companyPreferences.isHaveW_2Employees());
		contractorsCheckboxItem.setValue(companyPreferences
				.isHave1099contractors());

		if (employeeYesGroupItem.getValue()) {
			mainPanel.add(employeeExpenseForm);
		} else {
			if (employeeExpenseForm.isAttached())
				mainPanel.remove(employeeExpenseForm);
		}
	}

	@Override
	public void onSave() {
		if (employeeYesGroupItem.getValue())
			companyPreferences.setHaveEpmloyees(true);
		else
			companyPreferences.setHaveEpmloyees(false);

		if (employeeExpenseGroupItem.getValue().equals(constants.yes()))
			companyPreferences.setTrackEmployeeExpenses(true);
		else
			employeeExpenseGroupItem.setValue(constants.no());

		companyPreferences.setHaveW_2Employees(w2EmployeeItem.getValue());
		companyPreferences.setHave1099contractors(contractorsCheckboxItem
				.getValue());
	}

}
