package com.vimukti.accounter.web.client.ui.company.options;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class CompanyFiscalYearOption extends AbstractPreferenceOption {

	private static CompanyFiscalYearOptionUiBinder uiBinder = GWT
			.create(CompanyFiscalYearOptionUiBinder.class);
	@UiField
	ListBox monthNameComboBox;
	@UiField
	Label monthsCheckboxLabel;
	@UiField
	Label fiscalYearDescriptionLabel;
	@UiField
	Label dateItemLable;
	@UiField
	DateBox dateItem;
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

		monthsCheckboxLabel.setText(messages.selectFirstMonthOfFiscalYear());
		fiscalYearDescriptionLabel.setText(messages.FiscalYearDescription());
		fiscalYearDescriptionLabel.setStyleName("organisation_comment");
		monthNames = new String[] { DayAndMonthUtil.january(), DayAndMonthUtil.february(),
				DayAndMonthUtil.march(), DayAndMonthUtil.april(), DayAndMonthUtil.may_full(),
				DayAndMonthUtil.june(), DayAndMonthUtil.july(), DayAndMonthUtil.august(),
				DayAndMonthUtil.september(), DayAndMonthUtil.october(),
				DayAndMonthUtil.november(), DayAndMonthUtil.december() };

		for (int i = 0; i < monthNames.length; i++) {
			monthNameComboBox.addItem(monthNames[i]);
			monthNam.add(monthNames[i]);
		}

		dateItemLable.setText(messages.preventPostBefore());
		DateTimeFormat dateFormat = DateTimeFormat
				.getFormat(getCompanyPreferences().getDateFormat());
		dateItem.setFormat(new DateBox.DefaultFormat(dateFormat));
	}

	public void initData() {
		if (monthNam.size() > 0)
			monthNameComboBox.setSelectedIndex(getCompanyPreferences()
					.getFiscalYearFirstMonth());
		if (getCompanyPreferences().getPreventPostingBeforeDate() != 0) {
			dateItem.setValue(new ClientFinanceDate(getCompanyPreferences()
					.getPreventPostingBeforeDate()).getDateAsObject());
		}

	}

	@Override
	public String getTitle() {
		return messages.FiscalTaxYear();
	}

	@Override
	public void onSave() {
		getCompanyPreferences().setFiscalYearFirstMonth(
				(monthNameComboBox.getSelectedIndex()));
		if (dateItem.getValue() != null) {
			getCompanyPreferences().setPreventPostingBeforeDate(
					new ClientFinanceDate(dateItem.getValue()).getDate());
		} else {
			getCompanyPreferences().setPreventPostingBeforeDate(0);
		}
	}

	@Override
	public String getAnchor() {
		return messages.company();
	}

}
