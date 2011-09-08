package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.ClientBudgetList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class BudgetReport extends AbstractReportView<ClientBudgetList> {

	public BudgetReport() {
		super(false, "");
		this.serverReport = new BudgetServerReport(this);
		this.serverReport.setIshowGridFooter(true);
	}

	@Override
	public void OnRecordClick(ClientBudgetList record) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_BUDGET;
	}

	@Override
	public void makeReportRequest(long id, ClientFinanceDate start,
			ClientFinanceDate end) {
		Accounter.createReportService().getBudgetItemsList(id, startDate,
				endDate, this);
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

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		// TODO Auto-generated method stub

	}

}
