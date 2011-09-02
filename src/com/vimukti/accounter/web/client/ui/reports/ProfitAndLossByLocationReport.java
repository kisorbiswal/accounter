package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.ProfitAndLossByLocation;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.ProfitAndLossByLocationServerReport;

public class ProfitAndLossByLocationReport extends
		AbstractReportView<ProfitAndLossByLocation> {

	public ProfitAndLossByLocationReport() {
		ProfitAndLossByLocationServerReport.locations = Accounter.getCompany()
				.getLocations();
		ProfitAndLossByLocationServerReport.noColumns = ProfitAndLossByLocationServerReport.locations
				.size() + 2;
		this.serverReport = new ProfitAndLossByLocationServerReport(this);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getProfitAndLossByLocationReport(start,
				end, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(ProfitAndLossByLocation record) {
		// record.setStartDate(toolbar.getStartDate());
		// record.setEndDate(toolbar.getEndDate());
		// record.setDateRange(toolbar.getSelectedDateRange());
		// UIUtils.runAction(record,
		// ActionFactory.getTransactionDetailByAccountAction());

	}

	@Override
	public void print() {
		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 153, "",
				"");
	}
}
