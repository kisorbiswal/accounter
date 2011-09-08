package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.ClientBudgetList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class BudgetReport extends AbstractReportView<ClientBudgetList> {

	int budgettype;

	public BudgetReport(int budgetType) {
		super(false, "");
		budgettype = budgetType;
		BudgetServerReport serverreport = new BudgetServerReport(this,
				budgetType);

		this.serverReport = serverreport;
		this.serverReport.setIshowGridFooter(true);
	}

	@Override
	public void OnRecordClick(ClientBudgetList record) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getToolbarType() {
		if (budgettype == 2)
			return TOOLBAR_TYPE_BUDGET2;
		else if (budgettype == 3)
			return TOOLBAR_TYPE_BUDGET3;
		else if (budgettype == 4)
			return TOOLBAR_TYPE_BUDGET4;
		else
			return TOOLBAR_TYPE_BUDGET1;
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
