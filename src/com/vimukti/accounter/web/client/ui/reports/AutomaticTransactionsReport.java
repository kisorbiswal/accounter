package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByAccount;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.AutomaticTransactionsServerReport;

public class AutomaticTransactionsReport extends
		AbstractReportView<TransactionDetailByAccount> {

	public AutomaticTransactionsReport() {
		this.serverReport = new AutomaticTransactionsServerReport(this);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getAutomaticTransactions(start, end,
				this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(TransactionDetailByAccount record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			ReportsRPC.openTransactionView(record.getTransactionType(),
					record.getTransactionId());
		}
	}

	public int sort(TransactionDetailByAccount obj1,
			TransactionDetailByAccount obj2, int col) {
		switch (col) {
		case 1:
			String name1 = obj1.getName();
			String name2 = obj2.getName();
			return name1.toLowerCase().compareTo(name2.toLowerCase());
		case 2:
			return obj1.getTransactionDate().compareTo(
					obj2.getTransactionDate());
		case 3:
			int num1 = UIUtils.isInteger(obj1.getTransactionNumber()) ? Integer
					.parseInt(obj1.getTransactionNumber()) : 0;
			int num2 = UIUtils.isInteger(obj2.getTransactionNumber()) ? Integer
					.parseInt(obj2.getTransactionNumber()) : 0;
			if (num1 != 0 && num2 != 0)
				return UIUtils.compareInt(num1, num2);
			else
				return obj1.getTransactionNumber().compareTo(
						obj2.getTransactionNumber());

		case 4:
			return UIUtils.compareDouble(obj1.getTotal(), obj2.getTotal());
		}
		return 0;
	}

}