package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.ClientBudgetList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class BudgetVsActualsReport extends AbstractReportView<ClientBudgetList> {

	long budgetId;
	BudgetVsActualsServerReport serverreport;

	public BudgetVsActualsReport() {
		serverreport = new BudgetVsActualsServerReport(this);
		this.serverReport = serverreport;
		this.serverReport.setIshowGridFooter(true);
		this.addStyleName("budget_overview");
	}

	@Override
	public void OnRecordClick(ClientBudgetList record) {

		ReportsRPC.openTransactionView(record.getTransactionType(),
				record.getTransactionId());
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_BUDGETVSACTUALS;
	}

	@Override
	public void makeReportRequest(long id, ClientFinanceDate start,
			ClientFinanceDate end) {

		budgetId = id;
		if (budgetId == 999L) {
			super.addEmptyMessage(messages.noRecordsToShow());
		} else {
			Accounter.createReportService().getBudgetItemsList(id, this);
		}
	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

		UIUtils.generateReportPDF(Integer.parseInt(String
				.valueOf(new ClientFinanceDate().getDate())), Integer
				.parseInt(String.valueOf(new ClientFinanceDate().getDate())),
				154, "", "", budgetId);

	}

	public void exportToCsv() {
		UIUtils.exportReport(Integer.parseInt(String
				.valueOf(new ClientFinanceDate().getDate())), Integer
				.parseInt(String.valueOf(new ClientFinanceDate().getDate())),
				154, "", "", budgetId);

	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {

	}

}
