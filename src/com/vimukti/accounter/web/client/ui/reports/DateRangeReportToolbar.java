package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.theme.ThemesUtil;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;

public class DateRangeReportToolbar extends ReportToolbar {

	public DateItem fromItem;
	public DateItem toItem;
	@SuppressWarnings("unused")
	private SelectCombo reportBasisItemCombo;
	private SelectCombo dateRangeItemCombo;
	@SuppressWarnings("unused")
	private List<String> reportBasisItemList, dateRangeItemList;

	private Button updateButton;

	public DateRangeReportToolbar() {
		createControls();
		// selectedDateRange = FinanceApplication.getReportsMessages().all();
	}

	private void createControls() {
		@SuppressWarnings("unused")
		String[] reportBasisArray = {
				FinanceApplication.getReportsMessages().cash(),
				FinanceApplication.getReportsMessages().accrual() };

		String[] dateRangeArray = {
				FinanceApplication.getReportsMessages().all(),
				FinanceApplication.getReportsMessages().thisWeek(),
				FinanceApplication.getReportsMessages().thisMonth(),
				FinanceApplication.getReportsMessages().lastWeek(),
				FinanceApplication.getReportsMessages().lastMonth(),
				FinanceApplication.getReportsMessages().thisFinancialYear(),
				FinanceApplication.getReportsMessages().lastFinancialYear(),
				FinanceApplication.getReportsMessages().thisFinancialQuarter(),
				FinanceApplication.getReportsMessages().lastFinancialQuarter(),
				FinanceApplication.getReportsMessages().financialYearToDate(),
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
				FinanceApplication.getReportsMessages().custom() };

		LabelItem report = new LabelItem();
		report.setTitle(FinanceApplication.getReportsMessages()
				.reportBasisAccrual());
		// reportBasisItem = new ComboBoxItem();
		// reportBasisItem.setTitle("Report Basis");
		// reportBasisItem.setValueMap(reportBasisArray);
		// reportBasisItem.setDefaultValue(reportBasisArray[1]);
		// // report basic is not yet implemented, so disable the feature.
		// reportBasisItem.setDisabled(true);

		dateRangeItemCombo = new SelectCombo(FinanceApplication
				.getReportsMessages().dateRange());
		dateRangeItemCombo.setHelpInformation(true);
		dateRangeItemList = new ArrayList<String>();
		for (int i = 0; i < dateRangeArray.length; i++) {
			dateRangeItemList.add(dateRangeArray[i]);
		}
		dateRangeItemCombo.initCombo(dateRangeItemList);
		dateRangeItemCombo.setComboItem(FinanceApplication.getReportsMessages()
				.all());
		// dateRangeItem.setDefaultValue(dateRangeArray[0]);
		// dateRangeItem.addChangedHandler(new ChangeHandler() {
		//
		// @Override
		// public void onChange(ChangeEvent event) {
		// if (!dateRangeItem.getValue().toString().equals("Custom"))
		// dateRangeChanged(dateRangeItem.getValue().toString());
		// else {
		// fromItem.setDisabled(false);
		// toItem.setDisabled(false);
		// }
		//
		// }
		// });

		dateRangeItemCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						// if
						// (!dateRangeItem.getValue().toString().equals("Custom"))
						// {
						dateRangeChanged(dateRangeItemCombo.getSelectedValue());
						// fromItem.setDisabled(true);
						// toItem.setDisabled(true);
						// updateButton.setEnabled(false);
						// } else {
						// fromItem.setDisabled(false);
						// toItem.setDisabled(false);
						// updateButton.setEnabled(true);
						// }

					}
				});

		fromItem = new DateItem();
		fromItem.setHelpInformation(true);
		fromItem.setTitle(FinanceApplication.getReportsMessages().from());

		toItem = new DateItem();
		toItem.setHelpInformation(true);
		@SuppressWarnings("unused")
		ClientFinanceDate date = Utility.getLastandOpenedFiscalYearEndDate();

		// if (date != null)
		// toItem.setDate(date);
		// else
		// toItem.setDate(new Date());

		toItem.setTitle(FinanceApplication.getReportsMessages().to());
		toItem.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				startDate = fromItem.getValue();
				endDate = toItem.getValue();

				itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL,
						startDate, endDate);

			}
		});
		updateButton = new Button(FinanceApplication.getReportsMessages()
				.update());
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				setStartDate(fromItem.getDate());
				setEndDate(toItem.getDate());

				itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL,
						fromItem.getDate(), toItem.getDate());
				dateRangeItemCombo.setDefaultValue(FinanceApplication
						.getReportsMessages().custom());
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.custom());
			}
		});

		// fromItem.setDisabled(true);
		// toItem.setDisabled(true);
		// updateButton.setEnabled(false);

		Button printButton = new Button(FinanceApplication.getReportsMessages()
				.print());
		// printButton.setTop(2);
		// printButton.setWidth(40);
		printButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

			}

		});

		if (UIUtils.isMSIEBrowser()) {
			dateRangeItemCombo.setWidth("200px");
		}

		addItems(report, dateRangeItemCombo, fromItem, toItem);
		add(updateButton);
		updateButton.getElement().getParentElement().setClassName("ibutton");
		ThemesUtil.addDivToButton(updateButton, FinanceApplication
				.getThemeImages().button_right_blue_image(),
				"ibutton-right-image");
		this.setCellVerticalAlignment(updateButton,
				HasVerticalAlignment.ALIGN_MIDDLE);
	}

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		fromItem.setValue(startDate);
		toItem.setValue(endDate);
		itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL, startDate,
				endDate);
	}

	@Override
	public void setDateRanageOptions(String... dateRanages) {
		dateRangeItemCombo.setValueMap(dateRanages);
	}

	@Override
	public void setDefaultDateRange(String defaultDateRange) {
		// setSelectedDateRange(defaultDateRange);
		dateRangeItemCombo.setDefaultValue(defaultDateRange);
		dateRangeChanged(defaultDateRange);

	}

	@Override
	public void setStartAndEndDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		if (startDate != null && endDate != null) {
			fromItem.setValue(startDate);
			toItem.setValue(endDate);
			setStartDate(startDate);
			setEndDate(endDate);
		}

	}

}
