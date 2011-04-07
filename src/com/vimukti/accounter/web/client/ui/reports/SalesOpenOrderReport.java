package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.OpenAndClosedOrders;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.SalesOpenOrderServerReport;

@SuppressWarnings("unchecked")
public class SalesOpenOrderReport extends
		AbstractReportView<OpenAndClosedOrders> {

	@SuppressWarnings("unused")
	private boolean isSales;

	public SalesOpenOrderReport() {
		this.serverReport = new SalesOpenOrderServerReport(this);
		isSales = FinanceApplication.isSales();
	}

	@Override
	public void init() {

		super.init();
		toolbar.setDateRanageOptions(FinanceApplication.getReportsMessages()
				.all(), FinanceApplication.getReportsMessages().thisWeek(),
				FinanceApplication.getReportsMessages().thisMonth(),
				FinanceApplication.getReportsMessages().lastWeek(),
				FinanceApplication.getReportsMessages().lastMonth(),
				FinanceApplication.getReportsMessages().thisFinancialYear(),
				FinanceApplication.getReportsMessages().lastFinancialYear(),
				FinanceApplication.getReportsMessages().thisFinancialQuarter(),
				FinanceApplication.getReportsMessages().lastFinancialQuarter(),

				// FinanceApplication
				// .getReportsMessages().present(), FinanceApplication
				// .getReportsMessages().last3Months(), FinanceApplication
				// .getReportsMessages().last6Months(), FinanceApplication
				// .getReportsMessages().lastYear(), FinanceApplication
				// .getReportsMessages().untilEndOfYear(),
				FinanceApplication.getReportsMessages().custom());
	}

	@Override
	public void OnRecordClick(OpenAndClosedOrders record) {
		if (FinanceApplication.getUser().canDoInvoiceTransactions())
			ReportsRPC.openTransactionView(record.getTransactionType(), record
					.getTransactionID());
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
			FinanceApplication.createReportService().getSalesOpenOrderReport(
					start.getTime(), end.getTime(), this);
		else if (status == 2)
			FinanceApplication.createReportService()
					.getSalesCompletedOrderReport(start.getTime(),
							end.getTime(), this);
		else if (status == 3)
			FinanceApplication.createReportService()
					.getSalesCancelledOrderReport(start.getTime(),
							end.getTime(), this);
		else
			FinanceApplication.createReportService().getSalesOrderReport(
					start.getTime(), end.getTime(), this);

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {

		UIUtils.generateReportPDF(Integer.parseInt(String.valueOf(startDate
				.getTime())), Integer.parseInt(String
				.valueOf(endDate.getTime())), 125, "", "");

		UIUtils.exportReport(Integer.parseInt(String.valueOf(startDate
				.getTime())), Integer.parseInt(String
				.valueOf(endDate.getTime())), 125, "", "");
	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	public int sort(OpenAndClosedOrders obj1, OpenAndClosedOrders obj2, int col) {

		int ret = obj1.getVendorOrCustomerName().toLowerCase().compareTo(
				obj2.getVendorOrCustomerName().toLowerCase());
		if (ret != 0) {
			return ret;
		}
		switch (col) {

		case 0:
			return obj1.getTransactionDate().compareTo(
					obj2.getTransactionDate());

		case 1:
			return obj1.getVendorOrCustomerName().toLowerCase().compareTo(
					obj2.getVendorOrCustomerName().toLowerCase());

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
}
