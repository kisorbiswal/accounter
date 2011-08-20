/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
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
	protected void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createControls() {
		headerLabel.setText(accounterConstants.doyouHaveEmployees());

		w2Employees.setText(accounterConstants.wehavW2Employes());
		contractors.setText(accounterConstants.wehavContractors());
		trackExpenses.setText(accounterConstants.trackEmployeeExpenses());
		trackEmployeeYes.setText(accounterConstants.yes());
		trackEmployeeNo.setText(accounterConstants.no());
		trackEmployeeExpenseYes.setText(accounterConstants.yes());
		trackEmployeeExpenseNo.setText(accounterConstants.no());
	}

	@Override
	public boolean doShow() {
		return true;
	}

}
