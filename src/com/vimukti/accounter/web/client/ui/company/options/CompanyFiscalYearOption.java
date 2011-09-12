package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;

public class CompanyFiscalYearOption extends AbstractPreferenceOption {
	private AccounterConstants constants;
	private static CompanyFiscalYearOptionUiBinder uiBinder = GWT
			.create(CompanyFiscalYearOptionUiBinder.class);
	@UiField
	Label firstMonthofFiscalyear;
	@UiField
	ListBox calenderMonths;
	@UiField
	Label firstMonthofIncomeTaxyear;
	@UiField
	RadioButton firstMonthofmyfiscalyear;
	@UiField
	RadioButton januaryRadioButton;
	String[] monthNames;

	interface CompanyFiscalYearOptionUiBinder extends
			UiBinder<Widget, CompanyFiscalYearOption> {
	}

	public CompanyFiscalYearOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	private void createControls() {

		constants = Accounter.constants();
		firstMonthofFiscalyear
				.setText(constants.selectFirstMonthOfFiscalYear());

		monthNames = new String[] { constants.january(), constants.february(),
				constants.march(), constants.april(), constants.may(),
				constants.june(), constants.july(), constants.august(),
				constants.september(), constants.october(),
				constants.november(), constants.december() };
		// fiscalStartsList = null;
		for (int i = 0; i < monthNames.length; i++) {
			calenderMonths.addItem(monthNames[i]);
		}

		firstMonthofIncomeTaxyear
				.setText(constants.firstMonthofIncomeTaxyear());

		firstMonthofmyfiscalyear.setText(constants.firstMonthofmyfiscalyear());

		januaryRadioButton.setText(constants.january());

	}

	private void initData() {
		// TODO Auto-generated method stub

	}

	public CompanyFiscalYearOption(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getAnchor() {
		// TODO Auto-generated method stub
		return null;
	}

}
