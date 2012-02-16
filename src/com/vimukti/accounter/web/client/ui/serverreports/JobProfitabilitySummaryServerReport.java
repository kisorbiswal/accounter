package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.JobProfitabilitySummary;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class JobProfitabilitySummaryServerReport extends
		AbstractFinaneReport<JobProfitabilitySummary> {

	public JobProfitabilitySummaryServerReport(
			IFinanceReport<JobProfitabilitySummary> reportView) {
		this.reportView = reportView;
	}

	@Override
	public String[] getDynamicHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getColunms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getColumnTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processRecord(JobProfitabilitySummary record) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getColumnData(JobProfitabilitySummary record, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientFinanceDate getStartDate(JobProfitabilitySummary obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientFinanceDate getEndDate(JobProfitabilitySummary obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub

	}

}
