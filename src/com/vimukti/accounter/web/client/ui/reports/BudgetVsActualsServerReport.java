package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.BaseReport;
import com.vimukti.accounter.web.client.core.reports.BudgetActuals;
import com.vimukti.accounter.web.client.ui.serverreports.AbstractFinaneReport;

public class BudgetVsActualsServerReport extends
		AbstractFinaneReport<BudgetActuals> {

	protected List<String> sectiontypes;
	private String sectionName;

	public BudgetVsActualsServerReport(IFinanceReport<BudgetActuals> reportView) {
		sectiontypes = new ArrayList<String>();
		this.reportView = reportView;

	}

	public BudgetVsActualsServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public Object getColumnData(BudgetActuals record, int columnIndex) {

		switch (columnIndex) {
		case 0:
			return record.getAccountName();
		case 1:
			return record.getAtualAmount();
		case 2:
			return record.getBudgetAmount();
		case 3:
			return record.getAtualAmount() - record.getBudgetAmount();
		case 4:
			return record.getBudgetAmount() - record.getAtualAmount();
		}

		return null;

	}

	@Override
	public String getDefaultDateRange() {
		return getMessages().all();
	}

	@Override
	public int[] getColumnTypes() {

		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT };

	}

	@Override
	public int getColumnWidth(int index) {

		if (index == 0) {
			return 200;
		} else {
			return -1;
		}
	}

	@Override
	public String[] getColunms() {

		return new String[] { messages.accountName(), messages.actual(),
				messages.budget(), messages.overBudget(), messages.remaining() };
	}

	@Override
	public String getTitle() {
		return null;
	}

	@Override
	public void processRecord(BudgetActuals record) {
		// addSection("Hello", "bye", null);

	}

	@Override
	public ClientFinanceDate getEndDate(BudgetActuals obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(BudgetActuals obj) {
		return obj.getStartDate();

	}

	@Override
	protected String getPreviousReportDateRange(Object object) {
		return ((BaseReport) object).getDateRange();
	}

	@Override
	protected ClientFinanceDate getPreviousReportStartDate(Object object) {
		return ((BaseReport) object).getStartDate();
	}

	@Override
	protected ClientFinanceDate getPreviousReportEndDate(Object object) {
		return ((BaseReport) object).getEndDate();
	}

	@Override
	public void resetVariables() {
		this.sectionDepth = 0;
	}

	@Override
	public boolean isWiderReport() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String[] getDynamicHeaders() {

		return new String[] { messages.accountName(), messages.actual(),
				messages.budget(), messages.overBudget(), messages.remaining() };

	}

	@Override
	public void makeReportRequest(long start, long end) {

	}

}
