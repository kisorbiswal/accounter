package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.JobEstimatesVsActualsSummary;
import com.vimukti.accounter.web.client.core.reports.JobProfitability;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.JobEstimatesVsActualsSummaryServerReport;

public class JobEstimatesVsActualsSummaryReport extends
		AbstractReportView<JobEstimatesVsActualsSummary> {

	public JobEstimatesVsActualsSummaryReport() {
		this.serverReport = new JobEstimatesVsActualsSummaryServerReport(this);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getJobEstimatesVsActualsSummaryReport(
				start, end, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_BUDGETVSACTUALS;
	}

	@Override
	public void OnRecordClick(JobEstimatesVsActualsSummary record) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean canPrint() {
		return true;
	}

	@Override
	public boolean canExportToCsv() {
		return true;
	}

	@Override
	public void print() {
		String customerName = this.data != null ? ((JobProfitability) this.data)
				.getName() : "";
		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 188, "",
				"", customerName);
	}

	@Override
	public void exportToCsv() {
		UIUtils.exportReport(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 188, "",
				"");
	}
}
