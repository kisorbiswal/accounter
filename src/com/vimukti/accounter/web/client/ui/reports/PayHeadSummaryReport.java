package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.PayHeadSummary;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.serverreports.PayHeadSummaryServerReport;

public class PayHeadSummaryReport extends AbstractReportView<PayHeadSummary> {

	public PayHeadSummaryReport() {
		this.serverReport = new PayHeadSummaryServerReport(this);
	}

	@Override
	public void makeReportRequest(long payHeadId, ClientFinanceDate start,
			ClientFinanceDate end) {
		Accounter.createReportService().getPayHeadSummaryReport(payHeadId,
				start, end, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_PAY_HEAD;
	}

	@Override
	public void OnRecordClick(PayHeadSummary record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
	}

	@Override
	public void export(int generationType) {
		// TODO Auto-generated method stub

	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		// TODO Auto-generated method stub

	}

}
