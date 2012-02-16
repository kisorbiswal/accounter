package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.JobActualCostDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.serverreports.JobActualCostDetailServerReport;

public class JobActualCostDetailReport extends
		AbstractReportView<JobActualCostDetail> {

	private boolean isActualcostDetail;
	private long transactionId;
	private long jobId;

	public JobActualCostDetailReport(boolean isActualcostDetail,
			long transactionId, long jobId) {
		this.isActualcostDetail = isActualcostDetail;
		this.transactionId = transactionId;
		this.jobId = jobId;
		this.serverReport = new JobActualCostDetailServerReport(this,
				isActualcostDetail);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getJobActualCostOrRevenueDetails(start,
				end, isActualcostDetail, transactionId, jobId, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(JobActualCostDetail record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		ReportsRPC.openTransactionView(record.getType(),
				record.getTransaction());
	}

}
