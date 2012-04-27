package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.PaySheet;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.serverreports.PaySheetServerReport;

public class PaySheetReport extends AbstractReportView<PaySheet> {

	public PaySheetReport() {
		this.serverReport = new PaySheetServerReport(this);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createPayrollService().getPaySheet(start, end, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(PaySheet record) {
		// TODO Auto-generated method stub

	}

	@Override
	public void export(int generationType) {
		// TODO Auto-generated method stub

	}

}
