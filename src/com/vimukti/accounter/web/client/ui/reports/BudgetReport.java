package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class BudgetReport extends AbstractReportView<TrialBalance> {

	public BudgetReport() {
		super(false, "");
		this.serverReport = new BudgetServerReport(this);
		this.serverReport.setIshowGridFooter(false);
	}

	@Override
	public void OnRecordClick(TrialBalance record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());

	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_BUDGET;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 112, "",
				"");

	}

	public void exportToCsv() {
		UIUtils.exportReport(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 112, "",
				"");
	}

}
