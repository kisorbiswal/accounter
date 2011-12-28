package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Lists.OpenAndClosedOrders;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.SalesOpenOrderServerReport;

public class SalesOpenOrderReport extends
		AbstractReportView<OpenAndClosedOrders> {

	private long status;

	public SalesOpenOrderReport() {
		this.serverReport = new SalesOpenOrderServerReport(this);
	}

	@Override
	public void init() {

		super.init();
		toolbar.setDateRanageOptions(messages.all(), messages.thisWeek(), messages.thisMonth(),
				messages.lastWeek(), messages
						.lastMonth(),
				messages.thisFinancialYear(), messages.lastFinancialYear(), messages
						.thisFinancialQuarter(), messages
						.lastFinancialQuarter(),

				// FinanceApplication
				// .constants().present(), FinanceApplication
				// .constants().last3Months(), FinanceApplication
				// .constants().last6Months(), FinanceApplication
				// .constants().lastYear(), FinanceApplication
				// .constants().untilEndOfYear(),
				messages.custom());
	}

	@Override
	public void OnRecordClick(OpenAndClosedOrders record) {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			ReportsRPC.openTransactionView(record.getTransactionType(),
					record.getTransactionID());
			// resetReport(toolbar.getStartDate(), toolbar.getEndDate());
		}
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_SALES_PURCAHASE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		// TODO Auto-generated method stub

	}

	@Override
	public void makeReportRequest(int status, ClientFinanceDate start,
			ClientFinanceDate end) {
		if (status == 1)
			Accounter.createReportService().getSalesOpenOrderReport(start, end,
					this);
		else if (status == 2)
			Accounter.createReportService().getSalesCompletedOrderReport(start,
					end, this);
		else if (status == 3)
			Accounter.createReportService().getSalesCancelledOrderReport(start,
					end, this);
		else
			Accounter.createReportService().getSalesOrderReport(start, end,
					this);

		this.status = status;
	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 125, "",
				"", status);
	}

	@Override
	public void printPreview() {

	}

	public int sort(OpenAndClosedOrders obj1, OpenAndClosedOrders obj2, int col) {

		int ret = obj1.getVendorOrCustomerName().toLowerCase()
				.compareTo(obj2.getVendorOrCustomerName().toLowerCase());
		if (ret != 0) {
			return ret;
		}
		switch (col) {

		case 0:
			return obj1.getTransactionDate().compareTo(
					obj2.getTransactionDate());

		case 1:
			return obj1.getVendorOrCustomerName().toLowerCase()
					.compareTo(obj2.getVendorOrCustomerName().toLowerCase());

			// case 2:
			// // if (isSales)
			// return obj1.getDescription().toLowerCase().compareTo(
			// obj2.getDescription().toLowerCase());
			// else
			// return UIUtils
			// .compareDouble(obj1.getAmount(), obj2.getAmount());
			// case 2:
			// return UIUtils
			// .compareDouble(obj1.getQuantity(), obj2.getQuantity());

		case 2:
			return UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());

		}
		return 0;
	}

	public void exportToCsv() {
		UIUtils.exportReport(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 125, "",
				"", status);
	}
}
