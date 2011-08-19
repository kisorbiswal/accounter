/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Administrator
 * 
 */
public class SetupTrackEmployeesPage extends AbstractSetupPage {

	private static SetupTrackEmployeesPageUiBinder uiBinder = GWT
			.create(SetupTrackEmployeesPageUiBinder.class);
	@UiField
	VerticalPanel viewPanel;
	@UiField
	CheckBox w2Employees;
	@UiField
	CheckBox contractors;
	@UiField
	HTML trackExpenses;
	@UiField
	RadioButton employeeYes;
	@UiField
	RadioButton employeeNo;
	@UiField
	RadioButton trackYes;
	@UiField
	RadioButton trackNo;
	@UiField
	VerticalPanel trackPanel;

	interface SetupTrackEmployeesPageUiBinder extends
			UiBinder<Widget, SetupTrackEmployeesPage> {
	}

	public SetupTrackEmployeesPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createControls() {
		// TODO Auto-generated method stub

		w2Employees.setText(accounterConstants.wehavW2Employes());
		contractors.setText(accounterConstants.wehavContractors());
		trackExpenses.setText(accounterConstants.trackEmployeeExpenses());
		employeeYes.setText(accounterConstants.yes());
		employeeNo.setText(accounterConstants.no());
		trackYes.setText(accounterConstants.yes());
		trackNo.setText(accounterConstants.no());
	}

}
