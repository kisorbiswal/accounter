package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.PaySlipDetail;
import com.vimukti.accounter.web.client.ui.serverreports.PaySlipDetailServerReport;

public class PaySlipDetailReport extends AbstractReportView<PaySlipDetail> {

	public PaySlipDetailReport() {
		this.serverReport = new PaySlipDetailServerReport(this);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		// Accounter.createPayrollService().getPaySlipDetail(start, end, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(PaySlipDetail record) {
		// TODO Auto-generated method stub

	}

	@Override
	public void export(int generationType) {
		// TODO Auto-generated method stub

	}

}
