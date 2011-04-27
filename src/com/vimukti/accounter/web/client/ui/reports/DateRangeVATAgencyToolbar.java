package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.vimukti.accounter.web.client.core.AccounterConstants;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.theme.ThemesUtil;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXAgencyCombo;
import com.vimukti.accounter.web.client.ui.forms.DateItem;

public class DateRangeVATAgencyToolbar extends ReportToolbar {

	private DateItem fromItem;
	private DateItem toItem;
	@SuppressWarnings("unused")
	private SelectCombo reportBasisItem, dateRangeItem;
	private TAXAgencyCombo vatAgencyCombo;
	protected String selectedEndDate;
	protected String selectedStartDate;
	private Button updateButton;
	private List<String> dateRangeList;

	public DateRangeVATAgencyToolbar() {
		createControls();
		// selectedDateRange = FinanceApplication.getReportsMessages().all();
	}

	private void createControls() {

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

		vatAgencyCombo = new TAXAgencyCombo(FinanceApplication
				.getReportsMessages().chooseVATAgency(), false);
		vatAgencyCombo.setHelpInformation(true);
		vatAgencyCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXAgency>() {

					@Override
					public void selectedComboBoxItem(ClientTAXAgency selectItem) {
						if (selectItem != null) {

							ClientFinanceDate startDate = fromItem.getDate();
							ClientFinanceDate endDate = toItem.getDate();

							reportview.makeReportRequest(selectItem
									.getStringID(), startDate, endDate);
						}

					}
				});
		List<ClientTAXAgency> vatAgencies = vatAgencyCombo.getComboItems();
		for (ClientTAXAgency vatAgency : vatAgencies) {
			if (vatAgency.getName().equals(
					AccounterConstants.DEFAULT_VAT_AGENCY_NAME)) {
				vatAgencyCombo.setComboItem(vatAgency);
				break;
			}
		}

		dateRangeItem = new SelectCombo(FinanceApplication.getReportsMessages()
				.dateRange());
		dateRangeItem.setHelpInformation(true);
		dateRangeItem.setValueMap(dateRangeArray);
		dateRangeItem.setDefaultValue(dateRangeArray[0]);
		dateRangeList = new ArrayList<String>();
		for (int i = 0; i < dateRangeArray.length; i++) {
			dateRangeList.add(dateRangeArray[i]);
		}
		dateRangeItem.setComboItem(FinanceApplication.getReportsMessages()
				.all());
		dateRangeItem
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (!dateRangeItem.getSelectedValue().equals("Custom")) {
							dateRangeChanged(dateRangeItem.getSelectedValue());
							// fromItem.setDisabled(true);
							// toItem.setDisabled(true);
							// updateButton.setEnabled(false);
							// } else {
							// fromItem.setDisabled(false);
							// toItem.setDisabled(false);
							// updateButton.setEnabled(true);
							// }
						}
					}
				});

		fromItem = new DateItem();
		fromItem.setHelpInformation(true);
		fromItem.setTitle(FinanceApplication.getReportsMessages().from());

		toItem = new DateItem();
		toItem.setHelpInformation(true);
		toItem.setTitle(FinanceApplication.getReportsMessages().to());
		toItem.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {

				ClientFinanceDate startDate = fromItem.getDate();
				ClientFinanceDate endDate = toItem.getDate();
				ClientTAXAgency vatAgency = vatAgencyCombo.getSelectedValue();
				if (vatAgency != null)
					reportview.makeReportRequest(vatAgency.getStringID(),
							startDate, endDate);

				// itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL,
				// fromItem.getDate(), toItem.getDate());

			}
		});

		updateButton = new Button(FinanceApplication.getReportsMessages()
				.update());
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				setStartDate(fromItem.getDate());
				setEndDate(toItem.getDate());

				ClientFinanceDate start = fromItem.getDate();
				ClientFinanceDate end = toItem.getDate();
				ClientTAXAgency vatAgency = vatAgencyCombo.getSelectedValue();
				if (vatAgency != null)
					reportview.makeReportRequest(vatAgency.getStringID(),
							start, end);

				// This will update the dates in the date range layout
				itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL,
						startDate, endDate);
				dateRangeItem.setDefaultValue(FinanceApplication
						.getReportsMessages().custom());
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.custom());
			}
		});

		// fromItem.setDisabled(true);
		// toItem.setDisabled(true);
		// updateButton.setEnabled(false);

		// set the Date Range to End this Calendar quarter to date
		// setDefaultDateRange(dateRangeArray);

		Button printButton = new Button(FinanceApplication.getReportsMessages()
				.print());
		printButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

			}

		});

		if (UIUtils.isMSIEBrowser()) {
			dateRangeItem.setWidth("200px");
			vatAgencyCombo.setWidth("200px");
		}

		addItems(vatAgencyCombo, dateRangeItem, fromItem, toItem);
		add(updateButton);
		if (updateButton.isEnabled()) {
			updateButton.getElement().getParentElement()
					.setClassName("ibutton");
			ThemesUtil.addDivToButton(updateButton, FinanceApplication
					.getThemeImages().button_right_blue_image(),
					"ibutton-right-image");
		}
		this.setCellVerticalAlignment(updateButton,
				HasVerticalAlignment.ALIGN_MIDDLE);
	}

	// set the Default Date Range to End this Calendar quarter to date
	@SuppressWarnings("unused")
	private void setDefaultDateRange(String[] dateRangeArray) {

		dateRangeItem.setDefaultValue(dateRangeArray[9]);
		ClientFinanceDate date = new ClientFinanceDate();
		int month = (date.getMonth()) % 3;
		int startMonth = date.getMonth() - month;
		ClientFinanceDate startDate = new ClientFinanceDate(date.getYear(),
				startMonth, 1);
		fromItem.setDatethanFireEvent(startDate);
		toItem.setDatethanFireEvent(date);
		setStartDate(startDate);
		setEndDate(date);
		// dateRangeChanged(dateRangeArray[9]);

	}

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {

		fromItem.setValue(startDate);
		toItem.setValue(endDate);

		ClientFinanceDate start = startDate;
		ClientFinanceDate end = endDate;
		ClientTAXAgency vatAgency = vatAgencyCombo.getSelectedValue();
		if (vatAgency != null)
			reportview.makeReportRequest(vatAgency.getStringID(), start, end);

		itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL, startDate,
				endDate);

	}

	@Override
	public void setDateRanageOptions(String... dateRanages) {
		List<String> dateRangesList = new ArrayList<String>();
		for (int i = 0; i < dateRanages.length; i++) {
			dateRangesList.add(dateRanages[i]);
		}

		dateRangeItem.initCombo(dateRangesList);
	}

	@Override
	public void setDefaultDateRange(String defaultDateRange) {
		dateRangeItem.setDefaultValue(defaultDateRange);
		dateRangeChanged(defaultDateRange);

	}

	@Override
	public void setStartAndEndDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		if (startDate != null && endDate != null) {
			fromItem.setEnteredDate(startDate);
			toItem.setEnteredDate(endDate);
			setStartDate(startDate);
			setEndDate(endDate);
		}

	}

}
