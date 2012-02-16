package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.JobActualCostDetail;
import com.vimukti.accounter.web.client.ui.serverreports.JobActualCostDetailServerReport;

public class JobActualCostDetailReport extends
		AbstractReportView<JobActualCostDetail> {

	public JobActualCostDetailReport() {
		this.serverReport = new JobActualCostDetailServerReport(this);
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
	public void OnRecordClick(JobActualCostDetail record) {
		// TODO Auto-generated method stub

	}

}
