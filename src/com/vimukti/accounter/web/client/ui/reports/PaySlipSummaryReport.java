package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.PaySlipSummary;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.serverreports.PaySlipSummaryServerReport;

public class PaySlipSummaryReport extends AbstractReportView<PaySlipSummary> {

	public PaySlipSummaryReport() {
		this.serverReport = new PaySlipSummaryServerReport(this);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createPayrollService().getPaySlipSummary(start, end, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(PaySlipSummary record) {
		// TODO Auto-generated method stub

	}

	@Override
	public void export(int generationType) {
		// TODO Auto-generated method stub

	}

}
