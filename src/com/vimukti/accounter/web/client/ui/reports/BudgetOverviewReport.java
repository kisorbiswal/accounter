package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientBudgetItem;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.ClientBudgetList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class BudgetOverviewReport extends AbstractReportView<ClientBudgetList> {

	private int BUDGET_TYPE_CUSTOM = 1;

	BudgetOverviewServerReport serverreport;

	public BudgetOverviewReport() {
		super(false, "");
		serverreport = new BudgetOverviewServerReport(this);
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
			return TOOLBAR_TYPE_BUDGET;
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
			UIUtils.generateBudgetReportPDF(154, BUDGET_TYPE_CUSTOM);
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
