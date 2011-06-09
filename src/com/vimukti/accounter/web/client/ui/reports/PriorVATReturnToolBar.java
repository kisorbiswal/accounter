/**
 * 
 */
package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.AccounterConstants;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientVATReturn;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXAgencyCombo;

/**
 * @author Murali.A
 * 
 */
public class PriorVATReturnToolBar extends ReportToolbar {
	private TAXAgencyCombo vatAgencyCombo;
	private SelectCombo endingDateCombo;
	private ClientTAXAgency selectedVATAgency = null;
	private ClientFinanceDate selectedEndDate;
	private SelectCombo dateRangeCombo;
	private List<String> dateRangeList;

	public PriorVATReturnToolBar() {
		createControls();
	}

	public void createControls() {

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
		// vatAgencyCombo.setWidth(40);
		vatAgencyCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXAgency>() {

					@Override
					public void selectedComboBoxItem(ClientTAXAgency selectItem) {
						if (selectItem != null) {
							// isToolBarComponentChanged = true;
							selectedVATAgency = selectItem;
							fillEndingDatesCombo(selectItem);
						}

					}
				});

		dateRangeCombo = new SelectCombo(FinanceApplication
				.getReportsMessages().dateRange());
		dateRangeCombo.setHelpInformation(true);
		dateRangeList = new ArrayList<String>();
		for (int i = 0; i < dateRangeArray.length; i++) {
			dateRangeList.add(dateRangeArray[i]);
		}
		dateRangeCombo.initCombo(dateRangeList);
		dateRangeCombo.setComboItem(FinanceApplication.getReportsMessages()
				.all());
		dateRangeCombo.setName(FinanceApplication.getReportsMessages()
				.dateRange());
		dateRangeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (!dateRangeCombo.getSelectedValue().equals(
								FinanceApplication.getReportsMessages()
										.custom())) {
							dateRangeChanged(dateRangeCombo.getSelectedValue());
						}
					}
				});

		endingDateCombo = new SelectCombo(FinanceApplication
				.getReportsMessages().chooseEndingDate());
		endingDateCombo.setHelpInformation(true);
		endingDateCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						reportRequest();
						// selectedEndDate =
						// endingDateCombo.getValue().toString();
						// if (selectedEndDate == null)
						// return;
						//
						// if (selectedEndDate.length() == 0)
						// return;

						// String endDate = UIUtils
						// .dateToString(new Date(selectedEndDate));
						// reportview.makeReportRequest(selectedVATAgency.getStringID(),
						// selectedEndDate);
						// FinanceApplication.createReportService()
						// .getPriorVATReturnReport("", endDate, reportview);

						// changeDates(new Date(), new Date(selectedEndDate));

					}
				});
		List<ClientTAXAgency> vatAgencies = FinanceApplication.getCompany()
				.getActiveTAXAgencies();
		for (ClientTAXAgency vatAgency : vatAgencies) {
			if (vatAgency.getName().equals(
					AccounterConstants.DEFAULT_VAT_AGENCY_NAME))
				vatAgencyCombo.addItemThenfireEvent(vatAgency);
		}

		if (UIUtils.isMSIEBrowser()) {
			dateRangeCombo.setWidth("200px");
			vatAgencyCombo.setWidth("200px");
			endingDateCombo.setWidth("150px");
		}

	}

	public void setDateRangeHide(boolean hide) {
		if (hide) {
			addItems(vatAgencyCombo, endingDateCombo);
		} else {
			addItems(dateRangeCombo, vatAgencyCombo, endingDateCombo);
		}
	}

	public void fillEndingDatesCombo(ClientTAXAgency selectItem) {
		List<ClientVATReturn> vatReturns = FinanceApplication.getCompany()
				.getVatReturns();
		List<String> endDates = new ArrayList<String>();
		for (ClientVATReturn vatReturn : vatReturns) {
			if (vatReturn.getTAXAgency().equalsIgnoreCase(
					selectItem.getStringID())) {

				endDates.add(UIUtils.dateToString(new ClientFinanceDate(
						vatReturn.getVATperiodEndDate())));
			}
		}
		endingDateCombo.initCombo(endDates);
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.reports.ReportToolbar#changeDates
	 * (java.util.Date, java.util.Date)
	 */
	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		itemSelectionHandler.onItemSelectionChanged(0, startDate, endDate);
		reportRequest();
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
			setStartDate(startDate);
			setEndDate(endDate);
		}
	}

	public void reportRequest() {
		if (!endingDateCombo.getSelectedValue().isEmpty())
			selectedEndDate = UIUtils.stringToDate(endingDateCombo
					.getSelectedValue());
		if (selectedEndDate == null) {
			getView().showRecords();
			return;
		}
		// if (selectedEndDate.length() == 0)
		// return;
		reportview.makeReportRequest(selectedVATAgency.getStringID(),
				selectedEndDate);
	}
}
