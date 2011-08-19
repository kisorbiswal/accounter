/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;

/**
 * @author Administrator
 * 
 */
public class SetupTrackEmployeesPage extends AbstractSetupPage {

	private static SetupTrackEmployeesPageUiBinder uiBinder = GWT
			.create(SetupTrackEmployeesPageUiBinder.class);
	@UiField VerticalPanel viewPanel;
	@UiField CheckBox W2Employees;
	@UiField CheckBox Contractors;
	@UiField HTML TrackExpenses;
	@UiField Label AccounterPayroll;
	@UiField RadioButton EmployeeYes;
	@UiField RadioButton EmployeeNo;
	@UiField RadioButton TrackYes;
	@UiField RadioButton TrackNo;

	interface SetupTrackEmployeesPageUiBinder extends
			UiBinder<Widget, SetupTrackEmployeesPage> {
	}

	public SetupTrackEmployeesPage() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	protected VerticalPanel getViewBody() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getViewHeader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onSave() {
		// TODO Auto-generated method stub

	}

}
