package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.PayeeStatementsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class CustomerStatementServerReport extends
		AbstractFinaneReport<PayeeStatementsList> {

	public CustomerStatementServerReport(
			IFinanceReport<PayeeStatementsList> reportView) {

		this.reportView = reportView;
	}

	public CustomerStatementServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { getConstants().date(), getConstants().type(),
				getConstants().no(), getConstants().dueDate(),
				getConstants().invoiceAmount(), getConstants().payment(),
				getConstants().balance() };
	}

	@Override
	public String getTitle() {
		return Accounter.messages().customerStatement(Global.get().Customer());
	}

	@Override
	public String[] getColunms() {
		return new String[] { getConstants().date(), getConstants().type(),
				getConstants().no(), getConstants().dueDate(),
				getConstants().invoiceAmount(), getConstants().payment(),
				getConstants().balance() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_DATE, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public void processRecord(PayeeStatementsList record) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getColumnData(PayeeStatementsList record, int columnIndex) {
		switch (columnIndex) {
		case 0:// Date
			return getDateByCompanyType(record.getTransactionDate());
		case 1:// TransactionName based on transaction type
			return Utility.getTransactionName(record.getTransactiontype());
		case 2:// Transaction number
			return record.getTransactionNumber();
		case 3:// Due date
			return record.getDueDate();
		case 4:// invoice amount
			if (record.getTransactiontype() == ClientTransaction.TYPE_INVOICE)
				return record.getTotal();
			break;
		case 5:// payment amount
			if (record.getTransactiontype() == ClientTransaction.TYPE_RECEIVE_PAYMENT)
				return record.getTotal();
			break;
		case 6:// balance
			return record.getBalance();
		}
		return null;
	}

	@Override
	public int getColumnWidth(int index) {
		switch (index) {
		case 0:
			return 150;
		case 2:
			return 200;
		case 3:
			return 200;
		case 4:
			return 200;
		default:
			return -1;
		}
	}

	@Override
	public ClientFinanceDate getStartDate(PayeeStatementsList obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(PayeeStatementsList obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isWiderReport() {
		return true;
	}
}
