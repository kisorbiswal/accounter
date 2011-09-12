package com.vimukti.accounter.web.client.ui.company.options;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;

public class CompanyFiscalYearOption extends AbstractPreferenceOption {
	private AccounterConstants constants;
	private static CompanyFiscalYearOptionUiBinder uiBinder = GWT
			.create(CompanyFiscalYearOptionUiBinder.class);
	@UiField
	RadioButton januaryMonthRadioButton;
	@UiField
	RadioButton firstMonthOfmyfiscalyearRadioButton;
	@UiField
	Label radioButtonHeaderLabel;
	@UiField
	ListBox monthNameComboBox;
	List<String> monthNam;
	String[] monthNames;

	interface CompanyFiscalYearOptionUiBinder extends
			UiBinder<Widget, CompanyFiscalYearOption> {
	}

	public CompanyFiscalYearOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	public CompanyFiscalYearOption(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	private void createControls() {

		monthNameComboBox.setTitle(constants.selectFirstMonthOfFiscalYear());

		monthNames = new String[] { constants.january(), constants.february(),
				constants.march(), constants.april(), constants.may(),
				constants.june(), constants.july(), constants.august(),
				constants.september(), constants.october(),
				constants.november(), constants.december() };

		for (int i = 0; i < monthNames.length; i++) {
			monthNameComboBox.addItem(monthNames[i]);
			monthNam.add(monthNames[i]);
		}
		radioButtonHeaderLabel.setText(constants.firstMonthofIncomeTaxyear());
		firstMonthOfmyfiscalyearRadioButton.setText(constants
				.firstMonthofmyfiscalyear());
		januaryMonthRadioButton.setText(constants.january());

	}

	private void initData() {
		if (monthNam.size() > 0)
			monthNameComboBox.setItemText(
					companyPreferences.getFiscalYearFirstMonth(), "MonThs");
	}

	@Override
	public String getTitle() {
		return "Fiscal & Tax Year";
	}

	@Override
	public void onSave() {
		companyPreferences.setFiscalYearFirstMonth(monthNam
				.indexOf(monthNameComboBox.getSelectedIndex()));

	}

	@Override
	public String getAnchor() {
		return constants.company();
	}

}
