package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.JobProfitabilitySummary;
import com.vimukti.accounter.web.client.ui.serverreports.JobProfitabilitySummaryServerReport;

public class JobProfitabilitySummaryReport extends
		AbstractReportView<JobProfitabilitySummary> {
	public JobProfitabilitySummaryReport() {
		this.serverReport = new JobProfitabilitySummaryServerReport(this);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getToolbarType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void OnRecordClick(JobProfitabilitySummary record) {
		// TODO Auto-generated method stub

	}

}
