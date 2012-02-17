package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByAccount;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class MissingChecksServerReport extends
		AbstractFinaneReport<TransactionDetailByAccount> {

	private int previousNumber = 0;
	private int presentNumber = 0;
	private int previousType = 0;
	private int presentType = 0;

	public MissingChecksServerReport(
			IFinanceReport<TransactionDetailByAccount> reportView) {
		this.reportView = reportView;
	}

	public MissingChecksServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { getMessages().type(), getMessages().date(),
				getMessages().number(), getMessages().name(),
				getMessages().memo(), getMessages().accountName(),
				getMessages().totalAmount() };
	}

	@Override
	public String getTitle() {
		return "Missing Checks";
	}

	@Override
	public String[] getColunms() {
		return new String[] { getMessages().type(), getMessages().date(),
				getMessages().number(), getMessages().name(),
				getMessages().memo(), getMessages().accountName(),
				getMessages().totalAmount() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public void processRecord(TransactionDetailByAccount record) {
		if (record.getTransactionNumber().equals("")) {
			presentNumber = 0;
			previousType = record.getTransactionType();
			return;
		}
		presentNumber = Integer.valueOf(record.getTransactionNumber());
		presentType = record.getTransactionType();
		int difference = presentNumber - previousNumber;
		if (previousNumber == 0) {
			if (difference > 1) {
				// for (int i = 0; i < difference - 1; i++) {
				addSection("", "missing numbers here", new int[] { 0 });
				endSection();
			}
			previousNumber = presentNumber;
			previousType = presentType;
			return;
		} else if (presentType == previousType) {
			if (difference > 1) {
				// for (int i = 0; i < difference - 1; i++) {
				addSection("", "missing numbers here", new int[] { 0 });
				endSection();
			}
			previousNumber = presentNumber;
			previousType = presentType;
			return;
		} else if (presentType != previousType) {
			previousNumber = 0;
			int difference1 = presentNumber - previousNumber;
			if (difference1 > 1) {
				// for (int i = 0; i < difference1 - 1; i++) {
				addSection("", "missing numbers here", new int[] { 0 });
				endSection();
			}
			previousNumber = presentNumber;
			previousType = presentType;
			return;
		}

	}

	@Override
	public Object getColumnData(TransactionDetailByAccount record,
			int columnIndex) {
		switch (columnIndex) {
		case 0:
			return Utility.getTransactionName(record.getTransactionType());
		case 1:
			return record.getTransactionDate();
		case 2:
			return record.getTransactionNumber();
		case 3:
			return record.getName();
		case 4:
			return record.getAccountName();
		case 5:
			return record.getMemo();
		case 6:
			return record.getTotal();
		default:
			return null;
		}
	}

	@Override
	public ClientFinanceDate getStartDate(TransactionDetailByAccount obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(TransactionDetailByAccount obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		previousNumber = 0;
		previousType = 0;
	}

}
