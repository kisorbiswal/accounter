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
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;

public class DateRangeReportToolbar extends ReportToolbar {

	public DateItem fromItem;
	public DateItem toItem;

	private SelectCombo reportBasisItemCombo;
	private SelectCombo dateRangeItemCombo;

	private List<String> reportBasisItemList, dateRangeItemList;

	private Button updateButton;

	public DateRangeReportToolbar() {
		createControls();
		// selectedDateRange = FinanceApplication.constants().all();
	}

	private void createControls() {

		String[] reportBasisArray = { Accounter.constants().cash(),
				Accounter.constants().accrual() };

		String[] dateRangeArray = { Accounter.constants().all(),
				Accounter.constants().thisWeek(),
				Accounter.constants().thisMonth(),
				Accounter.constants().lastWeek(),
				Accounter.constants().lastMonth(),
				Accounter.constants().thisFinancialYear(),
				Accounter.constants().lastFinancialYear(),
				Accounter.constants().thisFinancialQuarter(),
				Accounter.constants().lastFinancialQuarter(),
				Accounter.constants().financialYearToDate(),
				// FinanceApplication.constants().today(),
				// FinanceApplication.constants().endThisWeek(),
				// FinanceApplication.constants().endThisWeekToDate(),
				// FinanceApplication.constants().endThisMonth(),
				// FinanceApplication.constants().endThisMonthToDate(),
				// FinanceApplication.constants().endThisFiscalQuarter(),
				// FinanceApplication.constants()
				// .endThisFiscalQuarterToDate(),
				// FinanceApplication.constants()
				// .endThisCalanderQuarter(),
				// FinanceApplication.constants()
				// .endThisCalanderQuarterToDate(),
				// FinanceApplication.constants().endThisFiscalYear(),
				// FinanceApplication.constants()
				// .endThisFiscalYearToDate(),
				// FinanceApplication.constants().endThisCalanderYear(),
				// FinanceApplication.constants()
				// .endThisCalanderYearToDate(),
				// FinanceApplication.constants().endYesterday(),
				// FinanceApplication.constants()
				// .endPreviousFiscalQuarter(),
				// FinanceApplication.constants()
				// .endLastCalenderQuarter(),
				// FinanceApplication.constants()
				// .previousFiscalYearSameDates(),
				// FinanceApplication.constants().lastCalenderYear(),
				// FinanceApplication.constants().previousCalenderYear(),
				Accounter.constants().custom() };

		LabelItem report = new LabelItem();
		report.setTitle(Accounter.constants().reportBasisAccrual());
		// reportBasisItem = new ComboBoxItem();
		// reportBasisItem.setTitle("Report Basis");
		// reportBasisItem.setValueMap(reportBasisArray);
		// reportBasisItem.setDefaultValue(reportBasisArray[1]);
		// // report basic is not yet implemented, so disable the feature.
		// reportBasisItem.setDisabled(true);

		dateRangeItemCombo = new SelectCombo(Accounter.constants().dateRange());
		dateRangeItemCombo.setHelpInformation(true);
		dateRangeItemList = new ArrayList<String>();
		for (int i = 0; i < dateRangeArray.length; i++) {
			dateRangeItemList.add(dateRangeArray[i]);
		}
		dateRangeItemCombo.initCombo(dateRangeItemList);
		dateRangeItemCombo.setComboItem(Accounter.constants()
				.financialYearToDate());
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
		fromItem.setTitle(Accounter.constants().from());

		toItem = new DateItem();
		toItem.setHelpInformation(true);

		ClientFinanceDate date = Accounter.getCompany()
				.getCurrentFiscalYearEndDate();
		// .getLastandOpenedFiscalYearEndDate();

		// if (date != null)
		// toItem.setDate(date);
		// else
		// toItem.setDate(new Date());

		toItem.setTitle(Accounter.constants().to());
		toItem.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				startDate = fromItem.getValue();
				endDate = toItem.getValue();

				itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL,
						startDate, endDate);

			}
		});
		updateButton = new Button(Accounter.constants().update());
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				setStartDate(fromItem.getDate());
				setEndDate(toItem.getDate());

				itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL,
						fromItem.getDate(), toItem.getDate());
				dateRangeItemCombo.setDefaultValue(Accounter.constants()
						.custom());
				setSelectedDateRange(Accounter.constants().custom());
			}
		});

		// fromItem.setDisabled(true);
		// toItem.setDisabled(true);
		// updateButton.setEnabled(false);

		Button printButton = new Button(Accounter.constants().print());
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
