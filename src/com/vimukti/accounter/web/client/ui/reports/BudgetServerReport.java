package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.ui.serverreports.AbstractFinaneReport;

public class BudgetServerReport extends AbstractFinaneReport<TrialBalance> {

	public BudgetServerReport(BudgetReport budgetReport) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] getDynamicHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getColunms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getColumnTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processRecord(TrialBalance record) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getColumnData(TrialBalance record, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientFinanceDate getStartDate(TrialBalance obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientFinanceDate getEndDate(TrialBalance obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub

	}

}
