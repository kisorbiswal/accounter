package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.Reconciliation;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.serverreports.ReconcilationsServerReport;

public class ReconcilationsReport extends AbstractReportView<Reconciliation> {

	public ReconcilationsReport() {
		this.serverReport = new ReconcilationsServerReport(this);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getAllReconciliations(start, end,
				Accounter.getCompany().getID(), this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(Reconciliation record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		if (record.getAccountId() != 0) {
			UIUtils.runAction(record, ActionFactory
					.getReconcilationDetailByAccount(record.getAccountId()));
		}
	}

	@Override
	public void print() {
		int reportType = 170;
		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())),
				reportType, "", "");
	}
}
