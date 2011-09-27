/**
 * 
 */
package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.AccounterClientConstants;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientVATReturn;
import com.vimukti.accounter.web.client.ui.Accounter;
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

		vatAgencyCombo = new TAXAgencyCombo(Accounter.constants()
				.chooseVATAgency(), false);
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

		dateRangeCombo = new SelectCombo(Accounter.constants()
				.dateRange());
		dateRangeCombo.setHelpInformation(true);
		dateRangeList = new ArrayList<String>();
		for (int i = 0; i < dateRangeArray.length; i++) {
			dateRangeList.add(dateRangeArray[i]);
		}
		dateRangeCombo.initCombo(dateRangeList);
		dateRangeCombo.setComboItem(Accounter.constants().financialYearToDate());
		dateRangeCombo.setName(Accounter.constants().dateRange());
		dateRangeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (!dateRangeCombo.getSelectedValue().equals(
								Accounter.constants().custom())) {
							dateRangeChanged(dateRangeCombo.getSelectedValue());
						}
					}
				});

		endingDateCombo = new SelectCombo(Accounter.constants()
				.chooseEndingDate());
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
						// reportview.makeReportRequest(selectedVATAgency.getID(),
						// selectedEndDate);
						// FinanceApplication.createReportService()
						// .getPriorVATReturnReport("", endDate, reportview);

						// changeDates(new Date(), new Date(selectedEndDate));

					}
				});
		List<ClientTAXAgency> vatAgencies = getCompany().getActiveTAXAgencies();
		for (ClientTAXAgency vatAgency : vatAgencies) {
			if (vatAgency.getName().equals(
					AccounterClientConstants.DEFAULT_VAT_AGENCY_NAME))
				vatAgencyCombo.addItemThenfireEvent(vatAgency);
		}

		if (UIUtils.isMSIEBrowser()) {
			dateRangeCombo.setWidth("200px");
			vatAgencyCombo.setWidth("200px");
			endingDateCombo.setWidth("150px");
		}

	}

	private ClientCompany getCompany() {
		ClientCompany company = Accounter.getCompany();
		return company;
	}

	public void setDateRangeHide(boolean hide) {
		if (hide) {
			addItems(vatAgencyCombo, endingDateCombo);
		} else {
			addItems(dateRangeCombo, vatAgencyCombo, endingDateCombo);
		}
	}

	public void fillEndingDatesCombo(ClientTAXAgency selectItem) {
		List<ClientVATReturn> vatReturns = getCompany().getVatReturns();
		List<String> endDates = new ArrayList<String>();
		// endDates.add("");
		for (ClientVATReturn vatReturn : vatReturns) {
			if (vatReturn.getTAXAgency() == selectItem.getID()) {

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
			setSelectedEndDate(UIUtils.stringToDate(endingDateCombo
					.getSelectedValue()));
		if (getSelectedEndDate() == null) {
			getView().showRecords();
			return;
		}
		setStartDate(getSelectedEndDate());
		setEndDate(getSelectedEndDate());
		// if (selectedEndDate.length() == 0)
		// return;
		reportview
				.makeReportRequest(selectedVATAgency.getID(), getSelectedEndDate());
	}

	public ClientFinanceDate getSelectedEndDate() {
		return selectedEndDate;
	}

	public void setSelectedEndDate(ClientFinanceDate selectedEndDate) {
		this.selectedEndDate = selectedEndDate;
	}
}
