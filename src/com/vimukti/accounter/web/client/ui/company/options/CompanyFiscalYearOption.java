package com.vimukti.accounter.web.client.ui.company.options;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class CompanyFiscalYearOption extends AbstractPreferenceOption {

	private static CompanyFiscalYearOptionUiBinder uiBinder = GWT
			.create(CompanyFiscalYearOptionUiBinder.class);
	@UiField
	ListBox monthNameComboBox;
	@UiField
	Label monthsCheckboxLabel;
	@UiField
	Label fiscalYearDescriptionLabel;
	List<String> monthNam = new ArrayList<String>();
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

	public void createControls() {

		monthsCheckboxLabel.setText(constants.selectFirstMonthOfFiscalYear());
		fiscalYearDescriptionLabel.setText(constants.FiscalYearDescription());
		fiscalYearDescriptionLabel.setStyleName("organisation_comment");
		monthNames = new String[] { constants.january(), constants.february(),
				constants.march(), constants.april(), constants.may(),
				constants.june(), constants.july(), constants.august(),
				constants.september(), constants.october(),
				constants.november(), constants.december() };

		for (int i = 0; i < monthNames.length; i++) {
			monthNameComboBox.addItem(monthNames[i]);
			monthNam.add(monthNames[i]);
		}
	}

	public void initData() {
		if (monthNam.size() > 0)
			monthNameComboBox.setSelectedIndex(getCompanyPreferences()
					.getFiscalYearFirstMonth());
	}

	@Override
	public String getTitle() {
		return "Fiscal & Tax Year";
	}

	@Override
	public void onSave() {
		getCompanyPreferences().setFiscalYearFirstMonth(
				(monthNameComboBox.getSelectedIndex()));
	}

	@Override
	public String getAnchor() {
		return constants.company();
	}

}
