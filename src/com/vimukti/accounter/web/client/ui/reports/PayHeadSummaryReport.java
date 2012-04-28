package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.PayHeadSummary;
import com.vimukti.accounter.web.client.core.reports.PaySlipDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.serverreports.PayHeadSummaryServerReport;

public class PayHeadSummaryReport extends AbstractReportView<PayHeadSummary> {

	public PayHeadSummaryReport() {
		this.serverReport = new PayHeadSummaryServerReport(this);
	}

	@Override
	public void makeReportRequest(long payHeadId, ClientFinanceDate start,
			ClientFinanceDate end) {

	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_PAY_HEAD;
	}

	@Override
	public void initData() {
		if (data != null) {
			PaySlipDetail headSummary = (PaySlipDetail) data;
			PayHeadReportToolBar payHeadEmployeeToolBar = (PayHeadReportToolBar) this.toolbar;
			payHeadEmployeeToolBar.setPayHead(headSummary.getPayheadId());
		}
		super.initData();
	}

	@Override
	public void OnRecordClick(PayHeadSummary record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		UIUtils.runAction(record, ActionFactory.getPayHeadDetailReportAction());
	}

	@Override
	public void export(int generationType) {
		// TODO Auto-generated method stub

	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		PayHeadReportToolBar payHeadEmployeeToolBar = (PayHeadReportToolBar) this.toolbar;
		long payHeadId = payHeadEmployeeToolBar.getSelectedPayHead() == null ? data == null ? 0
				: ((PaySlipDetail) data).getPayheadId()
				: payHeadEmployeeToolBar.getSelectedPayHead().getID();
		Accounter.createReportService().getPayHeadSummaryReport(payHeadId,
				start, end, this);
	}
}
