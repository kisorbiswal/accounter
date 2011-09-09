package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.ClientBudgetList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class BudgetReport extends AbstractReportView<ClientBudgetList> {

	private int BUDGET_TYPE_CUSTOM = 1;
	private int BUDGET_TYPE_MONTH = 2;
	private int BUDGET_TYPE_QUATER = 3;
	private int BUDGET_TYPE_YEAR = 4;

	int budgettype;
	BudgetServerReport serverreport;

	public BudgetReport(int budgetType) {
		super(false, "");
		budgettype = budgetType;
		serverreport = new BudgetServerReport(this, budgetType);
		this.serverReport = serverreport;
		this.serverReport.setIshowGridFooter(true);
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
		if (budgettype == BUDGET_TYPE_MONTH)
			return TOOLBAR_TYPE_BUDGET2;
		else if (budgettype == BUDGET_TYPE_QUATER)
			return TOOLBAR_TYPE_BUDGET3;
		else if (budgettype == BUDGET_TYPE_YEAR)
			return TOOLBAR_TYPE_BUDGET4;
		else
			return TOOLBAR_TYPE_BUDGET1;
	}

	@Override
	public void makeReportRequest(long id, ClientFinanceDate start,
			ClientFinanceDate end, int month) {

		serverreport.setMonth(month);
		Accounter.createReportService().getBudgetItemsList(id, start, end,
				month, this);
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
