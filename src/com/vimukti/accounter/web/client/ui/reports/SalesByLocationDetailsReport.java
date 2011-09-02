package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.SalesByLocationDetails;
import com.vimukti.accounter.web.client.core.reports.SalesByLocationSummary;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.SalesByLocationDetailsServerReport;

public class SalesByLocationDetailsReport extends
		AbstractReportView<SalesByLocationDetails> {

	public SalesByLocationDetailsReport() {
		this.serverReport = new SalesByLocationDetailsServerReport(this);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		SalesByLocationSummary record = (SalesByLocationSummary) data;
		if (record == null) {
			// Accounter.createReportService().getSalesByLocationDetailsReport(
			// start, end, this);
		} else {
			// Accounter.createReportService()
			// .getSalesByLocationDetailsForLocation(
			// record.getLocationName(), start, end, this);
		}
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(SalesByLocationDetails record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		ReportsRPC.openTransactionView(record.getType(),
				record.getTransactionid());
	}

	@Override
	public void print() {
		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 151, "",
				"");
	}

	@Override
	public void exportToCsv() {
		UIUtils.exportReport(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 151, "",
				"");
	}

}
