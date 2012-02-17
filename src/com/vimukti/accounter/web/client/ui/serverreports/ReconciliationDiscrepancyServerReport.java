package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.ReconciliationDiscrepancy;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class ReconciliationDiscrepancyServerReport extends
		AbstractFinaneReport<ReconciliationDiscrepancy> {

	public ReconciliationDiscrepancyServerReport(
			IFinanceReport<ReconciliationDiscrepancy> reportView) {
		this.reportView = reportView;
	}

	public ReconciliationDiscrepancyServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { getMessages().type(), getMessages().date(),
				getMessages().enterDate(), getMessages().number(),
				getMessages().name(),
				getMessages().reconciled() + " " + getMessages().amount(),
				"Type of Change", "Effect on Change" };
	}

	@Override
	public String getTitle() {
		return "Reconciliation Discrepancy";
	}

	@Override
	public String[] getColunms() {
		return new String[] { getMessages().type(), getMessages().date(),
				getMessages().enterDate(), getMessages().number(),
				getMessages().name(),
				getMessages().reconciled() + " " + getMessages().amount(),
				"Type of Change", "Effect on Change" };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_DATE, COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public void processRecord(ReconciliationDiscrepancy record) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getColumnData(ReconciliationDiscrepancy record, int index) {
		switch (index) {
		case 0:
			return "";
		case 1:
			return "";
		case 2:
			return "";
		case 3:
			return "";
		case 4:
			return "";
		case 5:
			return "";
		case 6:
			return "";
		case 7:
			return "";
		default:
			return "";
		}
	}

	@Override
	public ClientFinanceDate getStartDate(ReconciliationDiscrepancy obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(ReconciliationDiscrepancy obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub

	}
}
