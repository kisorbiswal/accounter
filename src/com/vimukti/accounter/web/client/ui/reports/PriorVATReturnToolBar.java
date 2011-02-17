/**
 * 
 */
package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.vimukti.accounter.web.client.core.AccounterConstants;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientVATReturn;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.TAXAgencyCombo;
import com.vimukti.accounter.web.client.ui.forms.ComboBoxItem;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;

/**
 * @author Murali.A
 * 
 */
public class PriorVATReturnToolBar extends ReportToolbar {

	private TAXAgencyCombo taxAgencyCombo;
	private SelectItem endingDateCombo;
	private ClientTAXAgency selectedTAXAgency = null;
	private ClientFinanceDate selectedEndDate;
	private ComboBoxItem dateRangeItem;

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

		taxAgencyCombo = new TAXAgencyCombo(FinanceApplication
				.getReportsMessages().chooseVATAgency(), false);
		// vatAgencyCombo.setWidth(40);
		taxAgencyCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXAgency>() {

					@Override
					public void selectedComboBoxItem(ClientTAXAgency selectItem) {
						if (selectItem != null) {
							// isToolBarComponentChanged = true;
							selectedTAXAgency = selectItem;
							fillEndingDatesCombo(selectItem);
						}

					}
				});

		dateRangeItem = new ComboBoxItem();
		dateRangeItem.setTitle(FinanceApplication.getReportsMessages()
				.dateRange());
		dateRangeItem.setValueMap(dateRangeArray);
		dateRangeItem.setName(FinanceApplication.getReportsMessages()
				.dateRange());
		dateRangeItem.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (!dateRangeItem.getValue().toString().equals(
						FinanceApplication.getReportsMessages().custom())) {
					dateRangeChanged(dateRangeItem.getValue().toString());
				}

			}
		});

		endingDateCombo = new SelectItem();
		// endingDateCombo.setWidth(70);
		endingDateCombo.setTitle(FinanceApplication.getReportsMessages()
				.chooseEndingDate());
		endingDateCombo.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				reportRequest();
				// selectedEndDate = endingDateCombo.getValue().toString();
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
		List<ClientTAXAgency> taxAgencies = FinanceApplication.getCompany()
				.getActiveTAXAgencies();
		for (ClientTAXAgency taxAgency : taxAgencies) {
			if (taxAgency.getName().equals(
					AccounterConstants.DEFAULT_VAT_AGENCY_NAME))
				taxAgencyCombo.addItemThenfireEvent(taxAgency);
		}

		if (UIUtils.isMSIEBrowser()) {
			dateRangeItem.setWidth("200px");
			taxAgencyCombo.setWidth("200px");
			endingDateCombo.setWidth("150px");
		}
		addItems(dateRangeItem, taxAgencyCombo, endingDateCombo);
	}

	public void setDateRangeHide(boolean hide) {
		if (hide)
			dateRangeItem.hide();
	}

	public void fillEndingDatesCombo(ClientTAXAgency selectedTAXAgency) {
		List<ClientVATReturn> vatReturns = FinanceApplication.getCompany()
				.getVatReturns();
		List<String> endDates = new ArrayList<String>();
		endDates.add("");
		for (ClientVATReturn vatReturn : vatReturns) {
			if (vatReturn.getTAXAgency().equalsIgnoreCase(
					selectedTAXAgency.getStringID())) {

				endDates.add(UIUtils.dateToString(new ClientFinanceDate(
						vatReturn.getVATperiodEndDate())));
			}
		}
		endingDateCombo.setValueMap(endDates
				.toArray(new String[endDates.size()]));
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
		dateRangeItem.setDefaultValue(defaultDateRange);
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
		if (!endingDateCombo.getValue().toString().isEmpty())
			selectedEndDate = UIUtils.stringToDate(endingDateCombo.getValue()
					.toString());
		if (selectedEndDate == null) {
			getView().showRecords();
			return;
		}
		// if (selectedEndDate.length() == 0)
		// return;
		reportview.makeReportRequest(selectedTAXAgency.getStringID(),
				selectedEndDate);
	}
}
