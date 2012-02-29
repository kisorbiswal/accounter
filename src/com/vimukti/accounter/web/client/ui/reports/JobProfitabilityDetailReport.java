package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.JobProfitabilityDetailByJob;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.serverreports.JobProfitabilityDetailServerReport;

public class JobProfitabilityDetailReport extends
		AbstractReportView<JobProfitabilityDetailByJob> {

	private long customerId;
	private long jobId;
	public JobProfitabilityDetailReport() {
this.serverReport = new JobProfitabilityDetailServerReport(this);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		
		this.customerId = toolbar.getPayeeId();
		this.jobId = toolbar.getJobId();
		Accounter.createReportService().getJobProfitabilityDetailByJobReport(toolbar.getPayeeId(), toolbar.getJobId(),start,
				end, this);

	}

	public void OnClick(JobProfitabilityDetailByJob record, int rowIndex, int columnIndex) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		
		if (columnIndex == 1) {// calls cost method,
			record.setCost(false);
			UIUtils.runAction(record, ActionFactory.getItemActualCostDetailReportAction());
			
			
		} else if (columnIndex == 2) {// calls revenue method
			record.setCost(true);
			UIUtils.runAction(record, ActionFactory.getItemActualCostDetailReportAction());
		}

	}
	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_JOB;
	}

	@Override
	public void OnRecordClick(JobProfitabilityDetailByJob record) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canPrint() {
		return true;
	}

	@Override
	public void print() {
		
		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 190, String.valueOf(customerId),
				String.valueOf(jobId), "");

	}

	@Override
	public boolean canExportToCsv() {
		return true;
	}

	@Override
	public void exportToCsv() {

		UIUtils.exportReport(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 190, String.valueOf(customerId),
				String.valueOf(jobId));
	}
}
