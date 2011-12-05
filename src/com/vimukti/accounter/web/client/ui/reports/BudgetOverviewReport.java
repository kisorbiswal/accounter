package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.ClientBudgetList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class BudgetOverviewReport extends AbstractReportView<ClientBudgetList> {

	long budgetId;
	BudgetOverviewServerReport serverreport;

	public BudgetOverviewReport() {
		//super(false, "");
		serverreport = new BudgetOverviewServerReport(this);
		this.serverReport = serverreport;
		this.serverReport.setIshowGridFooter(true);
		this.addStyleName("budget_overview");
	}

	@Override
	public void OnRecordClick(ClientBudgetList record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		// ReportsRPC.openTransactionView(record.getTransactionType(),record.getTransactionId());
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_BUDGET;
	}

	@Override
	public void makeReportRequest(long id, ClientFinanceDate start,
			ClientFinanceDate end) {

		budgetId = id;
		Accounter.createReportService().getBudgetItemsList(id, this);
	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {
		// UIUtils.generateBudgetReportPDF(154, BUDGET_TYPE_CUSTOM);

		UIUtils.generateReportPDF(Integer.parseInt(String
				.valueOf(new ClientFinanceDate().getDate())), Integer
				.parseInt(String.valueOf(new ClientFinanceDate().getDate())),
				154, "", "", budgetId);

	}

	public void exportToCsv() {
		// UIUtils.exportBudgetReport(
		// Integer.parseInt(String.valueOf(startDate.getDate())),
		// Integer.parseInt(String.valueOf(endDate.getDate())), 154, "",
		// "");
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		// TODO Auto-generated method stub

	}

}
