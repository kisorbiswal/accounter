package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.SalesByLocationSummary;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.serverreports.SalesByLocationsummaryServerReport;

public class SalesByLocationsummaryReport extends
		AbstractReportView<SalesByLocationSummary> {

	private boolean isLocation;

	public SalesByLocationsummaryReport(boolean isLocation) {
		this.isLocation = isLocation;
		this.serverReport = new SalesByLocationsummaryServerReport(this,
				isLocation);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getSalesByLocationSummaryReport(
				isLocation, start, end, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(SalesByLocationSummary record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		UIUtils.runAction(record,
				ActionFactory.getSalesByLocationDetailsAction(isLocation));
	}

	@Override
	public void print() {
		int reportType = 152;
		if (!isLocation) {
			reportType = 160;
		}
		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())),
				reportType, "", "");

	}

	@Override
	public void exportToCsv() {
		int reportType = 152;
		if (!isLocation) {
			reportType = 160;
		}
		UIUtils.exportReport(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())),
				reportType, "", "");
	}

}
