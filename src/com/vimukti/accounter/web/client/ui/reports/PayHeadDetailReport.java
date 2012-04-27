package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.reports.PayHeadDetails;
import com.vimukti.accounter.web.client.core.reports.PayHeadSummary;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.serverreports.PayHeadDetailServerReport;

public class PayHeadDetailReport extends AbstractReportView<PayHeadDetails> {

	public PayHeadDetailReport() {
		this.serverReport = new PayHeadDetailServerReport(this);
	}

	@Override
	public void initData() {
		super.initData();
		if (data != null) {
			PayHeadSummary headSummary = (PayHeadSummary) data;
			PayHeadEmployeeToolBar payHeadEmployeeToolBar = (PayHeadEmployeeToolBar) this.toolbar;
			payHeadEmployeeToolBar.setEmployee(headSummary.getEmployeeId());
			payHeadEmployeeToolBar.setPayHead(headSummary.getPayHead());
		}
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		PayHeadEmployeeToolBar payHeadEmployeeToolBar = (PayHeadEmployeeToolBar) this.toolbar;
		Accounter.createReportService().getPayHeadDetailReportList(
				payHeadEmployeeToolBar.getEmployee() == null ? 0
						: payHeadEmployeeToolBar.getEmployee().getID(),
				payHeadEmployeeToolBar.getSelectedPayHead() == null ? 0
						: payHeadEmployeeToolBar.getSelectedPayHead().getID(),
				start, end, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_PAY_HEAD_EMPLOYEE;
	}

	@Override
	public void OnRecordClick(PayHeadDetails record) {
		ReportsRPC.openTransactionView(ClientTransaction.TYPE_PAY_RUN,
				record.getTransactionId());
	}

	@Override
	public void export(int generationType) {
		// TODO Auto-generated method stub

	}

}
