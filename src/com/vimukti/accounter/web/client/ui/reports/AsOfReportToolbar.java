package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;

public class AsOfReportToolbar extends ReportToolbar {

	protected SelectCombo reportBasisCombo, dateRangeCombo;
	protected List<String> statusList, dateRangeList;
	private DateItem customDate;
	private AccounterButton updateButton;

	public AsOfReportToolbar() {
		createControls();
		// selectedDateRange = FinanceApplication.getReportsMessages().all();
	}

	private void createControls() {
		@SuppressWarnings("unused")
		String[] reportBasisArray = { Accounter.getReportsMessages().cash(),
				Accounter.getReportsMessages().accrual() };
		String[] dateRangeArray = { Accounter.getReportsMessages().all(),
				Accounter.getReportsMessages().thisWeek(),
				Accounter.getReportsMessages().thisMonth(),
				Accounter.getReportsMessages().lastWeek(),
				Accounter.getReportsMessages().lastMonth(),
				Accounter.getReportsMessages().thisFinancialYear(),
				Accounter.getReportsMessages().lastFinancialYear(),
				Accounter.getReportsMessages().thisFinancialQuarter(),
				Accounter.getReportsMessages().lastFinancialQuarter(),
				Accounter.getReportsMessages().financialYearToDate(),

				// FinanceApplication.getReportsMessages().today(),
				// FinanceApplication.getReportsMessages().endThisWeek(),
				// FinanceApplication.getReportsMessages().endThisWeekToDate(),
				// FinanceApplication.getReportsMessages().endThisMonth(),
				// FinanceApplication.getReportsMessages().endThisMonthToDate(),
				// FinanceApplication.getReportsMessages().endThisFiscalQuarter(),
				// FinanceApplication.getReportsMessages()
				// .endThisFiscalQuarterToDate(),
				// FinanceApplication.getReportsMessages()
				// .endThisCalanderQuarter(),
				// FinanceApplication.getReportsMessages()
				// .endThisCalanderQuarterToDate(),
				// FinanceApplication.getReportsMessages().endThisFiscalYear(),
				// FinanceApplication.getReportsMessages()
				// .endThisFiscalYearToDate(),
				// FinanceApplication.getReportsMessages().endThisCalanderYear(),
				// FinanceApplication.getReportsMessages()
				// .endThisCalanderYearToDate(),
				// FinanceApplication.getReportsMessages().endYesterday(),
				// FinanceApplication.getReportsMessages()
				// .endPreviousFiscalQuarter(),
				// FinanceApplication.getReportsMessages()
				// .endLastCalenderQuarter(),
				// FinanceApplication.getReportsMessages()
				// .previousFiscalYearSameDates(),
				// FinanceApplication.getReportsMessages().lastCalenderYear(),
				// FinanceApplication.getReportsMessages().previousCalenderYear(),
				Accounter.getReportsMessages().custom() };

		LabelItem report = new LabelItem();
		report.setTitle("Report Basis - Accrual");

		// reportBasisItem = new ComboBoxItem();
		// reportBasisItem.setTitle(FinanceApplication.getReportsMessages()
		// .reportBasis());
		// reportBasisItem.setValueMap(reportBasisArray);
		// reportBasisItem.setDefaultValue(reportBasisArray[1]);
		// // report basic is not yet implemented, so disable the feature.
		// reportBasisItem.setDisabled(true);

		dateRangeCombo = new SelectCombo(Accounter.getReportsMessages()
				.dateRange());
		dateRangeCombo.setHelpInformation(true);
		dateRangeList = new ArrayList<String>();
		for (int i = 0; i < dateRangeArray.length; i++) {
			dateRangeList.add(dateRangeArray[i]);
		}
		dateRangeCombo.initCombo(dateRangeList);
		dateRangeCombo.setDefaultValue(dateRangeArray[0]);
		dateRangeCombo.setComboItem(Accounter.getReportsMessages().all());
		dateRangeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

						// if (!dateRangeItem.getValue().toString().equals(
						// FinanceApplication.getReportsMessages().custom())) {
						dateRangeChanged(dateRangeCombo.getSelectedValue());
						// customDate.setDisabled(true);
						// updateButton.setEnabled(false);
						// } else {
						// customDate.setDisabled(false);
						// updateButton.setEnabled(true);
						// }

					}
				});
		customDate = new DateItem();
		customDate.setHelpInformation(true);
		// customDate.setUseTextField(true);
		// customDate.setWidth(100);
		customDate.setShowTitle(false);
		// customDate.setDisabled(true);
		customDate.setColSpan(2);
		customDate.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				ClientFinanceDate date = (customDate.getValue());

				if (date != null) {
					if (!date.after(startDate))
						Accounter.showError(Accounter.getReportsMessages()
								.pleaseSelectDateAfterCompanyStartDate()
								+ UIUtils.getDateStringByDate(startDate
										.toString()));
					else
						changeDates(startDate, date);
				} else {
					Accounter.showError(Accounter.getReportsMessages()
							.pleaseSelectDate());
				}

			}

		});

		ClientFinanceDate date = Accounter.getCompany()
				.getLastandOpenedFiscalYearEndDate();
		if (date != null)
			customDate.setValue(date);
		else
			customDate.setValue(new ClientFinanceDate());

		updateButton = new AccounterButton("Update");
		// updateButton.setEnabled(false);
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				// setStartDate(fromItem.getDate());
				setEndDate(customDate.getDate());

				itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL,
						startDate, customDate.getDate());
				dateRangeCombo.setDefaultValue(Accounter.getReportsMessages()
						.custom());
				setSelectedDateRange(Accounter.getReportsMessages().custom());

			}
		});

		if (UIUtils.isMSIEBrowser()) {
			dateRangeCombo.setWidth("200px");
		}
		addItems(report, dateRangeCombo, customDate);
		add(updateButton);
		updateButton.enabledButton();
		this.setCellVerticalAlignment(updateButton,
				HasVerticalAlignment.ALIGN_MIDDLE);
	}

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		customDate.setValue(endDate);
		itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL, startDate,
				endDate);
	}

	@Override
	public void setDefaultDateRange(String defaultDateRange) {
		dateRangeCombo.setDefaultValue(defaultDateRange);
		dateRangeChanged(defaultDateRange);

	}

	@Override
	public void setStartAndEndDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		if (endDate != null) {
			customDate.setEnteredDate(endDate);
			setStartDate(startDate);
			setEndDate(endDate);
		}

	}
}
